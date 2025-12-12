ALTER TABLE "user"
    ADD COLUMN phone TEXT NULL;

ALTER TABLE "user"
    ADD CONSTRAINT phone_format CHECK (phone ~ '^\+?[0-9]{10,15}$');
