package dev.studentpp1.streamingservice.subscription.controller;

import dev.studentpp1.streamingservice.subscription.dto.CreateSubscriptionPlanRequest;
import dev.studentpp1.streamingservice.subscription.dto.SubscriptionPlanDetailsDto;
import dev.studentpp1.streamingservice.subscription.dto.SubscriptionPlanSummaryDto;
import dev.studentpp1.streamingservice.subscription.service.SubscriptionPlanService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/subscription-plans")
@RequiredArgsConstructor
public class SubscriptionPlanController {

    private final SubscriptionPlanService subscriptionPlanService;

    @GetMapping
    public ResponseEntity<List<SubscriptionPlanSummaryDto>> getAllPlans() {
        return ResponseEntity.ok(subscriptionPlanService.getAllPlans());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubscriptionPlanDetailsDto> getPlanById(@PathVariable Long id) {
        return ResponseEntity.ok(subscriptionPlanService.getPlanById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SubscriptionPlanSummaryDto> createPlan(
        @Valid @RequestBody CreateSubscriptionPlanRequest request) {

        SubscriptionPlanSummaryDto createdPlan = subscriptionPlanService.createPlan(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdPlan);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SubscriptionPlanSummaryDto> updatePlan(
        @PathVariable("id") Long planId,
        @Valid @RequestBody CreateSubscriptionPlanRequest request) {

        return ResponseEntity.ok(subscriptionPlanService.updatePlan(planId, request));
    }

    @PostMapping("/{id}/movies")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity addMoviesToPlan(
        @PathVariable("id") Long planId,
        @RequestBody List<Long> movieIds) {

        return ResponseEntity.ok(subscriptionPlanService.addMoviesToPlan(planId, movieIds));
    }

    @DeleteMapping("/{id}/movies")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SubscriptionPlanSummaryDto> removeMoviesFromPlan(
        @PathVariable("id") Long planId,
        @RequestBody List<Long> movieIds) {

        return ResponseEntity.ok(subscriptionPlanService.removeMoviesFromPlan(planId, movieIds));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deletePlan(@PathVariable Long id) {
        subscriptionPlanService.deletePlan(id); // soft

        return ResponseEntity.noContent().build();
    }
}
