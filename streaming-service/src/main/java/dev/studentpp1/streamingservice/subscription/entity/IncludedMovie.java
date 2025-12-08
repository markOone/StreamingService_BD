package dev.studentpp1.streamingservice.subscription.entity;

import dev.studentpp1.streamingservice.movies.entity.Movie;
import jakarta.persistence.*;
import java.io.Serializable;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "included_movie")
public class IncludedMovie {

    @EmbeddedId
    private IncludedMovieId id;

    @ManyToOne
    @MapsId("movieId")
    @JoinColumn(name = "movie_id")
    private Movie movie;

    @ManyToOne
    @MapsId("subscriptionPlanId")
    @JoinColumn(name = "subscription_plan_id")
    private SubscriptionPlan subscriptionPlan;

    @Embeddable
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @EqualsAndHashCode
    public static class IncludedMovieId implements Serializable {
        @Column(name = "movie_id")
        private Long movieId;

        @Column(name = "subscription_plan_id")
        private Long subscriptionPlanId;
    }
}
