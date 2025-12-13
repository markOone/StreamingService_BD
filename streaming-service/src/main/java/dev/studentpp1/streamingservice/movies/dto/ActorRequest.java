package dev.studentpp1.streamingservice.movies.dto;

import jakarta.validation.constraints.*;

public record ActorRequest(
        @NotBlank(message = "Name is required")
        @Size(max = 100, message = "Name must be less than 100 characters")
        String name,

        @NotBlank(message = "Surname is required")
        @Size(max = 100, message = "Surname must be less than 100 characters")
        String surname,

        String biography
) {}