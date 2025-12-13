CREATE INDEX IF NOT EXISTS idx_payment_user_subscription_id
    ON payment (user_subscription_id);

CREATE INDEX IF NOT EXISTS idx_user_subscription_user_id
    ON user_subscription (user_id);

CREATE INDEX IF NOT EXISTS idx_user_subscription_subscription_plan_id
    ON user_subscription (subscription_plan_id);
