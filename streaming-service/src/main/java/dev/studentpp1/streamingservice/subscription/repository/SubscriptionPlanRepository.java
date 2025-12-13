package dev.studentpp1.streamingservice.subscription.repository;

import dev.studentpp1.streamingservice.subscription.entity.SubscriptionPlan;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriptionPlanRepository extends JpaRepository<SubscriptionPlan, Long> {
    Optional<SubscriptionPlan> findByName(String name);

    @EntityGraph(attributePaths = "movies")
    Optional<SubscriptionPlan> findWithMoviesById(Long id);
}
