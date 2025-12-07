package dev.studentpp1.streamingservice.subscription.controller;

import dev.studentpp1.streamingservice.subscription.dto.SubscribeRequest;
import dev.studentpp1.streamingservice.subscription.dto.UserSubscriptionDto;
import dev.studentpp1.streamingservice.subscription.service.SubscriptionService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/subscriptions")
@RequiredArgsConstructor
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    @PostMapping("/subscribe")
    public ResponseEntity<UserSubscriptionDto> subscribe(@Valid @RequestBody SubscribeRequest request) {
        UserSubscriptionDto subscription = subscriptionService.subscribeUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(subscription);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<UserSubscriptionDto>> getUserSubscriptions(@PathVariable Long userId) {
        return ResponseEntity.ok(subscriptionService.getUserSubscriptions(userId));
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<Void> cancelSubscription(@PathVariable Integer id) {
        subscriptionService.cancelSubscription(id);
        return ResponseEntity.ok().build();
    }
}
