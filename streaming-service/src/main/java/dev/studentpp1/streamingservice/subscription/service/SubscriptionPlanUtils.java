package dev.studentpp1.streamingservice.subscription.service;

import dev.studentpp1.streamingservice.subscription.entity.SubscriptionPlan;
import dev.studentpp1.streamingservice.subscription.exception.SubscriptionNotFoundException;
import dev.studentpp1.streamingservice.subscription.exception.SubscriptionPlanNotFoundException;
import dev.studentpp1.streamingservice.subscription.repository.SubscriptionPlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SubscriptionPlanUtils {
    private final SubscriptionPlanRepository subscriptionPlanRepository;

    public SubscriptionPlan findByName(String planName) {
        return subscriptionPlanRepository
                .findByName(planName)
                .orElseThrow(() -> new SubscriptionPlanNotFoundException("Plan not found with name " + planName));
    }

    public SubscriptionPlan findById(Long subscriptionId) {
        return subscriptionPlanRepository.findById(subscriptionId)
                .orElseThrow(
                        () -> new SubscriptionNotFoundException(
                                "Subscription not found with id " + subscriptionId));
    }
}
