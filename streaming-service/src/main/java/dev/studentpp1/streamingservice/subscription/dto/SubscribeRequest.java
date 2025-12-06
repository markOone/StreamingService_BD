package dev.studentpp1.streamingservice.subscription.dto;

import jakarta.validation.constraints.NotNull;

public record SubscribeRequest(
        @NotNull Long userId,
        @NotNull Integer planId) {
}
