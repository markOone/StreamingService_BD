package dev.studentpp1.streamingservice.subscription.dto;

import java.math.BigDecimal;

public record SubscriptionPlanDto(
        Integer id,
        String name,
        String description,
        BigDecimal price,
        Integer duration) {
}
