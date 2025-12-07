package dev.studentpp1.streamingservice.movies.service;

import dev.studentpp1.streamingservice.movies.dto.DirectorDto;
import dev.studentpp1.streamingservice.movies.entity.Director;
import dev.studentpp1.streamingservice.movies.mapper.DirectorMapper;
import dev.studentpp1.streamingservice.movies.repository.DirectorRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DirectorService {

    private final DirectorRepository directorRepository;
    private final DirectorMapper directorMapper;

    public DirectorService(DirectorRepository directorRepository, DirectorMapper directorMapper) {
        this.directorRepository = directorRepository;
        this.directorMapper = directorMapper;
    }

    public List<DirectorDto> getAllDirectors() {
        return directorRepository.findAll().stream()
                .map(directorMapper::toDto)
                .collect(Collectors.toList());
    }

    public DirectorDto getDirectorById(Long id) {
        Director director = directorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Director not found with id: " + id));
        return directorMapper.toDto(director);
    }

    public DirectorDto createDirector(DirectorDto dto) {
        Director director = directorMapper.toEntity(dto);
        Director savedDirector = directorRepository.save(director);
        return directorMapper.toDto(savedDirector);
    }

    public DirectorDto updateDirector(Long id, DirectorDto dto) {
        Director existingDirector = directorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Director not found with id: " + id));

        existingDirector.setName(dto.getName());
        existingDirector.setSurname(dto.getSurname());
        existingDirector.setBiography(dto.getBiography());

        return directorMapper.toDto(directorRepository.save(existingDirector));
    }

    public void deleteDirector(Long id) {
        if (!directorRepository.existsById(id)) {
            throw new RuntimeException("Director not found with id: " + id);
        }
        directorRepository.deleteById(id);
    }
}