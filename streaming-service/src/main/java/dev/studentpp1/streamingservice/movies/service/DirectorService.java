package dev.studentpp1.streamingservice.movies.service;

import dev.studentpp1.streamingservice.movies.dto.DirectorDetailDto;
import dev.studentpp1.streamingservice.movies.dto.DirectorDto;
import dev.studentpp1.streamingservice.movies.dto.DirectorRequest;
import dev.studentpp1.streamingservice.movies.dto.MovieDto;
import dev.studentpp1.streamingservice.movies.entity.Director;
import dev.studentpp1.streamingservice.movies.entity.Movie;
import dev.studentpp1.streamingservice.movies.mapper.DirectorMapper;
import dev.studentpp1.streamingservice.movies.mapper.MovieMapper;
import dev.studentpp1.streamingservice.movies.repository.DirectorRepository;
import dev.studentpp1.streamingservice.movies.repository.MovieRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DirectorService {

    private final DirectorRepository directorRepository;
    private final MovieRepository movieRepository;
    private final DirectorMapper directorMapper;
    private final MovieMapper movieMapper;

    public DirectorService(DirectorRepository directorRepository, MovieRepository movieRepository, DirectorMapper directorMapper, MovieMapper movieMapper) {
        this.directorRepository = directorRepository;
        this.movieRepository = movieRepository;
        this.directorMapper = directorMapper;
        this.movieMapper = movieMapper;
    }

    @Transactional(readOnly = true)
    public DirectorDetailDto getDirectorDetails(Long id) {
        Director director = directorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Director not found with id: " + id));

        List<Movie> movies = movieRepository.findAllByDirectorId(id);
        List<MovieDto> movieDtos = movieMapper.toDtoList(movies);

        // Передаємо фільми вручну
        return directorMapper.toDetailDto(director, movieDtos);
    }

    public DirectorDto getDirectorById(Long id) {
        Director director = directorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Director not found with id: " + id));
        return directorMapper.toDto(director);
    }

    public DirectorDto createDirector(DirectorRequest request) {
        Director director = directorMapper.toEntity(request);
        Director savedDirector = directorRepository.save(director);
        return directorMapper.toDto(savedDirector);
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