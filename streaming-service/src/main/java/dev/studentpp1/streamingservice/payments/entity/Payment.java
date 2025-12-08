package dev.studentpp1.streamingservice.payments.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.type.PostgreSQLOrdinalEnumJdbcType;

import java.time.LocalDate;

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
    @Column(name = "paid_at")
    private LocalDate paidAt;

    @Column(nullable = false)
    private Integer amount;

    @JdbcType(PostgreSQLOrdinalEnumJdbcType.class)
    @Column(nullable = false)
    private PaymentStatus status;

    @Column(name = "user_subscription_id")
    private Long userSubscription;
}
