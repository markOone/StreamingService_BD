# Lab 4

## JOIN queries

1. Select subscription plans with their included films
  – use `INNER JOIN` to return only those `subscription plans` that actually contain `movies`

```sql
SELECT
    sp.name,
    m.title
FROM public.subscription_plan AS sp
    INNER JOIN included_movie USING (subscription_plan_id)
    INNER JOIN movie AS m USING (movie_id);
```

2. Select all actors with their roles
  – use `RIGHT JOIN` to include all `actors` even if they have no `roles`

```sql
SELECT
    a.name,
    a.surname,
    p.character_name
FROM performance AS p
    RIGHT JOIN actor AS a ON a.actor_id = p.actor_id;
```

3. Select movies and their directors
  – use `FULL JOIN` to display both `movies` and `directors` (including unmatched rows)

```sql
SELECT
    m.title,
    d.name,
    d.surname
FROM movie AS m
    FULL JOIN director AS d USING (director_id);
```

4. Select all actors with starred movies
  – use `LEFT JOIN` to include all existing actors

```sql
SELECT
    a.name,
    a.surname,
    m.title AS movie_title,
    p.character_name
FROM actor AS a
    LEFT JOIN performance AS p USING (actor_id)
    LEFT JOIN movie AS m USING (movie_id);
```

5. Select payment history for each user
  – using `RIGHT JOIN` with `user` and `LEFT JOIN` to related tables ensures users without subscriptions or payments are still shown

```sql
SELECT
    u.name,
    u.surname,
    sp.name AS plan_name,
    p.amount
FROM user_subscription AS us
    RIGHT JOIN "user" AS u USING (user_id)
    LEFT JOIN subscription_plan AS sp USING (subscription_plan_id)
    LEFT JOIN payment AS p USING (user_subscription_id);
```
---

## Base aggregation

1. Select total sum of payments for each user

```sql
SELECT
    u.user_id,
    u.name,
    u.surname,
    SUM(p.amount) AS total_amount
FROM user_subscription AS us
    INNER JOIN payment AS p USING (user_subscription_id)
    RIGHT JOIN "user" AS u USING (user_id)
GROUP BY u.user_id, u.name, u.surname;
```

2. Count how many films are included in each subscription plan

```sql
SELECT
    sp.subscription_plan_id,
    sp.name,
    COUNT(*) AS film_count
FROM public.subscription_plan AS sp
    INNER JOIN included_movie AS im USING (subscription_plan_id)
GROUP BY sp.subscription_plan_id, sp.name;
```

3. Get average subscription plan price

```sql
SELECT AVG(sp.price) AS avg_price
FROM public.subscription_plan AS sp;
```

4. Get the cheapest subscription plan price

```sql
SELECT MIN(sp.price) AS cheapest_price
FROM public.subscription_plan AS sp;
```

5. Get the average rating of films in which each actor starred

```sql
SELECT 
    a.name, 
    a.surname, 
    ROUND(AVG(m.rating), 2) AS avg_rating
FROM actor AS a
    LEFT JOIN performance AS p USING (actor_id)
    LEFT JOIN movie AS m USING (movie_id)
GROUP BY a.actor_id, a.name, a.surname;
```
---

## Grouping

1.

```sql
SELECT
    year,
    COUNT(movie_id) AS movies_per_year
FROM movie
GROUP BY year
ORDER BY year DESC;
```

2.

```sql
SELECT
    duration,
    MIN(price) AS min_price,
    MAX(price) AS max_price,
    AVG(price) AS avg_price
FROM subscription_plan
GROUP BY duration
ORDER BY duration;
```

3.

```sql
SELECT
    FLOOR(rating) AS rating_group,
    COUNT(movie_id) AS num_movies,
    AVG(rating) AS average_rating_in_group
FROM movie
WHERE rating IS NOT NULL
GROUP BY rating_group
ORDER BY rating_group DESC;
```

4.

```sql
SELECT
    status,
    COUNT(payment_id) AS payments_count
FROM payment
GROUP BY status;
```

5.

```sql
SELECT
    actor_id,
    COUNT(performance_id) AS roles_count
FROM performance
GROUP BY actor_id
ORDER BY roles_count DESC;
```
---

## Groups filtering x5

1. Filters users who have 1 or more failed payments.

