-- while we don't finish payment -> null & without default value
ALTER TABLE payment
  ALTER COLUMN paid_at DROP NOT NULL,
  ALTER COLUMN paid_at DROP DEFAULT;

-- create user subscription only after success payment
ALTER TABLE payment
  ALTER COLUMN user_subscription_id DROP NOT NULL;

-- created_at for delete old pending payment
ALTER TABLE payment
  ADD COLUMN IF NOT EXISTS created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP;

-- unique external payment id from provider
ALTER TABLE payment
  ADD COLUMN IF NOT EXISTS provider_session_id VARCHAR(255);

CREATE UNIQUE INDEX IF NOT EXISTS uk_payment_provider_pid
  ON payment(provider_session_id)
  WHERE provider_session_id IS NOT NULL;

-- for delete old pending payments
CREATE INDEX IF NOT EXISTS ix_payment_status_created
  ON payment(status, created_at);
