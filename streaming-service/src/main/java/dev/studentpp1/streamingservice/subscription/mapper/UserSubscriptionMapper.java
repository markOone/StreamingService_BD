package dev.studentpp1.streamingservice.subscription.mapper;

import dev.studentpp1.streamingservice.subscription.dto.UserSubscriptionDto;
import dev.studentpp1.streamingservice.subscription.entity.UserSubscription;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;

@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface UserSubscriptionMapper {

    @Mapping(source = "plan.id", target = "planId")
    @Mapping(source = "plan.name", target = "planName")
    @Mapping(source = "user.id", target = "userId")
    UserSubscriptionDto toDto(UserSubscription subscription);
}
