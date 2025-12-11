package dev.studentpp1.streamingservice.subscription.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record CreateSubscriptionPlanRequest(
        @NotNull String name,
        @NotNull String description,
        @NotNull @Positive BigDecimal price,
        @NotNull @Positive Integer duration) {
}
