CREATE TYPE subscription_status AS ENUM (
    'ACTIVE',
    'CANCELLED',
    'EXPIRED'
);

CREATE TYPE payment_status AS ENUM (
    'PENDING',
    'COMPLETED',
    'FAILED',
    'REFUNDED'
);

CREATE TABLE IF NOT EXISTS director
(
    director_id SERIAL PRIMARY KEY,
    name        VARCHAR(100) NOT NULL,
    surname     VARCHAR(100) NOT NULL,
    biography   TEXT
);

CREATE TABLE IF NOT EXISTS actor
(
    actor_id  SERIAL PRIMARY KEY,
    name      VARCHAR(100) NOT NULL,
    surname   VARCHAR(100) NOT NULL,
    biography TEXT
);

CREATE TABLE IF NOT EXISTS subscription_plan
(
    subscription_plan_id SERIAL PRIMARY KEY,
    name                 VARCHAR(150)  NOT NULL UNIQUE,
    description          TEXT          NOT NULL,
    price                DECIMAL(8, 2) NOT NULL CHECK (price >= 0.0),
    duration             INT           NOT NULL
);

CREATE TABLE IF NOT EXISTS movie
(
    movie_id    SERIAL PRIMARY KEY,
    title       VARCHAR(255) NOT NULL,
    description TEXT,
    year        INT          NOT NULL CHECK (year > 1878),
    rating      DECIMAL(4, 1) CHECK (rating >= 0.0 AND rating <= 10.0),
    director_id INT          NOT NULL REFERENCES director (director_id)
);

CREATE TABLE IF NOT EXISTS performance
(
    performance_id SERIAL PRIMARY KEY,
    character_name VARCHAR(255) NOT NULL,
    description    TEXT         NOT NULL,
    actor_id       INT          NOT NULL REFERENCES actor (actor_id),
    movie_id       INT          NOT NULL REFERENCES movie (movie_id)
);

CREATE TABLE IF NOT EXISTS included_movie
(
    movie_id             INT NOT NULL REFERENCES movie (movie_id),
    subscription_plan_id INT NOT NULL REFERENCES subscription_plan (subscription_plan_id),
    PRIMARY KEY (movie_id, subscription_plan_id)
);

CREATE TABLE IF NOT EXISTS "user"
(
    user_id  SERIAL PRIMARY KEY,
    name     VARCHAR(100) NOT NULL,
    surname  VARCHAR(100) NOT NULL,
    email    VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    birthday DATE
);

CREATE TABLE IF NOT EXISTS user_subscription
(
    user_subscription_id SERIAL PRIMARY KEY,
    start_time           TIMESTAMP           NOT NULL,
    end_time             TIMESTAMP           NOT NULL,
    status               subscription_status NOT NULL,
    subscription_plan_id INT                 NOT NULL REFERENCES subscription_plan (subscription_plan_id),
    user_id              INT                 NOT NULL REFERENCES "user" (user_id),
    CONSTRAINT check_subscription_dates CHECK (end_time > start_time)
);

CREATE TABLE IF NOT EXISTS payment
(
    payment_id           SERIAL PRIMARY KEY,
    paid_at              TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    amount               DECIMAL(10, 2) NOT NULL CHECK (amount > 0),
    status               payment_status NOT NULL,
    user_subscription_id INT            NOT NULL REFERENCES user_subscription (user_subscription_id)
);