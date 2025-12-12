package dev.studentpp1.streamingservice.movies.mapper;

import dev.studentpp1.streamingservice.movies.dto.*;
import dev.studentpp1.streamingservice.movies.entity.Actor;
import dev.studentpp1.streamingservice.movies.entity.Performance;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ActorMapper {
    ActorDto toDto(Actor actor);
    Actor toEntity(ActorRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
            nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
    void updateActorFromRequest(ActorRequest request, @MappingTarget Actor actor);

    @Mapping(source = "performances", target = "filmography")
    ActorDetailDto toDetailDto(Actor actor);

    @Mapping(source = "movie.id", target = "movieId")
    @Mapping(source = "movie.title", target = "movieTitle")
    @Mapping(source = "movie.year", target = "movieYear")
    ActorFilmographyDto performanceToFilmographyDto(Performance performance);
}