package dev.studentpp1.streamingservice.subscription.entity;

import dev.studentpp1.streamingservice.users.entity.AppUser;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.type.PostgreSQLEnumJdbcType;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "user_subscription")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class UserSubscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_subscription_id")
    @EqualsAndHashCode.Include
    private Long id;

    @CreationTimestamp
    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalDateTime endTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @JdbcType(PostgreSQLEnumJdbcType.class)
    private SubscriptionStatus status;

    @ManyToOne(optional = false)
    @JoinColumn(name = "subscription_plan_id")
    private SubscriptionPlan plan;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private AppUser user;
}
