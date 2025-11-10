-- 1) Update movie rating
SELECT * FROM movie;

UPDATE movie
SET rating = 8.0
WHERE title = 'Inception';

SELECT * FROM movie;

-- 2) Change user email
SELECT * FROM "user";

UPDATE "user"
SET email = 'alice@example.com'
WHERE email = 'new_alice@example.com';

SELECT * FROM "user";

-- 3) Deactivate user subscription
SELECT * FROM user_subscription;

UPDATE user_subscription
SET status = 'CANCELLED'
WHERE user_id = 5 AND status = 'ACTIVE';

SELECT * FROM user_subscription;