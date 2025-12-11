package dev.studentpp1.streamingservice.payments.repository;

import dev.studentpp1.streamingservice.payments.dto.HistoryPaymentResponse;
import dev.studentpp1.streamingservice.payments.dto.MonthlyPlanStatisticProjection;
import dev.studentpp1.streamingservice.payments.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    @Query("""
            select new dev.studentpp1.streamingservice.payments.dto.HistoryPaymentResponse(
                p.status,
                p.paidAt,
                p.amount,
                sp.name
            )
            from Payment p
            join p.userSubscription us
            join us.plan sp
            join us.user u
            where u.id = :userId
            """)
    List<HistoryPaymentResponse> getPaymentByUserId(Long userId);

    @Query("""
            select new dev.studentpp1.streamingservice.payments.dto.HistoryPaymentResponse(
                p.status,
                p.paidAt,
                p.amount,
                sp.name
            )
            from Payment p
            join p.userSubscription us
            join us.plan sp
            where us.id = :userSubscriptionId
            """)
    List<HistoryPaymentResponse> getPaymentByUserSubscription(Long userSubscriptionId);

    @Modifying
    @Transactional
    @Query("delete from Payment p where p.paidAt < :dateTime")
    int deletePaymentsBefore(LocalDateTime dateTime);

    // nativeQuery -> because it's complex query
    // interface projection -> to simplify mapping be using getters
    @Query(value = """
                    WITH monthly_statistic AS (
                SELECT
                    DATE_TRUNC('month', p.paid_at)::date AS current_month,
                    sp.name                             AS plan_name,
                    COUNT(DISTINCT us.user_id)          AS unique_users,
                    COUNT(p.payment_id)                 AS payment_count,
                    SUM(p.amount)                       AS total_plan_amount
                FROM payment p
                JOIN user_subscription us
                    ON p.user_subscription_id = us.user_subscription_id
                JOIN subscription_plan sp
                    ON us.subscription_plan_id = sp.subscription_plan_id
                WHERE p.status = 'COMPLETED'
                GROUP BY current_month, plan_name
            )
            SELECT
                ms.current_month,
                ms.plan_name,
                ms.unique_users,
                ms.payment_count,
                ms.total_plan_amount,
                SUM(ms.total_plan_amount) OVER (PARTITION BY ms.current_month) AS month_sum,
                ROUND(
                    ms.total_plan_amount / SUM(ms.total_plan_amount) OVER (PARTITION BY ms.current_month) * 100,
                    2
                ) AS percent_in_total_sum
            FROM monthly_statistic ms
            ORDER BY ms.current_month DESC, ms.total_plan_amount DESC;
            """,
            nativeQuery = true
    )
    List<MonthlyPlanStatisticProjection> findMonthlyPlanStatistics();
}
