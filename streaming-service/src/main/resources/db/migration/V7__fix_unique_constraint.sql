ALTER TABLE subscription_plan
    DROP CONSTRAINT IF EXISTS subscription_plan_name_key;

CREATE UNIQUE INDEX idx_subscription_plan_name_unique_active
    ON subscription_plan (name) WHERE deleted = false;
