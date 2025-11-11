-- 1) Delete a specific performance by its ID
SELECT * FROM performance
WHERE performance_id = 1;

DELETE FROM performance
WHERE performance_id = 1; 

SELECT * FROM performance
WHERE performance_id = 1;

-- 2) Delete a movie from a specific subscription plan
SELECT * FROM included_movie
WHERE movie_id = 1 AND subscription_plan_id = 1;

DELETE FROM included_movie
WHERE movie_id = 1 AND subscription_plan_id = 1; 

SELECT * FROM included_movie
WHERE movie_id = 1 AND subscription_plan_id = 1;

-- 3) Delete all failed payments made before June 1, 2024
SELECT * FROM payment
WHERE status = 'FAILED' AND paid_at < '2024-06-01';

DELETE FROM payment
WHERE status = 'FAILED' AND paid_at < '2024-06-01';

SELECT * FROM payment
WHERE status = 'FAILED' AND paid_at < '2024-06-01';
