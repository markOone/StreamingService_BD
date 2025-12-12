package dev.studentpp1.streamingservice.movies.dto;

public record MovieCastDto(
        Long actorId,
        String actorName,
        String actorSurname,
        String characterName
) {}