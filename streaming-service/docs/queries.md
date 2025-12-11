# Запит 1: Щомісячний дохід за планами підписок

## Бізнес-питання
+ Які підписки найкраще продаються?
+ Які підписки слід змінити чи видалити, бо нерентабельні

## SQL-запит
```sql
WITH monthly_statistic AS (SELECT DATE_TRUNC('month', p.paid_at)::date AS current_month,
                               sp.name                              AS plan_name,
                                  COUNT(DISTINCT us.user_id)           AS unique_users,
                                  COUNT(p.payment_id)                  AS payment_count,
                                  SUM(p.amount)                        AS total_plan_amount
                           FROM payment p
                                    JOIN user_subscription us
                                         ON p.user_subscription_id = us.user_subscription_id
                                    JOIN subscription_plan sp
                                         ON us.subscription_plan_id = sp.subscription_plan_id
                           WHERE p.status = 'COMPLETED'
                           GROUP BY current_month, plan_name)
SELECT ms.current_month,
       ms.plan_name,
       ms.unique_users,
       ms.payment_count,
       ms.total_plan_amount,
       SUM(ms.total_plan_amount) OVER (PARTITION BY ms.current_month)
           AS month_sum,
    ROUND(
            ms.total_plan_amount / SUM(ms.total_plan_amount)
                OVER (PARTITION BY ms.current_month) * 100, 2
    )   AS percent_in_total_sum
FROM monthly_statistic ms
ORDER BY ms.current_month DESC, ms.total_plan_amount DESC;
```

## Пояснення
- JOIN таблиць payment, user_subscription, subscription_plan
- Групування за місяцем та ім'ям плану
- Обчислення загального доходу для плану та загальний за місяць
- Фільтрація лише завершених оплат
- Сортування результатів хронологічно та за зростанням вартості

## Приклад виводу:

| current\_month | plan\_name | unique\_users | payment\_count | total\_plan\_amount | month\_sum | percent\_in\_total\_sum |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| 2025-12-01 | STANDARD | 2 | 2 | 240 | 340 | 70.59 |
| 2025-12-01 | BASIC | 1 | 1 | 100 | 340 | 29.41 |
