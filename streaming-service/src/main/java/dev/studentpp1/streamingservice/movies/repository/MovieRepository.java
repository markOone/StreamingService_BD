package dev.studentpp1.streamingservice.movies.repository;

import dev.studentpp1.streamingservice.movies.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
    List<Movie> findAllByDirectorId(Long directorId);
}