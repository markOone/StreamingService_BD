package dev.studentpp1.streamingservice.movies.service;

import dev.studentpp1.streamingservice.movies.dto.DirectorDto;
import dev.studentpp1.streamingservice.movies.dto.DirectorRequest;
import dev.studentpp1.streamingservice.movies.entity.Director;
import dev.studentpp1.streamingservice.movies.mapper.DirectorMapper;
import dev.studentpp1.streamingservice.movies.repository.DirectorRepository;
import org.springframework.stereotype.Service;

@Service
public class DirectorService {

    private final DirectorRepository directorRepository;
    private final DirectorMapper directorMapper;

    public DirectorService(DirectorRepository directorRepository, DirectorMapper directorMapper) {
        this.directorRepository = directorRepository;
        this.directorMapper = directorMapper;
    }

    public DirectorDto getDirectorById(Long id) {
        Director director = directorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Director not found with id: " + id));
        return directorMapper.toDto(director);
    }

    public DirectorDto createDirector(DirectorRequest request) {
        Director director = directorMapper.toEntity(request);
        return directorMapper.toDto(directorRepository.save(director));
    }

    public DirectorDto updateDirector(Long id, DirectorRequest request) {
        Director existingDirector = directorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Director not found with id: " + id));

        directorMapper.updateDirectorFromRequest(request, existingDirector);

        return directorMapper.toDto(directorRepository.save(existingDirector));
    }

    public void deleteDirector(Long id) {
        if (!directorRepository.existsById(id)) {
            throw new RuntimeException("Director not found with id: " + id);
        }
        directorRepository.deleteById(id);
    }
}