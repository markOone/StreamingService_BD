SELECT sp.name, m.title
FROM public.subscription_plan AS sp
INNER JOIN included_movie USING (subscription_plan_id)
INNER JOIN movie AS m USING (movie_id);

SELECT a.name, a.surname, p.character_name
FROM performance AS p
RIGHT JOIN actor AS a ON a.actor_id = p.actor_id;

SELECT m.title, d.name, d.surname
FROM movie AS m
FULL JOIN director AS d USING (director_id);

SELECT a.name, a.surname, m.title AS movie_title, p.character_name
FROM actor AS a
LEFT JOIN performance AS p USING (actor_id)
LEFT JOIN movie AS m USING (movie_id);

SELECT u.name, u.surname, sp.name AS plan_name, p.amount
FROM user_subscription AS us
RIGHT JOIN "user" AS u USING (user_id)
LEFT JOIN subscription_plan AS sp USING (subscription_plan_id)
LEFT JOIN payment AS p USING (user_subscription_id);