# Lab 4

## JOIN queries

* Select subscription plans with their included films
  – use `INNER JOIN` to return only those `subscription plans` that actually contain `movies`

```sql
SELECT sp.name, m.title
FROM public.subscription_plan AS sp
INNER JOIN included_movie USING (subscription_plan_id)
INNER JOIN movie AS m USING (movie_id);
```

* Select all actors with their roles
  – use `RIGHT JOIN` to include all `actors` even if they have no `roles`

```sql
SELECT a.name, a.surname, p.character_name
FROM performance AS p
RIGHT JOIN actor AS a ON a.actor_id = p.actor_id;
```

* Select movies and their directors
  – use `FULL JOIN` to display both `movies` and `directors` (including unmatched rows)

```sql
SELECT m.title, d.name, d.surname
FROM movie AS m
FULL JOIN director AS d USING (director_id);
```

* Select all actors with starred movies
  – use `LEFT JOIN` to include all existing actors

```sql
SELECT a.name, a.surname, m.title AS movie_title, p.character_name
FROM actor AS a
LEFT JOIN performance AS p USING (actor_id)
LEFT JOIN movie AS m USING (movie_id);
```

* Select payment history for each user
  – using `RIGHT JOIN` with `user` and `LEFT JOIN` to related tables ensures users without subscriptions or payments are still shown

```sql
SELECT u.name, u.surname, sp.name AS plan_name, p.amount
FROM user_subscription AS us
RIGHT JOIN "user" AS u USING (user_id)
LEFT JOIN subscription_plan AS sp USING (subscription_plan_id)
LEFT JOIN payment AS p USING (user_subscription_id);
```

## Base aggregation

* Select total sum of payments for each user

```sql
SELECT u.user_id, u.name, u.surname, SUM(p.amount) AS total_amount
FROM user_subscription AS us
INNER JOIN payment AS p USING (user_subscription_id)
INNER JOIN "user" AS u USING (user_id)
GROUP BY u.user_id, u.name, u.surname;
```

* Count how many films are included in each subscription plan

```sql
SELECT sp.subscription_plan_id, sp.name, COUNT(*) AS film_count
FROM public.subscription_plan AS sp
INNER JOIN included_movie AS im USING (subscription_plan_id)
GROUP BY sp.subscription_plan_id, sp.name;
```

* Get average subscription plan price

```sql
SELECT AVG(sp.price) AS avg_price
FROM public.subscription_plan AS sp;
```

* Get the cheapest subscription plan price

```sql
SELECT MIN(sp.price) AS cheapest_price
FROM public.subscription_plan AS sp;
```

* Get the average rating of films in which each actor starred

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

## Grouping x5

```sql
SELECT year, COUNT(movie_id) AS movies_per_year
FROM movie
GROUP BY year
ORDER BY year DESC;

SELECT duration, MIN(price) AS min_price, MAX(price) AS max_price, AVG(price) AS avg_price
FROM subscription_plan
GROUP BY duration
ORDER BY duration;

SELECT FLOOR(rating) AS rating_group, COUNT(movie_id) AS num_movies, AVG(rating) AS average_rating_in_group
FROM movie
WHERE rating IS NOT NULL
GROUP BY rating_group
ORDER BY rating_group DESC;

SELECT status, COUNT(payment_id) AS payments_count
FROM payment
GROUP BY status;

SELECT actor_id, COUNT(performance_id) AS roles_count
FROM performance
GROUP BY actor_id
ORDER BY roles_count DESC;
```
