package dev.studentpp1.streamingservice.movies.service;

import dev.studentpp1.streamingservice.movies.dto.PerformanceDto;
import dev.studentpp1.streamingservice.movies.dto.PerformanceRequest;
import dev.studentpp1.streamingservice.movies.entity.Actor;
import dev.studentpp1.streamingservice.movies.entity.Movie;
import dev.studentpp1.streamingservice.movies.entity.Performance;
import dev.studentpp1.streamingservice.movies.mapper.PerformanceMapper;
import dev.studentpp1.streamingservice.movies.repository.ActorRepository;
import dev.studentpp1.streamingservice.movies.repository.MovieRepository;
import dev.studentpp1.streamingservice.movies.repository.PerformanceRepository;
import org.springframework.stereotype.Service;

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

    public PerformanceDto getPerformanceById(Long id) {
        Performance performance = performanceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Performance not found with id: " + id));
        return performanceMapper.toDto(performance);
    }

    public PerformanceDto createPerformance(PerformanceRequest request) {
        Actor actor = actorRepository.findById(request.actorId())
                .orElseThrow(() -> new RuntimeException("Actor not found with id: " + request.actorId()));

        Movie movie = movieRepository.findById(request.movieId())
                .orElseThrow(() -> new RuntimeException("Movie not found with id: " + request.movieId()));

        Performance performance = performanceMapper.toEntity(request);
        performance.setActor(actor);
        performance.setMovie(movie);

        return performanceMapper.toDto(performanceRepository.save(performance));
    }

    public void deletePerformance(Long id) {
        if (!performanceRepository.existsById(id)) {
            throw new RuntimeException("Performance not found with id: " + id);
        }
        performanceRepository.deleteById(id);
    }
}