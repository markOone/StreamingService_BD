-- 1) Update movie rating
SELECT * FROM movie
WHERE title = 'Inception'

UPDATE movie
SET rating = 8.0
WHERE title = 'Inception';

SELECT * FROM movie
WHERE title = 'Inception';

-- 2) Change user email
SELECT * FROM "user"
WHERE name = 'Alice' AND surname = 'Smith';

UPDATE "user"
SET email = 'new_alice@example.com'
WHERE email = 'alice@example.com';

SELECT * FROM "user"
WHERE name = 'Alice' AND surname = 'Smith';

-- 3) Deactivate user subscription
SELECT * FROM user_subscription
WHERE user_id = 5;

UPDATE user_subscription
SET status = 'CANCELLED'
WHERE user_id = 5;

SELECT * FROM user_subscription
WHERE user_id = 5;
