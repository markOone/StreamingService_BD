package dev.studentpp1.streamingservice.movies.service;

import dev.studentpp1.streamingservice.movies.dto.MovieDetailDto;
import dev.studentpp1.streamingservice.movies.dto.MovieDto;
import dev.studentpp1.streamingservice.movies.dto.MovieRequest;
import dev.studentpp1.streamingservice.movies.entity.Director;
import dev.studentpp1.streamingservice.movies.entity.Movie;
import dev.studentpp1.streamingservice.movies.mapper.MovieMapper;
import dev.studentpp1.streamingservice.movies.repository.DirectorRepository;
import dev.studentpp1.streamingservice.movies.repository.MovieRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MovieService {

    private final MovieRepository movieRepository;
    private final DirectorRepository directorRepository;
    private final MovieMapper movieMapper;

    public MovieService(MovieRepository movieRepository, DirectorRepository directorRepository, MovieMapper movieMapper) {
        this.movieRepository = movieRepository;
        this.directorRepository = directorRepository;
        this.movieMapper = movieMapper;
    }

    @Transactional(readOnly = true)
    public MovieDetailDto getMovieMethodDetails(Long id) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Movie not found with id: " + id));
        return movieMapper.toDetailDto(movie);
    }

    public List<MovieDto> getAllMovies() {
        return movieMapper.toDtoList(movieRepository.findAll());
    }

    public MovieDto getMovieById(Long id) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Movie not found with id: " + id));
        return movieMapper.toDto(movie);
    }

    public MovieDto createMovie(MovieRequest request) {
        Director director = directorRepository.findById(request.directorId())
                .orElseThrow(() -> new RuntimeException("Director not found with id: " + request.directorId()));

        Movie movie = movieMapper.toEntity(request);
        movie.setDirector(director);

        if (movie.getYear() != null && movie.getYear() <= 1880) {
            throw new RuntimeException("Movie year must be greater than 1880");
        }

        Movie savedMovie = movieRepository.save(movie);
        return movieMapper.toDto(savedMovie);
    }

    public MovieDto updateMovie(Long id, MovieRequest request) {
        Movie existingMovie = movieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Movie not found with id: " + id));

        movieMapper.updateMovieFromRequest(request, existingMovie);

        if (request.directorId() != null) {
            Director director = directorRepository.findById(request.directorId())
                    .orElseThrow(() -> new RuntimeException("Director not found with id: " + request.directorId()));
            existingMovie.setDirector(director);
        }

        return movieMapper.toDto(movieRepository.save(existingMovie));
    }

    public void deleteMovie(Long id) {
        if (!movieRepository.existsById(id)) {
            throw new RuntimeException("Movie not found with id: " + id);
        }
        movieRepository.deleteById(id);
    }
}