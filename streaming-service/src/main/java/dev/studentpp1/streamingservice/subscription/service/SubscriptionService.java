package dev.studentpp1.streamingservice.subscription.service;

import dev.studentpp1.streamingservice.payments.dto.PaymentRequest;
import dev.studentpp1.streamingservice.payments.dto.PaymentResponse;
import dev.studentpp1.streamingservice.payments.service.PaymentService;
import dev.studentpp1.streamingservice.subscription.dto.SubscribeRequest;
import dev.studentpp1.streamingservice.subscription.dto.UserSubscriptionDto;
import dev.studentpp1.streamingservice.subscription.entity.SubscriptionPlan;
import dev.studentpp1.streamingservice.subscription.entity.SubscriptionStatus;
import dev.studentpp1.streamingservice.subscription.entity.UserSubscription;
import dev.studentpp1.streamingservice.subscription.exception.SubscriptionNotFoundException;
import dev.studentpp1.streamingservice.subscription.mapper.UserSubscriptionMapper;
import dev.studentpp1.streamingservice.subscription.repository.UserSubscriptionRepository;
import dev.studentpp1.streamingservice.users.entity.AppUser;
import dev.studentpp1.streamingservice.users.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SubscriptionService {
    private final SubscriptionPlanUtils subscriptionPlanUtils;
    private final UserSubscriptionRepository userSubscriptionRepository;
    private final UserSubscriptionMapper userSubscriptionMapper;
    private final UserService userService;
    private final PaymentService paymentService;

    @Transactional
    public PaymentResponse subscribeUser(SubscribeRequest request) {
        SubscriptionPlan plan = subscriptionPlanUtils.findById(request.planId());
        return paymentService.checkoutProduct(plan);
    }

    public UserSubscriptionDto createUserSubscription(String planName, String userId) {
        SubscriptionPlan plan = subscriptionPlanUtils.findByName(planName);
        AppUser user = userService.findById(Long.parseLong(userId));
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
        AppUser user = userService.findById(userId);
        return userSubscriptionRepository.findByUser(user)
                .stream()
                .map(userSubscriptionMapper::toDto)
                .toList();
    }

    @Transactional
    public void cancelSubscription(Long subscriptionId) {
        UserSubscription subscription = userSubscriptionRepository.findById(subscriptionId)
                .orElseThrow(
                        () -> new SubscriptionNotFoundException(
                                "Subscription not found with id " + subscriptionId));
        subscription.setStatus(SubscriptionStatus.CANCELLED);
        userSubscriptionRepository.save(subscription);
    }
}
