package dev.studentpp1.streamingservice.payments.entity;

import dev.studentpp1.streamingservice.subscription.entity.UserSubscription;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.type.PostgreSQLOrdinalEnumJdbcType;

import java.time.LocalDateTime;

@Entity
@Table(name = "payment")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private Long id;

    @CreationTimestamp
    @Column(name = "paid_at", updatable = false)
    private LocalDateTime paidAt;

    @Column(nullable = false)
    private Integer amount;

    @JdbcType(PostgreSQLOrdinalEnumJdbcType.class)
    @Column(nullable = false)
    private PaymentStatus status;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_subscription_id")
    private UserSubscription userSubscription;
}
