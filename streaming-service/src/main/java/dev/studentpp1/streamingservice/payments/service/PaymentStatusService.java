package dev.studentpp1.streamingservice.payments.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stripe.model.Event;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentStatusService {
    private final ObjectMapper objectMapper = new ObjectMapper();
    public void handlePaymentEvent(Event event) {
        String eventType = event.getType();
        log.info("Received Stripe event: {}", eventType);
        switch (eventType) {
            case "payment_intent.succeeded" -> handlePaymentIntentSucceeded(event);
            case "payment_intent.payment_failed" -> handlePaymentIntentFailed(event);
            default -> log.info("Unhandled Stripe event type: {}", eventType);
        }
    }
    private void handlePaymentIntentSucceeded(Event event) {
        PaymentIntentPayload payload = parsePaymentIntentPayload(event);
        if (payload == null) {
            log.warn("Skipping payment_intent.succeeded due to parse error");
            return;
        }
        handlePaymentSucceeded(payload.paymentIntentId(), payload.userId(), payload.planName());
    }
    private void handlePaymentIntentFailed(Event event) {
        PaymentIntentPayload payload = parsePaymentIntentPayload(event);
        if (payload == null) {
            log.warn("Skipping payment_intent.payment_failed due to parse error");
            return;
        }
        String failureMessage = payload.failureReason() != null
                ? payload.failureReason()
                : "Unknown reason";
        handlePaymentFailed(
                payload.paymentIntentId(),
                payload.userId(),
                payload.planName(),
                failureMessage
        );
    }
    private PaymentIntentPayload parsePaymentIntentPayload(Event event) {
        var deserializer = event.getDataObjectDeserializer();
        String rawJson = deserializer.getRawJson();
        try {
            JsonNode root = objectMapper.readTree(rawJson);
            String paymentIntentId = root.path("id").asText(null);
            JsonNode metadata = root.path("metadata");
            String userId = metadata.path("userId").asText(null);
            String planName = metadata.path("planName").asText(null);
            String failureMessage = root.path("last_payment_error")
                    .path("message")
                    .asText(null);
            log.debug("Parsed payment_intent payload: id={}, userId={}, planName={}",
                    paymentIntentId, userId, planName);
            return new PaymentIntentPayload(paymentIntentId, userId, planName, failureMessage);
        } catch (Exception e) {
            log.error("Failed to parse payment_intent JSON", e);
            return null;
        }
    }

    public void handlePaymentSucceeded(String paymentIntentId, String userId, String planName) {
        log.info("HandlePaymentSucceeded: paymentIntentId={}, userId={}, planName={}",
                paymentIntentId, userId, planName);
        if (userId == null || planName == null) {
            log.warn("Missing metadata in payment_intent.succeeded: userId={}, planName={}", userId, planName);
            return;
        }
        // TODO: create user subscription + success payment record
        log.info("Activated subscription plan '{}' for user id={}", planName, userId);
    }

    public void handlePaymentFailed(String paymentIntentId,
                                    String userId,
                                    String planName,
                                    String reason) {
        log.warn("payment_intent.payment_failed: paymentIntentId={}, userId={}, planName={}, reason={}",
                paymentIntentId, userId, planName, reason);
        // TODO: create failed payment record
    }

    private record PaymentIntentPayload(
            String paymentIntentId,
            String userId,
            String planName,
            String failureReason
    ) {}
}
