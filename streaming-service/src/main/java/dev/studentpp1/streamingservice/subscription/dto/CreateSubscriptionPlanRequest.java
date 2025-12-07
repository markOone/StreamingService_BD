package dev.studentpp1.streamingservice.subscription.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CreateSubscriptionPlanRequest(
        @NotNull String name,
        @NotNull String description,
        @NotNull @Positive java.math.BigDecimal price,
        @NotNull @Positive Integer duration) {
}
