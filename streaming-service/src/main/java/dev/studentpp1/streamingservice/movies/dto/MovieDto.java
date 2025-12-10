package dev.studentpp1.streamingservice.movies.dto;

public record MovieDto (
        Long id,
        String title,
        String description,
        Integer year,
        Double rating,
        Long directorId
){
}