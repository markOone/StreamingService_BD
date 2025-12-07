package dev.studentpp1.streamingservice.subscription.service;

import dev.studentpp1.streamingservice.subscription.dto.CreateSubscriptionPlanRequest;
import dev.studentpp1.streamingservice.subscription.dto.SubscriptionPlanDto;
import dev.studentpp1.streamingservice.subscription.entity.SubscriptionPlan;
import dev.studentpp1.streamingservice.subscription.exception.SubscriptionPlanNotFoundException;
import dev.studentpp1.streamingservice.subscription.mapper.SubscriptionPlanMapper;
import dev.studentpp1.streamingservice.subscription.repository.SubscriptionPlanRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SubscriptionPlanService {

    private final SubscriptionPlanRepository subscriptionPlanRepository;
    private final SubscriptionPlanMapper subscriptionPlanMapper;

    public List<SubscriptionPlanDto> getAllPlans() {
        return subscriptionPlanRepository.findAll()
            .stream()
            .map(subscriptionPlanMapper::toDto)
            .toList();
    }

    public SubscriptionPlanDto getPlanById(Integer id) {
        return subscriptionPlanRepository.findById(id)
            .map(subscriptionPlanMapper::toDto)
            .orElseThrow(() -> new SubscriptionPlanNotFoundException(
                "Subscription Plan not found with id " + id));
    }

    @Transactional
    public SubscriptionPlanDto createPlan(CreateSubscriptionPlanRequest request) {
        SubscriptionPlan plan = subscriptionPlanMapper.toEntity(request);
        SubscriptionPlan savedPlan = subscriptionPlanRepository.save(plan);
        return subscriptionPlanMapper.toDto(savedPlan);
    }

    @Transactional
    public SubscriptionPlanDto updatePlan(Integer id, CreateSubscriptionPlanRequest request) {
        SubscriptionPlan plan = subscriptionPlanRepository.findById(id)
            .orElseThrow(() -> new SubscriptionPlanNotFoundException(
                "Subscription Plan not found with id " + id));

        plan.setName(request.name());
        plan.setDescription(request.description());
        plan.setPrice(request.price());
        plan.setDuration(request.duration());

        SubscriptionPlan updatedPlan = subscriptionPlanRepository.save(plan);
        return subscriptionPlanMapper.toDto(updatedPlan);
    }

    @Transactional
    public void deletePlan(Integer id) {
        if (!subscriptionPlanRepository.existsById(id)) {
            throw new SubscriptionPlanNotFoundException(
                "Subscription Plan not found with id " + id);
        }
        subscriptionPlanRepository.deleteById(id);
    }
}
