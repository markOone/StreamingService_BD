package dev.studentpp1.streamingservice.movies.repository;

import dev.studentpp1.streamingservice.movies.entity.Director;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DirectorRepository extends JpaRepository<Director, Long> {
}