package dev.studentpp1.streamingservice.movies.repository;

import dev.studentpp1.streamingservice.movies.entity.Actor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface ActorRepository extends JpaRepository<Actor, Long> {
}
