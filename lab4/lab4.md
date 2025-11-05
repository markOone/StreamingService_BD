# Lab 4

## JOIN queries x5

```sql
SELECT sp.name, m.title FROM public.subscription_plan as sp
INNER JOIN included_movie as im ON sp.subscription_plan_id = im.subscription_plan_id
INNER JOIN movie as m ON m.movie_id = im.movie_id

SELECT a.name, a.surname, p.character_name FROM performance as p
RIGHT JOIN actor as a ON a.actor_id = p.actor_id

SELECT m.title, d.name, d.surname from movie as m 
FULL JOIN director as d on m.director_id = d.director_id

SELECT a.name, a.surname, m.title FROM performance as p
LEFT JOIN actor as a ON p.actor_id = a.actor_id 
FULL JOIN movie as m ON p.movie_id = m.movie_id

SELECT u.name, u.surname, sp.name, p.amount FROM user_subscription as us
RIGHT JOIN "user" as u ON us.user_id = u.user_id
LEFT JOIN subscription_plan as sp ON us.subscription_plan_id = sp.subscription_plan_id
LEFT JOIN payment as p ON p.user_subscription_id = us.user_subscription_id;
```

## Base aggregation x5

```sql
SELECT u.name, u.surname, SUM(p.amount) FROM user_subscription as us
INNER JOIN payment as p ON p.user_subscription_id = us.user_subscription_id
INNER JOIN "user" as u ON us.user_id = u.user_id
GROUP BY u.user_id

SELECT sp.name, COUNT(*) FROM public.subscription_plan as sp
INNER JOIN included_movie as im ON sp.subscription_plan_id = im.subscription_plan_id
GROUP BY sp.name

SELECT AVG(sp.price) FROM public.subscription_plan as sp

SELECT MIN(sp.price) FROM public.subscription_plan as sp

SELECT MAX(p.amount) FROM public.payment as p
```

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