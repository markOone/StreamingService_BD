SELECT
    sp.name AS plan_name,
    SUM(p.amount) AS total_revenue
FROM subscription_plan sp
	JOIN user_subscription us USING(subscription_plan_id)
	JOIN payment p USING(user_subscription_id)
WHERE p.status = 'COMPLETED'
GROUP BY sp.subscription_plan_id, sp.name
ORDER BY total_revenue DESC, plan_name;

SELECT
    d.name,
    d.surname,
    COUNT(m.movie_id) AS movie_count,
    ROUND(AVG(m.rating), 2) AS average_rating
FROM director d
	JOIN movie m USING(director_id)
GROUP BY d.director_id, d.name, d.surname
ORDER BY average_rating DESC, d.name;

SELECT
    sp.name AS plan_name,
    sp.price,
    COUNT(im.movie_id) AS movie_count
FROM subscription_plan sp
	LEFT JOIN included_movie im USING(subscription_plan_id)
GROUP BY sp.subscription_plan_id, sp.name, sp.price
ORDER BY movie_count DESC, plan_name;

SELECT
    sp.name AS plan_name,
    COALESCE(SUM(p.amount), 0.00) AS total_refund_amount
FROM subscription_plan sp
	LEFT JOIN user_subscription us USING(subscription_plan_id)
	LEFT JOIN payment p ON us.user_subscription_id = p.user_subscription_id AND p.status = 'REFUNDED'
GROUP BY sp.subscription_plan_id, sp.name
ORDER BY total_refund_amount DESC, sp.name;

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