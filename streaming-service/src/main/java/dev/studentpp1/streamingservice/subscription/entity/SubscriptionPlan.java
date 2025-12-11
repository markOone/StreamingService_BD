package dev.studentpp1.streamingservice.subscription.entity;

import dev.studentpp1.streamingservice.movies.entity.Movie;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "subscription_plan")
public class SubscriptionPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "subscription_plan_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    private Integer duration;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "included_movie",
        joinColumns = @JoinColumn(name = "subscription_plan_id"),
        inverseJoinColumns = @JoinColumn(name = "movie_id")
    )
    @Builder.Default
    private Set<Movie> movies = new HashSet<>();

    @Builder.Default
    @Column(nullable = false)
    private boolean active = true;

    // Movie equals and hashCode
}
