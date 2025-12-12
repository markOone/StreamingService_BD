package dev.studentpp1.streamingservice.payments.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stripe.model.Event;
import dev.studentpp1.streamingservice.payments.entity.Payment;
import dev.studentpp1.streamingservice.payments.entity.PaymentStatus;
import dev.studentpp1.streamingservice.payments.repository.PaymentRepository;
import dev.studentpp1.streamingservice.subscription.entity.UserSubscription;
import dev.studentpp1.streamingservice.subscription.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentWebhookService {

    private static final String EVENT_SESSION_COMPLETED = "checkout.session.completed";
    private static final String EVENT_SESSION_EXPIRED = "checkout.session.expired";

    private final PaymentRepository paymentRepository;
    private final SubscriptionService subscriptionService;
    private final ObjectMapper mapper = new ObjectMapper();

    @Transactional
    public void handlePaymentEvent(Event event) {
        String type = event.getType();
        log.info("Received Stripe event type={}", type);

        switch (type) {
            case EVENT_SESSION_COMPLETED -> handleSuccess(event);
            case EVENT_SESSION_EXPIRED -> handleFailed(event);
            default -> log.info("Ignored Stripe event type={}", type);
        }
    }

    private void handleSuccess(Event event) {
        SessionPayload payload = parse(event);
        if (payload == null) {
            log.warn("{} skipped: payload is null", EVENT_SESSION_COMPLETED);
            return;
        }

        Payment payment = paymentRepository.findByProviderPaymentIdForUpdate(payload.sessionId())
                .orElseThrow(() -> new IllegalStateException("Payment not found for sessionId=" + payload.sessionId()));

        if (payment.getStatus() == PaymentStatus.COMPLETED) {
            log.info("Payment already COMPLETED, sessionId={}", payload.sessionId());
            return;
        }

        UserSubscription subscription = subscriptionService.createUserSubscription(payload.planName(), payload.userId());

        payment.setStatus(PaymentStatus.COMPLETED);
        payment.setPaidAt(LocalDateTime.now());
        payment.setUserSubscription(subscription);

        log.info("Payment COMPLETED, sessionId={}, userId={}, plan={}",
                payload.sessionId(), payload.userId(), payload.planName());
    }

    private void handleFailed(Event event) {
        SessionPayload payload = parse(event);
        if (payload == null) {
            log.warn("{} skipped: payload is null", EVENT_SESSION_EXPIRED);
            return;
        }

        Payment payment = paymentRepository.findByProviderPaymentIdForUpdate(payload.sessionId())
                .orElseThrow(() -> new IllegalStateException("Payment not found for sessionId=" + payload.sessionId()));

        if (payment.getStatus() == PaymentStatus.COMPLETED) {
            log.info("Ignoring FAILED event for COMPLETED payment, sessionId={}", payload.sessionId());
            return;
        }

        payment.setStatus(PaymentStatus.FAILED);

        log.info("Payment FAILED, sessionId={}, userId={}, plan={}",
                payload.sessionId(), payload.userId(), payload.planName());
    }

    private SessionPayload parse(Event event) {
        try {
            String rawJson = event.getDataObjectDeserializer().getRawJson();
            JsonNode root = mapper.readTree(rawJson);

            String sessionId = root.path("id").asText(null);
            if (sessionId == null) return null;

            JsonNode metadata = root.path("metadata");
            String userId = metadata.path("userId").asText(null);
            String planName = metadata.path("planName").asText(null);

            if (userId == null || planName == null) return null;

            return new SessionPayload(sessionId, userId, planName);
        } catch (Exception e) {
            log.error("Failed to parse Stripe event payload", e);
            return null;
        }
    }

    @Scheduled(cron = "0 0 3 * * *")
    @Transactional
    public void deleteStalePendingPayments() {
        LocalDateTime threshold = LocalDateTime.now().minusHours(24);
        int deleted = paymentRepository.deleteByStatusAndCreatedAtBefore(PaymentStatus.PENDING, threshold);
        log.info("Deleted {} stale PENDING payments older than {}", deleted, threshold);
    }

    record SessionPayload(String sessionId, String userId, String planName) {
    }
}
