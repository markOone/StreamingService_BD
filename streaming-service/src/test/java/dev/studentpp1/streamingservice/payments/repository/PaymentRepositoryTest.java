package dev.studentpp1.streamingservice.payments.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.annotation.DirtiesContext;

import dev.studentpp1.streamingservice.AbstractPostgresContainerTest;
import dev.studentpp1.streamingservice.auth.persistence.Role;
import dev.studentpp1.streamingservice.payments.dto.HistoryPaymentResponse;
import dev.studentpp1.streamingservice.payments.dto.MonthlyPlanStatisticProjection;
import dev.studentpp1.streamingservice.payments.entity.Payment;
import dev.studentpp1.streamingservice.payments.entity.PaymentStatus;
import dev.studentpp1.streamingservice.subscription.entity.SubscriptionPlan;
import dev.studentpp1.streamingservice.subscription.entity.SubscriptionStatus;
import dev.studentpp1.streamingservice.subscription.entity.UserSubscription;
import dev.studentpp1.streamingservice.subscription.repository.SubscriptionPlanRepository;
import dev.studentpp1.streamingservice.subscription.repository.UserSubscriptionRepository;
import dev.studentpp1.streamingservice.users.entity.AppUser;
import dev.studentpp1.streamingservice.users.repository.UserRepository;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@Testcontainers
@DataJpaTest
class PaymentRepositoryTest {

    private static final DockerImageName POSTGRES_IMAGE =
            DockerImageName.parse("postgres:16-alpine");

    @Container
    @ServiceConnection
    protected static final PostgreSQLContainer<?> POSTGRES =
            new PostgreSQLContainer<>(POSTGRES_IMAGE)
                    .withDatabaseName("streaming_service_test_db")
                    .withUsername("test")
                    .withPassword("test");

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SubscriptionPlanRepository subscriptionPlanRepository;

    @Autowired
    private UserSubscriptionRepository userSubscriptionRepository;

    private AppUser user1;
    private AppUser user2;
    private AppUser user3;
    private UserSubscription usUser1Basic;
    private UserSubscription usUser2Standard;
    private UserSubscription usUser3Premium;

    @BeforeEach
    void setUp() {
        paymentRepository.deleteAll();
        userSubscriptionRepository.deleteAll();
        userRepository.deleteAll();

        // ---------- USERS ----------
        user1 = AppUser.builder()
                .name("John")
                .surname("Doe")
                .email("user1@example.com")
                .password("password1")
                .birthday(LocalDate.of(1990, 1, 1))
                .role(Role.ROLE_USER)
                .deleted(false)
                .build();
        user1 = userRepository.save(user1);

        user2 = AppUser.builder()
                .name("Jane")
                .surname("Smith")
                .email("user2@example.com")
                .password("password2")
                .birthday(LocalDate.of(1995, 2, 2))
                .role(Role.ROLE_USER)
                .deleted(false)
                .build();
        user2 = userRepository.save(user2);

        user3 = AppUser.builder()
                .name("Alice")
                .surname("Brown")
                .email("user3@example.com")
                .password("password3")
                .birthday(LocalDate.of(1998, 3, 3))
                .role(Role.ROLE_USER)
                .deleted(false)
                .build();
        user3 = userRepository.save(user3);

        // ---------- PLANS ----------
        // Use existing plans from Flyway migration instead of creating new ones
        SubscriptionPlan basic = subscriptionPlanRepository.findByName("BASIC")
                .orElseThrow(() -> new IllegalStateException("BASIC plan not found"));

        SubscriptionPlan standard = subscriptionPlanRepository.findByName("STANDARD")
                .orElseThrow(() -> new IllegalStateException("STANDARD plan not found"));

        SubscriptionPlan premium = subscriptionPlanRepository.findByName("PREMIUM")
                .orElseThrow(() -> new IllegalStateException("PREMIUM plan not found"));

        // ---------- USER SUBSCRIPTIONS ----------
        usUser1Basic = UserSubscription.builder()
                .user(user1)
                .plan(basic)
                .startTime(LocalDateTime.of(2025, 1, 1, 0, 0))
                .endTime(LocalDateTime.of(2025, 12, 31, 0, 0))
                .status(SubscriptionStatus.ACTIVE)
                .build();
        usUser1Basic = userSubscriptionRepository.save(usUser1Basic);

        usUser2Standard = UserSubscription.builder()
                .user(user2)
                .plan(standard)
                .startTime(LocalDateTime.of(2025, 1, 1, 0, 0))
                .endTime(LocalDateTime.of(2025, 12, 31, 0, 0))
                .status(SubscriptionStatus.ACTIVE)
                .build();
        usUser2Standard = userSubscriptionRepository.save(usUser2Standard);

        usUser3Premium = UserSubscription.builder()
                .user(user3)
                .plan(premium)
                .startTime(LocalDateTime.of(2025, 1, 1, 0, 0))
                .endTime(LocalDateTime.of(2025, 12, 31, 0, 0))
                .status(SubscriptionStatus.ACTIVE)
                .build();
        usUser3Premium = userSubscriptionRepository.save(usUser3Premium);

        // ---------- PAYMENTS: 2025-12 ----------
        paymentRepository.save(Payment.builder()
                .userSubscription(usUser1Basic)
                .status(PaymentStatus.COMPLETED)
                .paidAt(LocalDateTime.of(2025, 12, 5, 10, 0))
                .amount(BigDecimal.valueOf(100))
                .build());

        paymentRepository.save(Payment.builder()
                .userSubscription(usUser2Standard)
                .status(PaymentStatus.COMPLETED)
                .paidAt(LocalDateTime.of(2025, 12, 6, 11, 0))
                .amount(BigDecimal.valueOf(150))
                .build());

        paymentRepository.save(Payment.builder()
                .userSubscription(usUser3Premium)
                .status(PaymentStatus.COMPLETED)
                .paidAt(LocalDateTime.of(2025, 12, 7, 12, 0))
                .amount(BigDecimal.valueOf(200))
                .build());

        // ---------- PAYMENTS: 2025-11 ----------
        paymentRepository.save(Payment.builder()
                .userSubscription(usUser2Standard)
                .status(PaymentStatus.COMPLETED)
                .paidAt(LocalDateTime.of(2025, 11, 10, 10, 0))
                .amount(BigDecimal.valueOf(150))
                .build());

        paymentRepository.save(Payment.builder()
                .userSubscription(usUser3Premium)
                .status(PaymentStatus.COMPLETED)
                .paidAt(LocalDateTime.of(2025, 11, 12, 11, 0))
                .amount(BigDecimal.valueOf(200))
                .build());

        paymentRepository.save(Payment.builder()
                .userSubscription(usUser3Premium)
                .status(PaymentStatus.FAILED)
                .paidAt(LocalDateTime.of(2025, 11, 13, 12, 0))
                .amount(BigDecimal.valueOf(200))
                .build());
    }

