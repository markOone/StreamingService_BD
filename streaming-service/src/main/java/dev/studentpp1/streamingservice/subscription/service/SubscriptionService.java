package dev.studentpp1.streamingservice.subscription.service;

import dev.studentpp1.streamingservice.subscription.dto.SubscribeRequest;
import dev.studentpp1.streamingservice.subscription.dto.UserSubscriptionDto;
import dev.studentpp1.streamingservice.subscription.entity.SubscriptionPlan;
import dev.studentpp1.streamingservice.subscription.entity.SubscriptionStatus;
import dev.studentpp1.streamingservice.subscription.entity.UserSubscription;
import dev.studentpp1.streamingservice.subscription.exception.SubscriptionNotFoundException;
import dev.studentpp1.streamingservice.subscription.exception.SubscriptionPlanNotFoundException;
import dev.studentpp1.streamingservice.subscription.mapper.UserSubscriptionMapper;
import dev.studentpp1.streamingservice.subscription.repository.SubscriptionPlanRepository;
import dev.studentpp1.streamingservice.subscription.repository.UserSubscriptionRepository;
import dev.studentpp1.streamingservice.users.entity.AppUser;
import dev.studentpp1.streamingservice.users.exception.UserNotFoundException;
import dev.studentpp1.streamingservice.users.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SubscriptionService {

    private final UserSubscriptionRepository userSubscriptionRepository;
    private final SubscriptionPlanRepository subscriptionPlanRepository;
    private final UserRepository userRepository;
    private final UserSubscriptionMapper userSubscriptionMapper;

    @Transactional
    public UserSubscriptionDto subscribeUser(SubscribeRequest request) {
        AppUser user = userRepository.findById(request.userId())
            .orElseThrow(
                () -> new UserNotFoundException("User not found with id " + request.userId()));
        SubscriptionPlan plan = subscriptionPlanRepository.findById(request.planId())
            .orElseThrow(() -> new SubscriptionPlanNotFoundException(
                "Plan not found with id " + request.planId()));

        UserSubscription subscription = new UserSubscription();
        subscription.setUser(user);
        subscription.setPlan(plan);
        subscription.setStartTime(LocalDateTime.now());
        subscription.setEndTime(LocalDateTime.now().plusDays(plan.getDuration()));
        subscription.setStatus(SubscriptionStatus.ACTIVE);

        UserSubscription savedSubscription = userSubscriptionRepository.save(subscription);
        return userSubscriptionMapper.toDto(savedSubscription);
    }

    public List<UserSubscriptionDto> getUserSubscriptions(Long userId) {
        AppUser user = userRepository.findById(userId)
            .orElseThrow(() -> new UserNotFoundException("User not found with id " + userId));
        return userSubscriptionRepository.findByUser(user)
            .stream()
            .map(userSubscriptionMapper::toDto)
            .toList();
    }

    @Transactional
    public void cancelSubscription(Integer subscriptionId) {
        UserSubscription subscription = userSubscriptionRepository.findById(subscriptionId)
            .orElseThrow(
                () -> new SubscriptionNotFoundException(
                    "Subscription not found with id " + subscriptionId));
        subscription.setStatus(SubscriptionStatus.CANCELLED);
        userSubscriptionRepository.save(subscription);
    }
}
