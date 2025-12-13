package dev.studentpp1.streamingservice.subscription.controller;

import dev.studentpp1.streamingservice.subscription.dto.CreateSubscriptionPlanRequest;
import dev.studentpp1.streamingservice.subscription.dto.SubscriptionPlanDetailsDto;
import dev.studentpp1.streamingservice.subscription.dto.SubscriptionPlanSummaryDto;
import dev.studentpp1.streamingservice.subscription.mapper.SubscriptionPlanMapper;
import dev.studentpp1.streamingservice.subscription.mapper.UserSubscriptionMapper;
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
    private final SubscriptionPlanMapper subscriptionPlanMapper;

    @GetMapping
    public ResponseEntity<List<SubscriptionPlanSummaryDto>> getAllPlans() {
        var plans = subscriptionPlanService.getAllPlans()
            .stream()
            .map(subscriptionPlanMapper::toSummaryDto)
            .toList();

        return ResponseEntity.ok(plans);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubscriptionPlanDetailsDto> getPlanById(@PathVariable Long id) {

        var plan = subscriptionPlanMapper.toDetailsDto(
            subscriptionPlanService.getPlanById(id)
        );

        return ResponseEntity.ok(plan);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SubscriptionPlanDetailsDto> createPlan(
        @Valid @RequestBody CreateSubscriptionPlanRequest request) {

        var createdPlan = subscriptionPlanMapper.toDetailsDto(
            subscriptionPlanService.createPlan(request)
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(createdPlan);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SubscriptionPlanDetailsDto> updatePlan(
        @PathVariable("id") Long planId,
        @Valid @RequestBody CreateSubscriptionPlanRequest request) {

        var updatedPlan = subscriptionPlanMapper.toDetailsDto(
            subscriptionPlanService.updatePlan(planId, request)
        );

        return ResponseEntity.ok(updatedPlan);
    }

    @PostMapping("/{id}/movies")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SubscriptionPlanDetailsDto> addMoviesToPlan(
        @PathVariable("id") Long planId,
        @RequestBody List<Long> movieIds) {

        var updatedPlan = subscriptionPlanMapper.toDetailsDto(
            subscriptionPlanService.addMoviesToPlan(planId, movieIds)
        );

        return ResponseEntity.ok(updatedPlan);
    }

    @DeleteMapping("/{id}/movies")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SubscriptionPlanDetailsDto> removeMoviesFromPlan(
        @PathVariable("id") Long planId,
        @RequestBody List<Long> movieIds) {

        var updatedPlan = subscriptionPlanMapper.toDetailsDto(
            subscriptionPlanService.removeMoviesFromPlan(planId, movieIds)
        );

        return ResponseEntity.ok(updatedPlan);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deletePlan(@PathVariable Long id) {
        subscriptionPlanService.deletePlan(id);

        return ResponseEntity.noContent().build();
    }
}
