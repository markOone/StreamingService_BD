package dev.studentpp1.streamingservice.payments.dto;

public record PaymentRequest(
        Long amount,
        Long quantity,
        String productName,
        String currency
) {
}
