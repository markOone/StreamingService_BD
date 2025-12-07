package dev.studentpp1.streamingservice.movies.dto;

import lombok.Data;

@Data
public class MovieDto {
    private Long id;
    private String title;
    private String description;
    private Integer year;
    private Double rating;

    private Long directorId;
}