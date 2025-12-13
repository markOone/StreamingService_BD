package dev.studentpp1.streamingservice.movies.dto;

import jakarta.validation.constraints.*;

public record PerformanceRequest(
        @NotBlank(message = "Character name is required")
        @Size(max = 255, message = "Character name must be less than 255 characters")
        String characterName,

        String description,

        @NotNull(message = "Actor ID is required")
        Long actorId,

        @NotNull(message = "Movie ID is required")
        Long movieId
) {}