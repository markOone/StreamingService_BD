package dev.studentpp1.streamingservice.movies.dto;

import lombok.Data;

@Data
public class PerformanceDto {
    private Long id;
    private String characterName;
    private String description;

    private Long actorId;
    private Long movieId;
}