    @Test
    void getPaymentByUserId_returnsCompletedPaymentsOnly() {
        List<HistoryPaymentResponse> history = paymentRepository.getPaymentByUserId(user1.getId());
        assertThat(history).hasSize(1);

        HistoryPaymentResponse payment = history.getFirst();
        assertThat(payment.status()).isEqualTo(PaymentStatus.COMPLETED);
        assertThat(payment.amount()).isEqualByComparingTo("100.00");
        assertThat(payment.subscriptionName()).isEqualTo("BASIC");
    }

    @Test
    void getPaymentByUserSubscription_returnsOrderedPayments() {
        List<HistoryPaymentResponse> history = paymentRepository.getPaymentByUserSubscription(usUser2Standard.getId());
        assertThat(history).hasSize(2);
    }

    @Test
    void findMonthlyPlanStatistics_returnsCorrectData() {
        List<MonthlyPlanStatisticProjection> stats = paymentRepository.findMonthlyPlanStatistics();
        assertThat(stats).hasSize(5);
    }

    @Test
    void deletePaymentsBefore_removesOnlyOlderPayments() {
        LocalDateTime cutoff = LocalDateTime.of(2025, 12, 1, 0, 0);
        int deleted = paymentRepository.deletePaymentsBefore(cutoff);

        assertThat(deleted).isEqualTo(3);
        assertThat(paymentRepository.findAll()).hasSize(3);
    }

    @Test
    void deleteByStatusAndCreatedAtBefore_deletesOnlyOldPendingPayments() {
        LocalDateTime threshold = LocalDateTime.of(2025, 12, 1, 0, 0);

        Payment stalePending = paymentRepository.save(Payment.builder()
                .status(PaymentStatus.PENDING)
                .createdAt(LocalDateTime.of(2025, 11, 1, 0, 0))
                .paidAt(null)
                .amount(new BigDecimal("10.00"))
                .providerSessionId("pi_stale")
                .userSubscription(null)
                .build());

        Payment freshPending = paymentRepository.save(Payment.builder()
                .status(PaymentStatus.PENDING)
                .createdAt(LocalDateTime.of(2025, 12, 5, 0, 0))
                .paidAt(null)
                .amount(new BigDecimal("11.00"))
                .providerSessionId("pi_fresh")
                .userSubscription(null)
                .build());

        paymentRepository.flush();

        int deleted = paymentRepository.deleteByStatusAndCreatedAtBefore(PaymentStatus.PENDING, threshold);

        paymentRepository.flush();

        assertThat(deleted).isEqualTo(1);
        assertThat(paymentRepository.findById(stalePending.getId())).isEmpty();
        assertThat(paymentRepository.findById(freshPending.getId())).isPresent();
    }
}
