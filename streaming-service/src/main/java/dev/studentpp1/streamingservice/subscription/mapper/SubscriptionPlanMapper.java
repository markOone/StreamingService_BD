package dev.studentpp1.streamingservice.subscription.mapper;

import dev.studentpp1.streamingservice.subscription.dto.CreateSubscriptionPlanRequest;
import dev.studentpp1.streamingservice.subscription.dto.SubscriptionPlanDto;
import dev.studentpp1.streamingservice.subscription.entity.SubscriptionPlan;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;

@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface SubscriptionPlanMapper {
    SubscriptionPlanDto toDto(SubscriptionPlan pla);

    @Mapping(target = "id", ignore = true)
    SubscriptionPlan toEntity(CreateSubscriptionPlanRequest request);
}
