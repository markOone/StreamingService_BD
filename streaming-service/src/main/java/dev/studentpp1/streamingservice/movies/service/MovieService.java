package dev.studentpp1.streamingservice.movies.service;

import dev.studentpp1.streamingservice.movies.dto.MovieDto;
import dev.studentpp1.streamingservice.movies.entity.Director;
import dev.studentpp1.streamingservice.movies.entity.Movie;
import dev.studentpp1.streamingservice.movies.mapper.MovieMapper;
import dev.studentpp1.streamingservice.movies.repository.DirectorRepository;
import dev.studentpp1.streamingservice.movies.repository.MovieRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MovieService {

    private final MovieRepository movieRepository;
    private final DirectorRepository directorRepository; // Треба, щоб знайти режисера
    private final MovieMapper movieMapper;

    public MovieService(MovieRepository movieRepository, DirectorRepository directorRepository, MovieMapper movieMapper) {
        this.movieRepository = movieRepository;
        this.directorRepository = directorRepository;
        this.movieMapper = movieMapper;
    }

    public List<MovieDto> getAllMovies() {
        return movieRepository.findAll().stream()
                .map(movieMapper::toDto)
                .collect(Collectors.toList());
    }

    public MovieDto getMovieById(Long id) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Movie not found with id: " + id));
        return movieMapper.toDto(movie);
    }

    public MovieDto createMovie(MovieDto dto) {
        Director director = directorRepository.findById(dto.getDirectorId())
                .orElseThrow(() -> new RuntimeException("Director not found with id: " + dto.getDirectorId()));

        Movie movie = movieMapper.toEntity(dto);

        movie.setDirector(director);

        if (movie.getYear() <= 1880) {
            throw new RuntimeException("Movie year must be greater than 1880");
        }

        Movie savedMovie = movieRepository.save(movie);
        return movieMapper.toDto(savedMovie);
    }

    public MovieDto updateMovie(Long id, MovieDto dto) {
        Movie existingMovie = movieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Movie not found with id: " + id));

        if (dto.getDirectorId() != null) {
            Director director = directorRepository.findById(dto.getDirectorId())
                    .orElseThrow(() -> new RuntimeException("Director not found with id: " + dto.getDirectorId()));
            existingMovie.setDirector(director);
        }

        existingMovie.setTitle(dto.getTitle());
        existingMovie.setDescription(dto.getDescription());
        existingMovie.setYear(dto.getYear());
        existingMovie.setRating(dto.getRating());

        return movieMapper.toDto(movieRepository.save(existingMovie));
    }

    public void deleteMovie(Long id) {
        if (!movieRepository.existsById(id)) {
            throw new RuntimeException("Movie not found with id: " + id);
        }
        movieRepository.deleteById(id);
    }
}