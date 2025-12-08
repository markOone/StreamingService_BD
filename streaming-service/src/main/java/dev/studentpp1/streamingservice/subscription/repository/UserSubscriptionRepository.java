package dev.studentpp1.streamingservice.subscription.repository;

import dev.studentpp1.streamingservice.subscription.entity.UserSubscription;
import dev.studentpp1.streamingservice.users.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserSubscriptionRepository extends JpaRepository<UserSubscription, Long> {
    List<UserSubscription> findByUser(AppUser user);
}
