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

## Groups filtering x5

Фільтрує користувачів, у яких кількість невдалих платежів дорівнює 1 або більше.
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
Фільтрує акторів, які зіграли одного й того ж персонажа у більш ніж одному фільмі.
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
Фільтрує плани підписки, які мають менше двох активних користувачів.
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
Фільтрує користувачів, які в сумі витратили більше 100 помідорів на підписки
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
Фільтрує фільми, які включені в не більше ніж один план підписки
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

## Multi-table aggregation x5

Визначає загальний дохід, отриманий від кожного плану підписки.
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
Обчислює кількість знятих фільмів та середній рейтинг для кожного режисера.
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
Обчислює кількість фільмів, включених до кожного плану підписки.
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
Обчислює загальну суму повернутих коштів (refunds) для кожного плану підписки.
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
Знаходить найкращих 5 акторів за середнім рейтингом фільмів, у яких вони брали участь
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
