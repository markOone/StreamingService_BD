# **Normalization Report — Streaming Service Database**

## **1. Introduction**

This report analyzes and evaluates the database schema of a movie-streaming service.
The objective is to:

* document **functional dependencies (FDs)** for every table,
* determine the **highest normal form** of the initial design,
* formally verify compliance with **1NF, 2NF, and 3NF**,
* demonstrate the normalization process (even if no structural changes are required),
* provide the final **SQL DDL** reflecting a fully normalized schema.

Although the schema is already well-designed, the normalization steps are still described formally to meet the lab requirements.

---

## **2. Functional Dependencies of the Initial Schema**

Below is the **minimal and complete set** of FDs for each table.

---

## **2.1. `director(director_id, name, surname, biography)`**

**Primary key:** `director_id`

**Functional dependencies:**

* `director_id → name, surname, biography`

This is the full and minimal FD set.

---

## **2.2. `actor(actor_id, name, surname, biography)`**

**Primary key:** `actor_id`

**FDs:**

* `actor_id → name, surname, biography`

---

## **2.3. `subscription_plan(subscription_plan_id, name, description, price, duration)`**

**Primary key:** `subscription_plan_id`
**Alternate key:** `name` (unique)

**FDs:**

* `subscription_plan_id → name, description, price, duration`
* `name → subscription_plan_id, description, price, duration`

The second FD exists due to the `UNIQUE(name)` constraint.

---

## **2.4. `movie(movie_id, title, description, year, rating, director_id)`**

**Primary key:** `movie_id`

**FDs:**

* `movie_id → title, description, year, rating, director_id`

---

## **2.5. `performance(performance_id, character_name, description, actor_id, movie_id)`**

**Primary key:** `performance_id`

**FDs:**

* `performance_id → character_name, description, actor_id, movie_id`

---

## **2.6. `included_movie(movie_id, subscription_plan_id)`**

**Composite primary key:** `(movie_id, subscription_plan_id)`

The table has no non-key attributes.

**FDs:**

* `(movie_id, subscription_plan_id) → movie_id, subscription_plan_id`

---

## **2.7. `"user"(user_id, name, surname, email, password, birthday)`**

**Primary key:** `user_id`
**Alternate key:** `email`

**FDs:**

* `user_id → name, surname, email, password, birthday`
* `email → user_id, name, surname, password, birthday`

---

## **2.8. `user_subscription(user_subscription_id, start_time, end_time, status, subscription_plan_id, user_id)`**

**Primary key:** `user_subscription_id`

**FDs:**

* `user_subscription_id → start_time, end_time, status, subscription_plan_id, user_id`

---

## **2.9. `payment(payment_id, paid_at, amount, status, user_subscription_id)`**

**Primary key:** `payment_id`

**FDs:**

* `payment_id → paid_at, amount, status, user_subscription_id`

---

## **3. Normal Form Analysis**

This section evaluates compliance with **1NF, 2NF, and 3NF** based on the functional dependencies above.

---

## **3.1. First Normal Form (1NF)**

A relation is in 1NF if:

* all attributes contain atomic values,
* no repeating groups exist,
* each table has a primary key.

**Evaluation:**
All tables satisfy these rules. No multivalued attributes or nested structures are present.

➡️ **The entire schema is in 1NF.**

---

## **3.2. Second Normal Form (2NF)**

2NF requires:

1. the table is in 1NF,
2. no non-key attribute depends on part of a composite key.

Only one table has a composite key:

* `included_movie(movie_id, subscription_plan_id)`

* It has **no non-key attributes**, so partial dependencies cannot exist.

All other tables use simple primary keys → 2NF is automatically satisfied.

➡️ **The entire schema is in 2NF.**

---

## **3.3. Third Normal Form (3NF)**

3NF requires:

1. the table is in 2NF,
2. no non-key attribute depends transitively on the key.

Evaluation:

* None of the tables contain dependencies of the form
  `key → attributeA → attributeB`.
* Lookup entities (`director`, `actor`, `subscription_plan`, `user`) are isolated properly.
* Tables referencing them only store foreign keys, not redundant descriptive information.

➡️ **The entire schema is in 3NF.**

---

## **4. Final SQL DDL (Fully Normalized Schema — 3NF)**

```sql
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

CREATE TABLE director (
    director_id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    surname VARCHAR(100) NOT NULL,
    biography TEXT
);

CREATE TABLE actor (
    actor_id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    surname VARCHAR(100) NOT NULL,
    biography TEXT
);

CREATE TABLE subscription_plan (
    subscription_plan_id SERIAL PRIMARY KEY,
    name VARCHAR(150) NOT NULL UNIQUE,
    description TEXT NOT NULL,
    price DECIMAL(8, 2) NOT NULL CHECK (price >= 0.0),
    duration INT NOT NULL
);

CREATE TABLE movie (
    movie_id SERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    year INT NOT NULL CHECK (year > 1878),
    rating DECIMAL(4, 1) CHECK (rating >= 0 AND rating <= 10),
    director_id INT NOT NULL REFERENCES director(director_id)
);

CREATE TABLE performance (
    performance_id SERIAL PRIMARY KEY,
    character_name VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    actor_id INT NOT NULL REFERENCES actor(actor_id),
    movie_id INT NOT NULL REFERENCES movie(movie_id)
);

CREATE TABLE included_movie (
    movie_id INT NOT NULL REFERENCES movie(movie_id),
    subscription_plan_id INT NOT NULL REFERENCES subscription_plan(subscription_plan_id),
    PRIMARY KEY (movie_id, subscription_plan_id)
);

CREATE TABLE "user" (
    user_id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    surname VARCHAR(100) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    birthday DATE
);

CREATE TABLE user_subscription (
    user_subscription_id SERIAL PRIMARY KEY,
    start_time TIMESTAMP NOT NULL,
    end_time TIMESTAMP NOT NULL,
    status subscription_status NOT NULL,
    subscription_plan_id INT NOT NULL REFERENCES subscription_plan(subscription_plan_id),
    user_id INT NOT NULL REFERENCES "user"(user_id),
    CONSTRAINT check_subscription_dates CHECK (end_time > start_time)
);

CREATE TABLE payment (
    payment_id SERIAL PRIMARY KEY,
    paid_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    amount DECIMAL(10, 2) NOT NULL CHECK (amount > 0),
    status payment_status NOT NULL,
    user_subscription_id INT NOT NULL REFERENCES user_subscription(user_subscription_id)
);
```

---
