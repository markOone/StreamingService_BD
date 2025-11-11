SELECT
    year,
    COUNT(movie_id) AS movies_per_year
FROM movie
GROUP BY year
ORDER BY year DESC;

SELECT
    duration,
    MIN(price) AS min_price,
    MAX(price) AS max_price,
    AVG(price) AS avg_price
FROM subscription_plan
GROUP BY duration
ORDER BY duration;

SELECT
    FLOOR(rating) AS rating_group,
    COUNT(movie_id) AS num_movies,
    AVG(rating) AS average_rating_in_group
FROM movie
WHERE rating IS NOT NULL
GROUP BY rating_group
ORDER BY rating_group DESC;

SELECT
    status,
    COUNT(payment_id) AS payments_count
FROM payment
GROUP BY status;

SELECT
    actor_id,
    COUNT(performance_id) AS roles_count
FROM performance
GROUP BY actor_id
ORDER BY roles_count DESC;