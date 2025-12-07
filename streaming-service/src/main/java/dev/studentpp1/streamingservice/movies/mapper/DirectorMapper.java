package dev.studentpp1.streamingservice.movies.mapper;

import dev.studentpp1.streamingservice.movies.dto.DirectorDto;
import dev.studentpp1.streamingservice.movies.entity.Director;
import org.springframework.stereotype.Component;

@Component
public class DirectorMapper {

    public DirectorDto toDto(Director director) {
        if (director == null) return null;

        DirectorDto dto = new DirectorDto();
        dto.setId(director.getId());
        dto.setName(director.getName());
        dto.setSurname(director.getSurname());
        dto.setBiography(director.getBiography());
        return dto;
    }

    public Director toEntity(DirectorDto dto) {
        if (dto == null) return null;

        Director director = new Director();
        director.setName(dto.getName());
        director.setSurname(dto.getSurname());
        director.setBiography(dto.getBiography());
        return director;
    }
}