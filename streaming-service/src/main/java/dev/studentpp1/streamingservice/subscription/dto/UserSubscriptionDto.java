package dev.studentpp1.streamingservice.subscription.dto;

import dev.studentpp1.streamingservice.subscription.entity.SubscriptionStatus;
import java.time.LocalDateTime;

public record UserSubscriptionDto(
    Long id,
    LocalDateTime startTime,
    LocalDateTime endTime,
    SubscriptionStatus status,
    String planName
) {

}
