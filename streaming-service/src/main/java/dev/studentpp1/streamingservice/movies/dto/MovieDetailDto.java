package dev.studentpp1.streamingservice.movies.dto;

import java.util.List;

public record MovieDetailDto(
        Long id,
        String title,
        String description,
        Integer year,
        Double rating,
        DirectorDto director,
        List<MovieCastDto> cast
) {}