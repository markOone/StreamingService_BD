-- 1) Update movie rating
SELECT * FROM movie;

UPDATE movie
SET rating = 8.0
WHERE title = 'Wonder Woman';

SELECT * FROM movie;

-- 2) Change user email
SELECT * FROM "user";

UPDATE "user"
SET email = 'eve.a@example.com'
WHERE email = 'eve@example.com';

SELECT * FROM "user";

-- 3) Deactivate user subscription
SELECT * FROM user_subscription;

UPDATE user_subscription
SET status = 'CANCELLED'
WHERE user_id = 1 AND status = 'ACTIVE';

SELECT * FROM user_subscription;