package dev.studentpp1.streamingservice.movies.dto;

public record ActorDto(
        Long id,
        String name,
        String surname,
        String biography
) {}