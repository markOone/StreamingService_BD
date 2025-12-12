package dev.studentpp1.streamingservice.movies.dto;

public record ActorFilmographyDto(
        Long movieId,
        String movieTitle,
        Integer movieYear,
        String characterName
) {}