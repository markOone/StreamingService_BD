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

SELECT
    sp.name AS plan_name,
    COUNT(us.user_subscription_id) AS active_subscribers
FROM subscription_plan sp
	LEFT JOIN user_subscription us ON sp.subscription_plan_id = us.subscription_plan_id AND us.status = 'ACTIVE' 
GROUP BY sp.subscription_plan_id, sp.name
HAVING COUNT(us.user_subscription_id) < 2
ORDER BY active_subscribers DESC, plan_name;

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

SELECT
    m.title,
    COUNT(im.subscription_plan_id) AS plan_count
FROM movie m
	JOIN included_movie im ON m.movie_id = im.movie_id
GROUP BY m.movie_id, m.title
HAVING COUNT(im.subscription_plan_id) <= 1
ORDER BY plan_count, m.title;