package dev.studentpp1.streamingservice.subscription.repository;

import dev.studentpp1.streamingservice.subscription.entity.UserSubscription;
import dev.studentpp1.streamingservice.users.entity.AppUser;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserSubscriptionRepository extends JpaRepository<UserSubscription, Integer> {
    List<UserSubscription> findByUser(AppUser user);

    Optional<UserSubscription> findTopByUserOrderByEndTimeDesc(AppUser user);
}
