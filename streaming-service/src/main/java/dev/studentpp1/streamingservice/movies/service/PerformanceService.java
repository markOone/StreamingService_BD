package dev.studentpp1.streamingservice.movies.service;

import dev.studentpp1.streamingservice.movies.dto.PerformanceDto;
import dev.studentpp1.streamingservice.movies.entity.Actor;
import dev.studentpp1.streamingservice.movies.entity.Movie;
import dev.studentpp1.streamingservice.movies.entity.Performance;
import dev.studentpp1.streamingservice.movies.mapper.PerformanceMapper;
import dev.studentpp1.streamingservice.movies.repository.ActorRepository;
import dev.studentpp1.streamingservice.movies.repository.MovieRepository;
import dev.studentpp1.streamingservice.movies.repository.PerformanceRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PerformanceService {

    private final PerformanceRepository performanceRepository;
    private final ActorRepository actorRepository;
    private final MovieRepository movieRepository;
    private final PerformanceMapper performanceMapper;

    public PerformanceService(PerformanceRepository performanceRepository,
                              ActorRepository actorRepository,
                              MovieRepository movieRepository,
                              PerformanceMapper performanceMapper) {
        this.performanceRepository = performanceRepository;
        this.actorRepository = actorRepository;
        this.movieRepository = movieRepository;
        this.performanceMapper = performanceMapper;
    }

    public List<PerformanceDto> getAllPerformances() {
        return performanceRepository.findAll().stream()
                .map(performanceMapper::toDto)
                .collect(Collectors.toList());
    }

    public PerformanceDto getPerformanceById(Long id) {
        Performance performance = performanceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Performance not found with id: " + id));
        return performanceMapper.toDto(performance);
    }

    public PerformanceDto createPerformance(PerformanceDto dto) {
        Actor actor = actorRepository.findById(dto.getActorId())
                .orElseThrow(() -> new RuntimeException("Actor not found with id: " + dto.getActorId()));

        Movie movie = movieRepository.findById(dto.getMovieId())
                .orElseThrow(() -> new RuntimeException("Movie not found with id: " + dto.getMovieId()));

        Performance performance = performanceMapper.toEntity(dto);
        performance.setActor(actor);
        performance.setMovie(movie);

        return performanceMapper.toDto(performanceRepository.save(performance));
    }

    public void deletePerformance(Long id) {
        if (!performanceRepository.existsById(id)) {
            throw new RuntimeException("Performance not found");
        }
        performanceRepository.deleteById(id);
    }
}