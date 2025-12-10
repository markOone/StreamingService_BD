package dev.studentpp1.streamingservice.payments.repository;

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
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class PaymentRepositoryTest extends AbstractPostgresContainerTest {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SubscriptionPlanRepository subscriptionPlanRepository;

    @Autowired
    private UserSubscriptionRepository userSubscriptionRepository;

    @BeforeAll
    void setUp() {
        paymentRepository.deleteAll();
        userSubscriptionRepository.deleteAll();
        subscriptionPlanRepository.deleteAll();
        userRepository.deleteAll();

        // ---------- USERS ----------
        AppUser user1 = AppUser.builder()
                .name("John")
                .surname("Doe")
                .email("user1@example.com")
                .password("password1")
                .birthday(LocalDate.of(1990, 1, 1))
                .role(Role.ROLE_USER)
                .deleted(false)
                .build();
        user1 = userRepository.save(user1);

        AppUser user2 = AppUser.builder()
                .name("Jane")
                .surname("Smith")
                .email("user2@example.com")
                .password("password2")
                .birthday(LocalDate.of(1995, 2, 2))
                .role(Role.ROLE_USER)
                .deleted(false)
                .build();
        user2 = userRepository.save(user2);

        AppUser user3 = AppUser.builder()
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
        SubscriptionPlan basic = SubscriptionPlan.builder()
                .name("BASIC")
                .description("Basic plan")
                .price(new BigDecimal("100.00"))
                .duration(30)
                .build();
        basic = subscriptionPlanRepository.save(basic);

        SubscriptionPlan standard = SubscriptionPlan.builder()
                .name("STANDARD")
                .description("Standard plan")
                .price(new BigDecimal("150.00"))
                .duration(30)
                .build();
        standard = subscriptionPlanRepository.save(standard);

        SubscriptionPlan premium = SubscriptionPlan.builder()
                .name("PREMIUM")
                .description("Premium plan")
                .price(new BigDecimal("200.00"))
                .duration(30)
                .build();
        premium = subscriptionPlanRepository.save(premium);

        // ---------- USER SUBSCRIPTIONS ----------
        UserSubscription usUser1Basic = UserSubscription.builder()
                .user(user1)
                .plan(basic)
                .startTime(LocalDateTime.of(2025, 1, 1, 0, 0))
                .endTime(LocalDateTime.of(2025, 12, 31, 0, 0))
                .status(SubscriptionStatus.ACTIVE)
                .build();
        usUser1Basic = userSubscriptionRepository.save(usUser1Basic);

        UserSubscription usUser2Standard = UserSubscription.builder()
                .user(user2)
                .plan(standard)
                .startTime(LocalDateTime.of(2025, 1, 1, 0, 0))
                .endTime(LocalDateTime.of(2025, 12, 31, 0, 0))
                .status(SubscriptionStatus.ACTIVE)
                .build();
        usUser2Standard = userSubscriptionRepository.save(usUser2Standard);

        UserSubscription usUser3Premium = UserSubscription.builder()
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
                .amount(100)
                .build());

        paymentRepository.save(Payment.builder()
                .userSubscription(usUser2Standard)
                .status(PaymentStatus.COMPLETED)
                .paidAt(LocalDateTime.of(2025, 12, 6, 11, 0))
                .amount(150)
                .build());

        paymentRepository.save(Payment.builder()
                .userSubscription(usUser3Premium)
                .status(PaymentStatus.COMPLETED)
                .paidAt(LocalDateTime.of(2025, 12, 7, 12, 0))
                .amount(200)
                .build());

        // ---------- PAYMENTS: 2025-11 ----------
        paymentRepository.save(Payment.builder()
                .userSubscription(usUser2Standard)
                .status(PaymentStatus.COMPLETED)
                .paidAt(LocalDateTime.of(2025, 11, 10, 10, 0))
                .amount(150)
                .build());

        paymentRepository.save(Payment.builder()
                .userSubscription(usUser3Premium)
                .status(PaymentStatus.COMPLETED)
                .paidAt(LocalDateTime.of(2025, 11, 12, 11, 0))
                .amount(200)
                .build());

        paymentRepository.save(Payment.builder()
                .userSubscription(usUser3Premium)
                .status(PaymentStatus.FAILED)
                .paidAt(LocalDateTime.of(2025, 11, 13, 12, 0))
                .amount(200)
                .build());
    }

    @Test
    void getPaymentByUserId_returnsAllPaymentsForUser() {
        List<HistoryPaymentResponse> historyPayments = paymentRepository.getPaymentByUserId(2L);
        assertThat(historyPayments).size().isEqualTo(1);
        HistoryPaymentResponse paymentResponse = historyPayments.getFirst();
        assertThat(paymentResponse.status()).isEqualTo(PaymentStatus.COMPLETED);
        assertThat(paymentResponse.paidAt()).isEqualTo(LocalDateTime.of(2025, 12, 5, 10, 0));
        assertThat(paymentResponse.amount()).isEqualTo(100);
        assertThat(paymentResponse.subscriptionName()).isEqualTo("BASIC");
    }

    @Test
    void getPaymentByUserSubscription_returnsAllPaymentsForUserSubscription() {
        List<HistoryPaymentResponse> historyPayments = paymentRepository.getPaymentByUserSubscription(2L);
        assertThat(historyPayments).size().isEqualTo(2);
        HistoryPaymentResponse paymentResponse1 = historyPayments.getFirst();
        assertThat(paymentResponse1.status()).isEqualTo(PaymentStatus.COMPLETED);
        assertThat(paymentResponse1.paidAt()).isEqualTo(LocalDateTime.of(2025, 12, 6, 11, 0));
        assertThat(paymentResponse1.amount()).isEqualTo(150);
        assertThat(paymentResponse1.subscriptionName()).isEqualTo("STANDARD");

        HistoryPaymentResponse paymentResponse2 = historyPayments.getLast();
        assertThat(paymentResponse2.status()).isEqualTo(PaymentStatus.COMPLETED);
        assertThat(paymentResponse2.paidAt()).isEqualTo(LocalDateTime.of(2025, 11, 10, 10, 0));
        assertThat(paymentResponse2.amount()).isEqualTo(150);
        assertThat(paymentResponse2.subscriptionName()).isEqualTo("STANDARD");
    }

    @Test
    void findMonthlyPlanStatistics_returnsCorrectAggregatedMonthlyData() {
        List<MonthlyPlanStatisticProjection> stats = paymentRepository.findMonthlyPlanStatistics();
        assertThat(stats).hasSize(5);

        MonthlyPlanStatisticProjection decPremium = stats.get(0);
        assertThat(decPremium.getCurrentMonth()).isEqualTo(LocalDate.of(2025, 12, 1));
        assertThat(decPremium.getPlanName()).isEqualTo("PREMIUM");
        assertThat(decPremium.getUniqueUsers()).isEqualTo(1L);
        assertThat(decPremium.getPaymentCount()).isEqualTo(1L);
        assertThat(decPremium.getTotalPlanAmount()).isEqualByComparingTo("200.00");
        assertThat(decPremium.getMonthSum()).isEqualByComparingTo("450.00");
        assertThat(decPremium.getPercentInTotalSum()).isEqualByComparingTo("44.44");

        MonthlyPlanStatisticProjection decStandard = stats.get(1);
        assertThat(decStandard.getCurrentMonth()).isEqualTo(LocalDate.of(2025, 12, 1));
        assertThat(decStandard.getPlanName()).isEqualTo("STANDARD");
        assertThat(decStandard.getUniqueUsers()).isEqualTo(1L);
        assertThat(decStandard.getPaymentCount()).isEqualTo(1L);
        assertThat(decStandard.getTotalPlanAmount()).isEqualByComparingTo("150.00");
        assertThat(decStandard.getMonthSum()).isEqualByComparingTo("450.00");
        assertThat(decStandard.getPercentInTotalSum()).isEqualByComparingTo("33.33");

        MonthlyPlanStatisticProjection decBasic = stats.get(2);
        assertThat(decBasic.getCurrentMonth()).isEqualTo(LocalDate.of(2025, 12, 1));
        assertThat(decBasic.getPlanName()).isEqualTo("BASIC");
        assertThat(decBasic.getUniqueUsers()).isEqualTo(1L);
        assertThat(decBasic.getPaymentCount()).isEqualTo(1L);
        assertThat(decBasic.getTotalPlanAmount()).isEqualByComparingTo("100.00");
        assertThat(decBasic.getMonthSum()).isEqualByComparingTo("450.00");
        assertThat(decBasic.getPercentInTotalSum()).isEqualByComparingTo("22.22");

        MonthlyPlanStatisticProjection novPremium = stats.get(3);
        assertThat(novPremium.getCurrentMonth()).isEqualTo(LocalDate.of(2025, 11, 1));
        assertThat(novPremium.getPlanName()).isEqualTo("PREMIUM");
        assertThat(novPremium.getUniqueUsers()).isEqualTo(1L);
        assertThat(novPremium.getPaymentCount()).isEqualTo(1L);
        assertThat(novPremium.getTotalPlanAmount()).isEqualByComparingTo("200.00");
        assertThat(novPremium.getMonthSum()).isEqualByComparingTo("350.00");
        assertThat(novPremium.getPercentInTotalSum()).isEqualByComparingTo("57.14");

        MonthlyPlanStatisticProjection novStandard = stats.get(4);
        assertThat(novStandard.getCurrentMonth()).isEqualTo(LocalDate.of(2025, 11, 1));
        assertThat(novStandard.getPlanName()).isEqualTo("STANDARD");
        assertThat(novStandard.getUniqueUsers()).isEqualTo(1L);
        assertThat(novStandard.getPaymentCount()).isEqualTo(1L);
        assertThat(novStandard.getTotalPlanAmount()).isEqualByComparingTo("150.00");
        assertThat(novStandard.getMonthSum()).isEqualByComparingTo("350.00");
        assertThat(novStandard.getPercentInTotalSum()).isEqualByComparingTo("42.86");
    }

    @Test
    void deletePaymentsBefore_removesOnlyOlderPayments() {
        LocalDateTime cutoff = LocalDateTime.of(2025, 12, 1, 0, 0);
        int deletedCount = paymentRepository.deletePaymentsBefore(cutoff);
        assertThat(deletedCount).isEqualTo(3);
        List<Payment> remaining = paymentRepository.findAll();
        assertThat(remaining).hasSize(3);
        assertThat(remaining)
                .allSatisfy(p ->
                        assertThat(p.getPaidAt()).isAfterOrEqualTo(cutoff)
                );
    }
}