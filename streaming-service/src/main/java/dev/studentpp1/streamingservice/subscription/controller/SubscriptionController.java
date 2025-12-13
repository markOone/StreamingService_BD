package dev.studentpp1.streamingservice.subscription.controller;

import dev.studentpp1.streamingservice.auth.persistence.AuthenticatedUser;
import dev.studentpp1.streamingservice.payments.dto.PaymentResponse;
import dev.studentpp1.streamingservice.subscription.dto.SubscribeRequest;
import dev.studentpp1.streamingservice.subscription.dto.UserSubscriptionDto;
import dev.studentpp1.streamingservice.subscription.entity.UserSubscription;
import dev.studentpp1.streamingservice.subscription.mapper.UserSubscriptionMapper;
import dev.studentpp1.streamingservice.subscription.service.SubscriptionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subscriptions")
@RequiredArgsConstructor
public class SubscriptionController {

    private final SubscriptionService subscriptionService;
    private final UserSubscriptionMapper userSubscriptionMapper;

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<PaymentResponse> subscribe(
        @Valid @RequestBody SubscribeRequest request,
        @AuthenticationPrincipal AuthenticatedUser currentUser
    ) {
        PaymentResponse paymentResponse = subscriptionService.subscribeUser(request, currentUser);

        return ResponseEntity.status(HttpStatus.CREATED).body(paymentResponse);
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<UserSubscriptionDto>> getMySubscriptions(
        @AuthenticationPrincipal AuthenticatedUser currentUser
    ) {
        var subscriptions = subscriptionService.getUserSubscriptions(currentUser)
            .stream()
            .map(userSubscriptionMapper::toDto)
            .toList();

        return ResponseEntity.ok(subscriptions);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> cancelSubscription(
        @PathVariable("id") Long subscriptionId,
        @AuthenticationPrincipal AuthenticatedUser currentUser
    ) {
        subscriptionService.cancelSubscription(subscriptionId, currentUser);

        return ResponseEntity.noContent().build();
    }
}
