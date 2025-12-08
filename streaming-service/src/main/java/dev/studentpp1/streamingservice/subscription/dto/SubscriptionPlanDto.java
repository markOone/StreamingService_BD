package dev.studentpp1.streamingservice.subscription.dto;

import java.math.BigDecimal;

public record SubscriptionPlanDto(
        Long id,
        String name,
        String description,
        BigDecimal price,
        Integer duration) {
}
