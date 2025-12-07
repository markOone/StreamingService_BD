package dev.studentpp1.streamingservice.movies.mapper;

import dev.studentpp1.streamingservice.movies.dto.MovieDto;
import dev.studentpp1.streamingservice.movies.entity.Movie;
import org.springframework.stereotype.Component;

@Component
public class MovieMapper {

    public MovieDto toDto(Movie movie) {
        if (movie == null) return null;

        MovieDto dto = new MovieDto();
        dto.setId(movie.getId());
        dto.setTitle(movie.getTitle());
        dto.setDescription(movie.getDescription());
        dto.setYear(movie.getYear());
        dto.setRating(movie.getRating());

        if (movie.getDirector() != null) {
            dto.setDirectorId(movie.getDirector().getId());
        }

        return dto;
    }

    public Movie toEntity(MovieDto dto) {
        if (dto == null) return null;

        Movie movie = new Movie();
        movie.setTitle(dto.getTitle());
        movie.setDescription(dto.getDescription());
        movie.setYear(dto.getYear());
        movie.setRating(dto.getRating());

        return movie;
    }
}