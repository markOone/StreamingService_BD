package dev.studentpp1.streamingservice.subscription.repository;

import dev.studentpp1.streamingservice.subscription.entity.IncludedMovie;
import dev.studentpp1.streamingservice.subscription.entity.SubscriptionPlan;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IncludedMovieRepository extends JpaRepository<IncludedMovie, IncludedMovie.IncludedMovieId> {
    List<IncludedMovie> findBySubscriptionPlan(SubscriptionPlan plan);
}
