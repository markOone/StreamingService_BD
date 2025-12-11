package dev.studentpp1.streamingservice.movies.mapper;

import dev.studentpp1.streamingservice.movies.dto.PerformanceDto;
import dev.studentpp1.streamingservice.movies.dto.PerformanceRequest;
import dev.studentpp1.streamingservice.movies.entity.Performance;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PerformanceMapper {

    @Mapping(source = "actor.id", target = "actorId")
    @Mapping(source = "movie.id", target = "movieId")
    PerformanceDto toDto(Performance performance);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "actor", ignore = true)
    @Mapping(target = "movie", ignore = true)
    Performance toEntity(PerformanceRequest request);
}