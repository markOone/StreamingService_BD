# Database Normalization Report: Subscription & Payment Module

This document provides a concrete example of normalization from an initially unnormalized (0NF) table to a fully normalized schema in First (1NF), Second (2NF), and Third Normal Form (3NF).

The example focuses on the User–Subscription–Payment domain of the streaming service database.

---

## 1. Initial State (Unnormalized / 0NF)

Imagine that initially, all data regarding users, plans, and payments was collected in a single, unstructured log table (e.g., imported from a legacy CSV file).

```sql
CREATE TABLE payment_log_raw (
    -- Composite Primary Key (Unique identifier for the row)
    user_email       VARCHAR(255),
    plan_name        VARCHAR(150),
    payment_date     TIMESTAMP,

    -- User Attributes
    user_full_name   VARCHAR(255), -- Violation: Contains "Name Surname"
    user_contacts    TEXT,         -- Violation: Contains "email, phone, skype"

    -- Plan Attributes
    plan_price       DECIMAL(8,2),
    plan_duration    INT,

    -- Transaction/Subscription Attributes
    amount_paid      DECIMAL(10,2),
    payment_status   VARCHAR(50),
    sub_start_date   TIMESTAMP,
    sub_end_date     TIMESTAMP,

    PRIMARY KEY (user_email, plan_name, payment_date)
);
```

### 1.1. Problems in the Initial Design

This design contains severe anomalies:

1.  **1NF Violation (Atomicity):**
      * `user_full_name` contains two facts (Name and Surname).
      * `user_contacts` contains a list of values (comma-separated).
2.  **2NF Violation (Partial Dependency):**
      * The primary key is composite: `(user_email, plan_name, payment_date)`.
      * However, `plan_price` depends only on `plan_name`.
      * `user_full_name` depends only on `user_email`.
      * These attributes do not depend on the whole key.
3.  **3NF Violation (Transitive Dependency):**
      * Subscription dates (`sub_start_date`, `sub_end_date`) define the service period, whereas `amount_paid` and `payment_date` define the financial transaction. Mixing them causes redundancy if a user pays multiple times for the same subscription (e.g., installments or renewals).

-----

## 2\. Functional Dependencies

Based on the data analysis, we identified the following functional dependencies (FDs):

1.  **Partial FDs (Violating 2NF):**

      * `user_email → user_full_name, user_contacts`
      * `plan_name → plan_price, plan_duration`

2.  **Transitive/Full FDs:**

      * `(user_email, plan_name, payment_date) → amount_paid, payment_status, sub_start_date, sub_end_date`

-----

## 3\. Normalization Process

We will now step through the normalization forms to fix these issues.

### 3.1. First Normal Form (1NF)

**Definition (1NF):**

  * All attributes must be **atomic** (single value per cell).
  * No repeating groups.

**Fix:**

1.  Split `user_full_name` into `name` and `surname`.
2.  Remove `user_contacts` list. We will rely on `email` as the unique identifier and store it as a single scalar value.

**Resulting Table (Satisfies 1NF):**

  * Columns: `user_email`, `plan_name`, `payment_date`, `user_name`, `user_surname`, `plan_price`, `plan_duration`, `amount`, `status`...
  * Issue: Data is atomic, but redundancy remains.

-----

### 3.2. Second Normal Form (2NF)

**Definition (2NF):**

  * The relation is in 1NF.
  * **No partial dependencies** (non-key attributes must depend on the entire primary key).

**Fix:**
We decompose the table by extracting attributes that depend on only part of the composite key into their own tables. We also introduce **Surrogate Keys** (`SERIAL`) for efficiency.

#### Step 2.1: Extract `User`

Depends only on `user_email`.

```sql
CREATE TABLE "user" (
    user_id    SERIAL PRIMARY KEY,
    name       VARCHAR(100) NOT NULL,
    surname    VARCHAR(100) NOT NULL,
    email      VARCHAR(255) NOT NULL UNIQUE
);
```

#### Step 2.2: Extract `SubscriptionPlan`

Depends only on `plan_name`.