```sql
SELECT
    u.name,
    u.surname,
    u.email,
    COUNT(p.payment_id) AS failed_payment_count
FROM "user" u
	JOIN user_subscription us USING(user_id)
	JOIN payment p USING(user_subscription_id)
WHERE p.status = 'FAILED'
GROUP BY u.user_id, u.name, u.surname, u.email
HAVING COUNT(p.payment_id) >= 1
ORDER BY failed_payment_count DESC;
```

2. Filters actors who have played the same character in more than one film.

```sql
SELECT
    a.name,
    a.surname,
    p.character_name,
    COUNT(p.movie_id) AS movie_appearances
FROM actor a
	JOIN performance p USING(actor_id)
GROUP BY a.actor_id, a.name, a.surname, p.character_name
HAVING COUNT(p.movie_id) > 1
ORDER BY movie_appearances DESC;
```

3. Filters subscription plans that have fewer than two active users.

```sql
SELECT
    sp.name AS plan_name,
    COUNT(us.user_subscription_id) AS active_subscribers
FROM subscription_plan sp
	LEFT JOIN user_subscription us ON sp.subscription_plan_id = us.subscription_plan_id AND us.status = 'ACTIVE' 
GROUP BY sp.subscription_plan_id, sp.name
HAVING COUNT(us.user_subscription_id) < 2
ORDER BY active_subscribers DESC, plan_name;
```

4. Filters users who have spent more than 100 tomatoes on subscriptions.

```sql
SELECT
    u.name,
    u.surname,
    SUM(p.amount) AS total_spent
FROM "user" u
	JOIN user_subscription us USING(user_id)
	JOIN payment p USING(user_subscription_id)
WHERE p.status = 'COMPLETED'
GROUP BY u.user_id, u.name, u.surname
HAVING SUM(p.amount) > 100.00
ORDER BY total_spent DESC, u.name;
```

5. Filters films that are included in no more than one subscription plan.

```sql
SELECT
    m.title,
    COUNT(im.subscription_plan_id) AS plan_count
FROM movie m
	JOIN included_movie im ON m.movie_id = im.movie_id
GROUP BY m.movie_id, m.title
HAVING COUNT(im.subscription_plan_id) <= 1
ORDER BY plan_count, m.title;
```
---

## Multi-table aggregation x5

1. Calculates the total revenue generated from each subscription plan.

```sql
SELECT
    sp.name AS plan_name,
    SUM(p.amount) AS total_revenue
FROM subscription_plan sp
	JOIN user_subscription us USING(subscription_plan_id)
	JOIN payment p USING(user_subscription_id)
WHERE p.status = 'COMPLETED'
GROUP BY sp.subscription_plan_id, sp.name
ORDER BY total_revenue DESC, plan_name;
```

2. Calculates the number of films made and the average rating for each director.

```sql
SELECT
    d.name,
    d.surname,
    COUNT(m.movie_id) AS movie_count,
    ROUND(AVG(m.rating), 2) AS average_rating
FROM director d
	JOIN movie m USING(director_id)
GROUP BY d.director_id, d.name, d.surname
ORDER BY average_rating DESC, d.name;
```

3. Calculates the number of films included in each subscription plan.

```sql
SELECT
    sp.name AS plan_name,
    sp.price,
    COUNT(im.movie_id) AS movie_count
FROM subscription_plan sp
	LEFT JOIN included_movie im USING(subscription_plan_id)
GROUP BY sp.subscription_plan_id, sp.name, sp.price
ORDER BY movie_count DESC, plan_name;
```

4. Calculates the total amount of refunds for each subscription plan.

```sql
SELECT
    sp.name AS plan_name,
    COALESCE(SUM(p.amount), 0.00) AS total_refund_amount
FROM subscription_plan sp
	LEFT JOIN user_subscription us USING(subscription_plan_id)
	LEFT JOIN payment p ON us.user_subscription_id = p.user_subscription_id AND p.status = 'REFUNDED'
GROUP BY sp.subscription_plan_id, sp.name
ORDER BY total_refund_amount DESC, sp.name;
```

5. Finds the top 5 actors by average rating of the films in which they performed.

```sql
SELECT
    a.name AS actor_name,
    a.surname AS actor_surname,
    ROUND(AVG(m.rating), 2) AS average_movie_rating,
    COUNT(DISTINCT m.movie_id) AS total_movies_count
FROM actor a
	JOIN performance p USING(actor_id)
	JOIN movie m USING(movie_id)
GROUP BY a.actor_id, a.name, a.surname
HAVING COUNT(DISTINCT m.movie_id) >= 1
ORDER BY average_movie_rating DESC, total_movies_count DESC, actor_surname
LIMIT 5;
```
---
