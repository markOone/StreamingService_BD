package dev.studentpp1.streamingservice.movies.mapper;

import dev.studentpp1.streamingservice.movies.dto.*;
import dev.studentpp1.streamingservice.movies.entity.Movie;
import dev.studentpp1.streamingservice.movies.entity.Performance;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", uses = {DirectorMapper.class})
public interface MovieMapper {

    @Mapping(source = "director.id", target = "directorId")
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

    @Mapping(source = "performances", target = "cast")
    MovieDetailDto toDetailDto(Movie movie);

    @Mapping(source = "actor.id", target = "actorId")
    @Mapping(source = "actor.name", target = "actorName")
    @Mapping(source = "actor.surname", target = "actorSurname")
    MovieCastDto performanceToCastDto(Performance performance);
}