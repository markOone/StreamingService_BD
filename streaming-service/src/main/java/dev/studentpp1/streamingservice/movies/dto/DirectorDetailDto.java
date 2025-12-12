package dev.studentpp1.streamingservice.movies.dto;

import java.util.List;

public record DirectorDetailDto(
        Long id,
        String name,
        String surname,
        String biography,
        List<MovieDto> movies
) {}