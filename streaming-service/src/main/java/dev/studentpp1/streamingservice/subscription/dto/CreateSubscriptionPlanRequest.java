package dev.studentpp1.streamingservice.subscription.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.List;

public record CreateSubscriptionPlanRequest(
        @NotNull
        @Size(max = 150, message = "Name must not exceed 150 characters")
        String name,

        @NotNull
        String description,

        @NotNull
        @Positive
        BigDecimal price,

        @NotNull
        @Positive
        Integer duration,

        List<Long> includedMovieIds
) {

}
