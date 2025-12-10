package dev.studentpp1.streamingservice.movies.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PerformanceRequest(
        @NotBlank String characterName,
        String description,
        @NotNull Long actorId,
        @NotNull Long movieId
) {}