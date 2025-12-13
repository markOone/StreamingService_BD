package dev.studentpp1.streamingservice.subscription.service;

import dev.studentpp1.streamingservice.auth.persistence.AuthenticatedUser;
import dev.studentpp1.streamingservice.payments.dto.PaymentResponse;
import dev.studentpp1.streamingservice.payments.service.PaymentService;
import dev.studentpp1.streamingservice.subscription.dto.SubscribeRequest;
import dev.studentpp1.streamingservice.subscription.entity.SubscriptionPlan;
import dev.studentpp1.streamingservice.subscription.entity.SubscriptionStatus;
import dev.studentpp1.streamingservice.subscription.entity.UserSubscription;
import dev.studentpp1.streamingservice.subscription.exception.SubscriptionNotFoundException;
import dev.studentpp1.streamingservice.subscription.repository.UserSubscriptionRepository;
import dev.studentpp1.streamingservice.users.entity.AppUser;
import dev.studentpp1.streamingservice.users.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SubscriptionService {
    private final SubscriptionPlanUtils subscriptionPlanUtils;
    private final UserSubscriptionRepository userSubscriptionRepository;
    private final UserService userService;
    private final PaymentService paymentService;
    private final UserSubscriptionUtils userSubscriptionUtils;

    @Transactional
    public PaymentResponse subscribeUser(SubscribeRequest request, AuthenticatedUser currentUser) {
        SubscriptionPlan plan = subscriptionPlanUtils.findById(request.planId());
        createUserSubscription(
            plan.getName(),
            currentUser.getAppUser().getId().toString()
        );

        return paymentService.checkoutProduct(plan);
    }

    public UserSubscription createUserSubscription(String planName, String userId) {
        SubscriptionPlan plan = subscriptionPlanUtils.findByName(planName);
        AppUser user = userService.findById(Long.parseLong(userId));
        LocalDateTime startTime = LocalDateTime.now();
        UserSubscription subscription = UserSubscription.builder()
            .user(user)
            .plan(plan)
            .startTime(startTime)
            .endTime(startTime.plusDays(plan.getDuration()))
            .status(SubscriptionStatus.ACTIVE)
            .build();

        return userSubscriptionRepository.save(subscription);
    }

    public List<UserSubscription> getUserSubscriptions(AuthenticatedUser currentUser) {
        Long userId = currentUser.getAppUser().getId();
        AppUser user = userService.findById(userId);

        return userSubscriptionRepository.findByUser(user);
    }

    @Transactional
    public void cancelSubscription(Long subscriptionId, AuthenticatedUser currentUser) {
        Long userId = currentUser.getAppUser().getId();

        UserSubscription subscription = userSubscriptionUtils.findById(subscriptionId);

        if (!subscription.getUser().getId().equals(userId)) {
            throw new AccessDeniedException("You are not authorized to cancel this subscription");
        }

        if (!subscription.getStatus().equals(SubscriptionStatus.ACTIVE)) {
            throw new IllegalStateException("Only active subscriptions can be cancelled");
        }

        subscription.setStatus(SubscriptionStatus.CANCELLED);
        userSubscriptionRepository.save(subscription);
    }
}
