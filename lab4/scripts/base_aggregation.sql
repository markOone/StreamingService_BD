
SELECT u.user_id, u.name, u.surname, SUM(p.amount) AS total_amount
FROM user_subscription AS us
INNER JOIN payment AS p USING (user_subscription_id)
INNER JOIN "user" AS u USING (user_id)
GROUP BY u.user_id, u.name, u.surname;

SELECT sp.subscription_plan_id, sp.name, COUNT(*) AS film_count
FROM public.subscription_plan AS sp
INNER JOIN included_movie AS im USING (subscription_plan_id)
GROUP BY sp.subscription_plan_id, sp.name;

SELECT AVG(sp.price) AS avg_price
FROM public.subscription_plan AS sp;

SELECT MIN(sp.price) AS cheapest_price
FROM public.subscription_plan AS sp;

SELECT 
  a.name, 
  a.surname, 
  ROUND(AVG(m.rating), 2) AS avg_rating
FROM actor AS a
LEFT JOIN performance AS p USING (actor_id)
LEFT JOIN movie AS m USING (movie_id)
GROUP BY a.actor_id, a.name, a.surname;