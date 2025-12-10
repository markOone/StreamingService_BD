package dev.studentpp1.streamingservice.movies.dto;

import jakarta.validation.constraints.NotBlank;

public record DirectorRequest(
        @NotBlank String name,
        @NotBlank String surname,
        String biography
) {}