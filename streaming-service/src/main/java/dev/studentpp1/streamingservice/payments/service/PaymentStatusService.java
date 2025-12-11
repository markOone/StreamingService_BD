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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentStatusService {
    public static final String USER_ID = "userId";
    public static final String PLAN_NAME = "planName";
    public static final String ID = "id";
    public static final String METADATA = "metadata";
    public static final String AMOUNT = "amount";
    public static final String CURRENCY = "currency";

    private final SubscriptionService subscriptionService;

    private final PaymentRepository paymentRepository;

    public void handlePaymentEvent(Event event) {
        String eventType = event.getType();
        log.info("Received Stripe event: {}", eventType);
        switch (eventType) {
            case "payment_intent.succeeded" -> handlePaymentIntentSucceeded(event);
            case "payment_intent.payment_failed" -> handlePaymentIntentFailed(event);
            default -> log.info("Unhandled Stripe event type: {}", eventType);
        }
    }

    private PaymentIntentPayload parsePaymentIntentPayload(Event event) {
        var deserializer = event.getDataObjectDeserializer();
        try {
            String rawJson = deserializer.getRawJson();
            log.debug("Falling back to raw JSON for PaymentIntent: {}", rawJson);
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(rawJson);
            String paymentIntentId = root.path(ID).asText(null);
            JsonNode metadata = root.path(METADATA);
            String userId = metadata.path(USER_ID).asText(null);
            String planName = metadata.path(PLAN_NAME).asText(null);
            Long amount = root.path(AMOUNT).asLong();
            String currency = root.path(CURRENCY).asText(null);
            String failureMessage = root.path("last_payment_error").path("message").asText(null);
            log.debug("Parsed PaymentIntent via raw JSON: id={}, userId={}, planName={}, amount={}, currency={}",
                    paymentIntentId, userId, planName, amount, currency);
            return new PaymentIntentPayload(
                    paymentIntentId,
                    userId,
                    planName,
                    amount,
                    currency,
                    failureMessage
            );
        } catch (Exception e) {
            log.error("Failed to parse payment_intent payload", e);
            return null;
        }
    }

    @Transactional
    protected void handlePaymentIntentSucceeded(Event event) {
        PaymentIntentPayload payload = parsePaymentIntentPayload(event);
        if (payload == null) {
            log.warn("Skipping payment_intent.succeeded due to parse error");
            return;
        }
        UserSubscription userSubscription = subscriptionService.createUserSubscription(
                payload.planName(),
                payload.userId()
        );
        Payment payment = Payment.builder()
                .status(PaymentStatus.COMPLETED)
                .userSubscription(userSubscription)
                .amount(Math.toIntExact(payload.amount()))
                .build();
        paymentRepository.save(payment);
        log.info("Activated user subscription '{}' for user id={}",
                userSubscription, payload.userId()
        );
    }

    private void handlePaymentIntentFailed(Event event) {
        PaymentIntentPayload payload = parsePaymentIntentPayload(event);
        if (payload == null) {
            log.warn("Skipping payment_intent.payment_failed due to parse error");
            return;
        }
        Payment payment = Payment.builder()
                .status(PaymentStatus.FAILED)
                .amount(Math.toIntExact(payload.amount()))
                .build();
        paymentRepository.save(payment);
    }

    private record PaymentIntentPayload(
            String paymentIntentId,
            String userId,
            String planName,
            Long amount,
            String currency,
            String failureReason
    ) {
    }
}
