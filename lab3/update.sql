-- 1) Update movie rating
UPDATE movie
SET rating = 8.0
WHERE title = 'Wonder Woman';

-- 2) Change user email
UPDATE "user"
SET email = 'eve.a@example.com'
WHERE email = 'eve@example.com';

-- 3) Deactivate user subscription
UPDATE user_subscription
SET status = 'CANCELLED'
WHERE user_id = 1 AND status = 'ACTIVE';
