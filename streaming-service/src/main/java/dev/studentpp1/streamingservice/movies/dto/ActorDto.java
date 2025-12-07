package dev.studentpp1.streamingservice.movies.dto;


import lombok.Data;

@Data
public class ActorDto {
    private Long id;
    private String name;
    private String surname;
    private String biography;
}
