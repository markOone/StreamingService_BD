package dev.studentpp1.streamingservice.payments.dto;

import dev.studentpp1.streamingservice.payments.entity.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record HistoryPaymentResponse(
        PaymentStatus status,
        LocalDateTime paidAt,
        BigDecimal amount,
        String subscriptionName
) {
}
