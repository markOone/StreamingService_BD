-- 1) Select movies ordered by year
SELECT movie_id, title, year
FROM movie
ORDER BY year;

-- 2) Filter subscription plans with price less than or equal to 9.99
SELECT subscription_plan_id, name, price
FROM subscription_plan
WHERE price <= 9.99
ORDER BY price;

-- 3) Select completed payments ordered by payment date
SELECT payment_id, paid_at, amount, status, user_subscription_id
FROM payment
WHERE status = 'COMPLETED'
ORDER BY paid_at DESC;
