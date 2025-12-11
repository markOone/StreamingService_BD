package dev.studentpp1.streamingservice.movies.dto;

import jakarta.validation.constraints.*;

public record ActorRequest(
        @NotBlank String name,
        @NotBlank String surname,
        String biography
) {}