package dev.studentpp1.streamingservice.movies.dto;

public record DirectorDto(
        Long id,
        String name,
        String surname,
        String biography
) {}