```sql
CREATE TABLE subscription_plan (
    subscription_plan_id SERIAL PRIMARY KEY,
    name        VARCHAR(150) NOT NULL UNIQUE,
    price       DECIMAL(8, 2) NOT NULL,
    duration    INT NOT NULL
);
```

**Result:**
We now have `user` and `subscription_plan` tables (in 2NF). The remaining transaction data links `user_id` and `subscription_plan_id`.

-----

### 3.3. Third Normal Form (3NF)

**Definition (3NF):**

  * The relation is in 2NF.
  * **No transitive dependencies** (non-key attributes cannot depend on other non-key attributes).

**Analysis of Remaining Data:**
We are left with transaction data: `user_id`, `plan_id`, `start_time`, `end_time`, `amount`, `paid_at`, `payment_status`.

  * **Dependency:** A **Payment** is made for a **Subscription**.
  * `Payment → Subscription → User/Plan`.
  * Storing `start_time` (Subscription data) and `paid_at` (Payment data) in one row creates logical ambiguity and potential anomalies (e.g., a failed payment shouldn't necessarily delete the record that a subscription was attempted).

**Fix:**
Decompose into two logical entities:

1.  **`user_subscription`**: Represents the agreement/period.
2.  **`payment`**: Represents the financial event linked to that subscription.

#### Step 3.1: Create `user_subscription`

```sql
CREATE TABLE user_subscription (
    user_subscription_id SERIAL PRIMARY KEY,
    start_time           TIMESTAMP NOT NULL,
    end_time             TIMESTAMP NOT NULL,
    status               subscription_status NOT NULL,
    subscription_plan_id INT NOT NULL REFERENCES subscription_plan(subscription_plan_id),
    user_id              INT NOT NULL REFERENCES "user"(user_id),
    CONSTRAINT check_subscription_dates CHECK (end_time > start_time)
);
```

#### Step 3.2: Create `payment`

```sql
CREATE TABLE payment (
    payment_id           SERIAL PRIMARY KEY,
    paid_at              TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    amount               DECIMAL(10, 2) NOT NULL CHECK (amount > 0),
    status               payment_status NOT NULL,
    user_subscription_id INT NOT NULL REFERENCES user_subscription(user_subscription_id)
);
```

-----

## 4\. Final Resulting Schema

After applying 1NF (atomicity), 2NF (removing partial dependencies), and 3NF (removing transitive dependencies), we have the following clean schema:

### 1\. User Table

```sql
CREATE TABLE "user" (
    user_id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    surname VARCHAR(100) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    birthday DATE
);
```

### 2\. Subscription Plan Table

```sql
CREATE TABLE subscription_plan (
    subscription_plan_id SERIAL PRIMARY KEY,
    name VARCHAR(150) NOT NULL UNIQUE,
    description TEXT NOT NULL,
    price DECIMAL(8, 2) NOT NULL CHECK (price >= 0.0),
    duration INT NOT NULL
);
```

### 3\. User Subscription Table

```sql
CREATE TABLE user_subscription (
    user_subscription_id SERIAL PRIMARY KEY,
    start_time TIMESTAMP NOT NULL,
    end_time TIMESTAMP NOT NULL,
    status subscription_status NOT NULL,
    subscription_plan_id INT NOT NULL REFERENCES subscription_plan(subscription_plan_id),
    user_id INT NOT NULL REFERENCES "user"(user_id),
    CONSTRAINT check_subscription_dates CHECK (end_time > start_time)
);
```

### 4\. Payment Table

```sql
CREATE TABLE payment (
    payment_id SERIAL PRIMARY KEY,
    paid_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    amount DECIMAL(10, 2) NOT NULL CHECK (amount > 0),
    status payment_status NOT NULL,
    user_subscription_id INT NOT NULL REFERENCES user_subscription(user_subscription_id)
);
```

### Conclusion

This decomposition ensures:

  * **Data Integrity:** Prices and User names are stored in one place.
  * **Scalability:** We can add new plans without needing a transaction.
  * **Flexibility:** A user can have a subscription with multiple payment attempts (e.g., failed then successful) without duplicating subscription dates.
