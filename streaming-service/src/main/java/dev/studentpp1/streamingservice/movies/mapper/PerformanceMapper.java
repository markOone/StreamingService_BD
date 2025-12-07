package dev.studentpp1.streamingservice.movies.mapper;

import dev.studentpp1.streamingservice.movies.dto.PerformanceDto;
import dev.studentpp1.streamingservice.movies.entity.Performance;
import org.springframework.stereotype.Component;

@Component
public class PerformanceMapper {

    public PerformanceDto toDto(Performance performance) {
        if (performance == null) return null;

        PerformanceDto dto = new PerformanceDto();
        dto.setId(performance.getId());
        dto.setCharacterName(performance.getCharacterName());
        dto.setDescription(performance.getDescription());

        if (performance.getActor() != null) {
            dto.setActorId(performance.getActor().getId());
        }
        if (performance.getMovie() != null) {
            dto.setMovieId(performance.getMovie().getId());
        }

        return dto;
    }

    public Performance toEntity(PerformanceDto dto) {
        if (dto == null) return null;

        Performance performance = new Performance();
        performance.setCharacterName(dto.getCharacterName());
        performance.setDescription(dto.getDescription());
        return performance;
    }
}