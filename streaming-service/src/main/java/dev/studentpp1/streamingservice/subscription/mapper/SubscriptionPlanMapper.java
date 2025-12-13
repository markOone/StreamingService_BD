package dev.studentpp1.streamingservice.subscription.mapper;

import dev.studentpp1.streamingservice.movies.mapper.MovieMapper;
import dev.studentpp1.streamingservice.subscription.dto.CreateSubscriptionPlanRequest;
import dev.studentpp1.streamingservice.subscription.dto.SubscriptionPlanDetailsDto;
import dev.studentpp1.streamingservice.subscription.dto.SubscriptionPlanSummaryDto;
import dev.studentpp1.streamingservice.subscription.entity.SubscriptionPlan;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueCheckStrategy;

@Mapper(
    componentModel = "spring",
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
    uses = {MovieMapper.class}
)
public interface SubscriptionPlanMapper {
    SubscriptionPlanSummaryDto toSummaryDto(SubscriptionPlan plan);
    SubscriptionPlanDetailsDto toDetailsDto(SubscriptionPlan plan);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "movies", ignore = true)
    SubscriptionPlan toEntity(CreateSubscriptionPlanRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "movies", ignore = true)
    void updateEntityFromDto(CreateSubscriptionPlanRequest request, @MappingTarget SubscriptionPlan plan);
}
