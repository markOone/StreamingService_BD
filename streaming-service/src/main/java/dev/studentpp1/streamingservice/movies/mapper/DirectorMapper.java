package dev.studentpp1.streamingservice.movies.mapper;

import dev.studentpp1.streamingservice.movies.dto.*;
import dev.studentpp1.streamingservice.movies.entity.Director;
import org.mapstruct.*;
import java.util.List;

@Mapper(componentModel = "spring")
public interface DirectorMapper {
    DirectorDto toDto(Director director);
    Director toEntity(DirectorRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
            nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
    void updateDirectorFromRequest(DirectorRequest request, @MappingTarget Director director);
    
    DirectorDetailDto toDetailDto(Director director, List<MovieDto> movies);
}