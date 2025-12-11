package dev.studentpp1.streamingservice.movies.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record MovieRequest(
        @NotBlank String title,
        String description,
        @Min(1880) Integer year,
        Double rating,
        @NotNull Long directorId
) {}