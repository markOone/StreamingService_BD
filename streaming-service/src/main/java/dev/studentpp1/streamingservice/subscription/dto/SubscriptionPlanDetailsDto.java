package dev.studentpp1.streamingservice.subscription.dto;

import dev.studentpp1.streamingservice.movies.dto.MovieDto;
import java.math.BigDecimal;
import java.util.Set;

public record SubscriptionPlanDetailsDto(
    Long id,
    String name,
    String description,
    BigDecimal price,
    Integer duration,
    Set<MovieDto> movies
) {

}
