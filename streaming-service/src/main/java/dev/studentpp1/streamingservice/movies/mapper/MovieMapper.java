package dev.studentpp1.streamingservice.movies.mapper;

import dev.studentpp1.streamingservice.movies.dto.MovieDto;
import dev.studentpp1.streamingservice.movies.dto.MovieRequest;
import dev.studentpp1.streamingservice.movies.entity.Movie;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface MovieMapper {

    @Mapping(source = "director.id", target = "directorId")
    @Mapping(source = "director.name", target = "directorName")
    @Mapping(source = "director.surname", target = "directorSurname")
    MovieDto toDto(Movie movie);

    List<MovieDto> toDtoList(List<Movie> movies);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "director", ignore = true)
    @Mapping(target = "performances", ignore = true)
    Movie toEntity(MovieRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
            nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "director", ignore = true)
    @Mapping(target = "performances", ignore = true)
    void updateMovieFromRequest(MovieRequest request, @MappingTarget Movie movie);
}