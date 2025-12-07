package dev.studentpp1.streamingservice.movies.mapper;

import dev.studentpp1.streamingservice.movies.dto.MovieDto;
import dev.studentpp1.streamingservice.movies.entity.Movie;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MovieMapper {

    @Mapping(source = "director.id", target = "directorId")
    MovieDto toDto(Movie movie);

    @Mapping(target = "director", ignore = true)
    @Mapping(target = "performances", ignore = true)
    Movie toEntity(MovieDto movieDto);
}