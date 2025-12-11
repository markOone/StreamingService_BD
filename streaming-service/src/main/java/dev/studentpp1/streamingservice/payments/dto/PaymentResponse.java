package dev.studentpp1.streamingservice.payments.dto;

public record PaymentResponse(
        String status,
        String message,
        String sessionId,
        String sessionUrl
) {
}
