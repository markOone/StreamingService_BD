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
SELECT *
FROM payment
WHERE status = 'COMPLETED'
ORDER BY paid_at DESC;

-- 4) Select Movies of certain Actor("Christian Bale")
SELECT m.title, m.description, m.year, m.rating, a.name, a.surname
FROM movie AS m, performance AS p, actor AS a
WHERE m.movie_id = p.movie_id 
AND p.actor_id = a.actor_id 
AND a.name = 'Christian'
AND a.surname = 'Bale'
ORDER BY m.year ASC;

-- 5) Select movies of certain Director("Cristopher Nolan")
SELECT m.title, m.description, m.year, m.rating, d.name, d.surname
FROM movie AS m, director AS d
WHERE m.director_id = d.director_id
AND d.name = 'Christopher'
AND d.surname = 'Nolan'
ORDER BY m.year ASC;

-- 6) Select normal movies
SELECT title, description, year
FROM movie
WHERE rating >= 6.0
ORDER BY year ASC;

-- 7) Select top 10 movies of our service
SELECT title, description, year, rating
FROM movie
ORDER BY rating DESC
LIMIT 10;
