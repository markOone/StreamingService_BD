-- For join with user_subscription
CREATE INDEX IF NOT EXISTS idx_payment_user_subscription_id
    ON payment(user_subscription_id);

-- For analytics query (using status & aggregating by date)
CREATE INDEX IF NOT EXISTS idx_payment_status_paid_at
    ON payment(status, paid_at DESC);

-- For cleanup query deleting old payments
CREATE INDEX IF NOT EXISTS idx_payment_paid_at
    ON payment(paid_at);

-- For finding user's subscriptions with active status
CREATE INDEX IF NOT EXISTS idx_user_subscription_user_status
    ON user_subscription(user_id, status);

-- For join with subscription_plan
CREATE INDEX IF NOT EXISTS idx_user_subscription_subscription_plan_id
    ON user_subscription(subscription_plan_id);

-- For finding expired subscriptions
CREATE INDEX IF NOT EXISTS idx_user_subscription_status_dates
    ON user_subscription(status, end_time DESC);

-- for authentication queries (find not deleted users)
CREATE INDEX IF NOT EXISTS idx_users_email_deleted
    ON users(email)
    WHERE deleted = FALSE;

-- For fetching movies by director
CREATE INDEX IF NOT EXISTS idx_movie_director_id
    ON movie(director_id);

-- For fetching actor's performances
CREATE INDEX IF NOT EXISTS idx_performance_actor_id
    ON performance(actor_id);

-- For fetching movie's cast
CREATE INDEX IF NOT EXISTS idx_performance_movie_id
    ON performance(movie_id);

-- For fetching movies included in subscription plan
CREATE INDEX IF NOT EXISTS idx_included_movie_subscription_plan_id
    ON included_movie(subscription_plan_id);