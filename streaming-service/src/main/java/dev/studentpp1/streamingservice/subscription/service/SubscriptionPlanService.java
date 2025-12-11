package dev.studentpp1.streamingservice.subscription.service;

import dev.studentpp1.streamingservice.movies.entity.Movie;
import dev.studentpp1.streamingservice.movies.repository.MovieRepository;
import dev.studentpp1.streamingservice.subscription.dto.CreateSubscriptionPlanRequest;
import dev.studentpp1.streamingservice.subscription.dto.SubscriptionPlanDetailsDto;
import dev.studentpp1.streamingservice.subscription.dto.SubscriptionPlanSummaryDto;
import dev.studentpp1.streamingservice.subscription.entity.SubscriptionPlan;
import dev.studentpp1.streamingservice.subscription.exception.SubscriptionPlanNotFoundException;
import dev.studentpp1.streamingservice.subscription.mapper.SubscriptionPlanMapper;
import dev.studentpp1.streamingservice.subscription.repository.SubscriptionPlanRepository;
import java.util.HashSet;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SubscriptionPlanService {

    private final SubscriptionPlanRepository subscriptionPlanRepository;
    private final MovieRepository movieRepository;
    private final SubscriptionPlanMapper subscriptionPlanMapper;
    private final SubscriptionPlanUtils subscriptionPlanUtils;

    public List<SubscriptionPlanSummaryDto> getAllPlans() {
        return subscriptionPlanRepository.findAll()
            .stream()
            .map(subscriptionPlanMapper::toSummaryDto)
            .toList();
    }

    public SubscriptionPlanDetailsDto getPlanById(Long id) {
        return subscriptionPlanRepository.findById(id)
            .map(subscriptionPlanMapper::toDetailsDto)
            .orElseThrow(() -> new SubscriptionPlanNotFoundException(
                "Subscription Plan not found with id " + id));
    }

    @Transactional
    public SubscriptionPlanSummaryDto createPlan(CreateSubscriptionPlanRequest request) {
        SubscriptionPlan plan = subscriptionPlanMapper.toEntity(request);

        if (request.includedMovieIds() != null && !request.includedMovieIds().isEmpty()) {
            List<Movie> movies = movieRepository.findAllById(request.includedMovieIds());

            if (movies.size() != request.includedMovieIds().size()) {
                throw new IllegalArgumentException("Some movies were not found");
            }

            plan.setMovies(new HashSet<>(movies));
        }

        SubscriptionPlan savedPlan = subscriptionPlanRepository.save(plan);
        return subscriptionPlanMapper.toSummaryDto(savedPlan);
    }

    @Transactional
    public SubscriptionPlanSummaryDto updatePlan(Long id, CreateSubscriptionPlanRequest request) {
        SubscriptionPlan plan = subscriptionPlanUtils.findById(id);

        subscriptionPlanMapper.updateEntityFromDto(request, plan);

        if (request.includedMovieIds() != null) {
            List<Movie> movies = movieRepository.findAllById(request.includedMovieIds());
            plan.setMovies(new HashSet<>(movies));
        }

        SubscriptionPlan updatedPlan = subscriptionPlanRepository.save(plan);
        return subscriptionPlanMapper.toSummaryDto(updatedPlan);
    }

    @Transactional
    public SubscriptionPlanSummaryDto addMoviesToPlan(Long planId, List<Long> movieIds) {
        SubscriptionPlan plan = subscriptionPlanUtils.findById(planId);
        List<Movie> newMovies = movieRepository.findAllById(movieIds);

        plan.getMovies().addAll(newMovies);
        SubscriptionPlan updatedPlan = subscriptionPlanRepository.save(plan);

        return subscriptionPlanMapper.toSummaryDto(updatedPlan);
    }

    @Transactional
    public void deletePlan(Long id) {
        if (!subscriptionPlanRepository.existsById(id)) {
            throw new SubscriptionPlanNotFoundException(
                "Subscription Plan not found with id " + id);
        }
        subscriptionPlanRepository.deleteById(id);
    }

    @Transactional
    public SubscriptionPlanSummaryDto removeMoviesFromPlan(Long planId, List<Long> movieIds) {
        SubscriptionPlan plan = subscriptionPlanUtils.findById(planId);

        boolean changed = plan.getMovies().removeIf(movie -> movieIds.contains(movie.getId()));

        if (!changed) {
            throw new IllegalArgumentException("None of the provided movies were found in the plan");
        }

        SubscriptionPlan updatedPlan = subscriptionPlanRepository.save(plan);
        return subscriptionPlanMapper.toSummaryDto(updatedPlan);
    }
}
