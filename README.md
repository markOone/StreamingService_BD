# Netflix like Streaming Service - Database Design
![ER diagram](https://github.com/user-attachments/assets/4db2300d-2ded-473d-8fd2-db7245dfdef4)


## Table of Contents
- [Summary of Requirements](#1-summary-of-requirements)
- [Entities, Attributes, and Relationships](#2-entities-attributes-and-relationships)
- [Field-Level Constraints](#3-field-level-constraints)
- [System Features](#4-system-features)
  
## **1. Summary of Requirements**

- **Stakeholder Needs**: The system requires a robust backend to manage a subscription-based streaming service. Key needs to include user account management (registration, login), a comprehensive movie catalog (information on movies, directors, actors), and a complete subscription lifecycle system (plan selection, payment processing, subscription status management).
- **Data to be stored**:
  - **User Data**: User profile information, including names, contact details (email), credentials, and birthdate.
  - **Content Catalog**: Detailed information for movies (title, description, year, rating), directors (biography), actors (biography), and the specific roles (performances) actors played in movies.
  - **Subscription and Financial Data**: Descriptions of available subscription plans (name, price, duration), records of user subscriptions (start/end dates, status), and a history of all payments (amount, timestamp, status).
- **Business Rules**:
  - A movie must have exactly one director, but a director can helm zero or more movies.
  - A movie can feature zero to many actor performances. An actor can perform in zero or more movies.
  - A user can have zero or many subscriptions over time and can make zero or many payments.
  - A subscription plan can contain many movies, and a single movie can be included in zero or many different plans.
  - Each specific user subscription instance is based on one specific plan.
  - Each payment is made for one specific user subscription.

## **2. Entities, Attributes, and Relationships**

**Entities: Actor, Performance, Movie, Director, Payment, User, User_Subscription, Subscription_Plan, Included_Movie**

**Attributes:**

- **Actor: (PK)** actor_id, name, surname, biography
- **Performance: (PK)** performance_id, character_name, description, **(FK)** actor_id, **(FK)** movie_id
- **Movie: (PK)** movie_id, title, description, year, rating, **(FK)** director_id
- **Director: (PK)** director_id, name, surname, biography
- **Included_Movie: composite PK** (**(FK)** movie_id, **(FK)** subscription_plan_id)
- **Subscription_Plan: (PK)** subscription_plan_id, name, description, price, duration
- **User_Subscription: (PK)** user_subscription_id, start_time, end_time, status, **(FK)** subscription_plan_id, **(FK)** user_id
- **Payment: (PK)** payment_id, paid_at, amount, status, **(FK)** user_subscription_id
- **User: (PK)** user_id, name, surname, email, password, birthday

**Relationships:**

- **Actor (one and only one ⇔ zero or many) Performance**: In a low-budget movie, one actor can play a few roles. An actor can have zero performances if the movie was deleted
- **Movie (one and only one ⇔ zero or many) Performance**: Cartoons don't have actors
- **Director (one and only one ⇔ zero or many) Movie**: A director can have zero movies if the movie was deleted
- **Movie (one and only one ⇔ zero or many) Included_Movie**: A movie can exist on a streaming service but not be included in any subscription
- **Subscription_Plan (one and only one ⇔ one or many) Included_Movie**
- **Subscription_Plan (one and only one ⇔ one or many) User_Subscription**: One subscription plan can be chosen for many different user subscriptions
- **User_Subscription (one and only one ⇔ zero or many) Payment**
- **User (one and only one ⇔ zero or many) User_Subscription**: One user can have many subscriptions over time

## 3. Field-Level Constraints

**"user"**
| Field | Type | Constraints |
|-------|------|-------------|
| user_id (PK) | SERIAL | NOT NULL, PRIMARY KEY |
| name | VARCHAR(100) | NOT NULL |
| surname | VARCHAR(100) | NOT NULL |
| email | VARCHAR(255) | NOT NULL, UNIQUE |
| password | VARCHAR(255) | NOT NULL |
| birthday | DATE | NULLABLE |

> **Note:** The `password` field stores a hash, not plaintext

**subscription_plan**
| Field | Type | Constraints |
|-------|------|-------------|
| subscription_plan_id (PK) | SERIAL | NOT NULL, PRIMARY KEY |
| name | VARCHAR(150) | NOT NULL, UNIQUE |
| description | TEXT | NOT NULL |
| price | DECIMAL(8, 2) | NOT NULL, CHECK (price >= 0.0) |
| duration | INT | NOT NULL |

> **Note:** `duration` is in days (e.g., 30 for a monthly plan).

**user_subscription**
| Field | Type | Constraints |
|-------|------|-------------|
| user_subscription_id (PK) | SERIAL | NOT NULL, PRIMARY KEY |
| start_time | TIMESTAMP | NOT NULL |
| end_time | TIMESTAMP | NOT NULL |
| status | subscription_status (ENUM) | NOT NULL |
| subscription_plan_id (FK) | INT | NOT NULL, REFERENCES subscription_plan |
| user_id (FK) | INT | NOT NULL, REFERENCES "user" |
| | | CHECK (end_time > start_time) |

**payment**
| Field | Type | Constraints |
|-------|------|-------------|
| payment_id (PK) | SERIAL | NOT NULL, PRIMARY KEY |
| paid_at | TIMESTAMP | NOT NULL, DEFAULT CURRENT_TIMESTAMP |
| amount | DECIMAL(10, 2) | NOT NULL, CHECK (amount > 0) |
| status | payment_status (ENUM) | NOT NULL |
| user_subscription_id (FK) | INT | NOT NULL, REFERENCES user_subscription |

**movie**
| Field | Type | Constraints |
|-------|------|-------------|
| movie_id (PK) | SERIAL | NOT NULL, PRIMARY KEY |
| title | VARCHAR(255) | NOT NULL |
| description | TEXT | NULLABLE |
| year | INT | NOT NULL, CHECK (year > 1878) |
| rating | DECIMAL(4, 1) | NULLABLE, CHECK (rating >= 0.0 AND rating <= 10.0) |
| director_id (FK) | INT | NOT NULL, REFERENCES director |

**director**
| Field | Type | Constraints |
|-------|------|-------------|
| director_id (PK) | SERIAL | NOT NULL, PRIMARY KEY |
| name | VARCHAR(100) | NOT NULL |
| surname | VARCHAR(100) | NOT NULL |
| biography | TEXT | NULLABLE |

**actor**
| Field | Type | Constraints |
|-------|------|-------------|
| actor_id (PK) | SERIAL | NOT NULL, PRIMARY KEY |
| name | VARCHAR(100) | NOT NULL |
| surname | VARCHAR(100) | NOT NULL |
| biography | TEXT | NULLABLE |

**performance**
| Field | Type | Constraints |
|-------|------|-------------|
| performance_id (PK) | SERIAL | NOT NULL, PRIMARY KEY |
| character_name | VARCHAR(255) | NOT NULL |
| description | TEXT | NOT NULL |
| actor_id (FK) | INT | NOT NULL, REFERENCES actor |
| movie_id (FK) | INT | NOT NULL, REFERENCES movie |

**included_movie**
| Field | Type | Constraints |
|-------|------|-------------|
| movie_id (FK, PK component) | INT | NOT NULL, REFERENCES movie |
| subscription_plan_id (FK, PK component) | INT | NOT NULL, REFERENCES subscription_plan |

## 4. System Features

**User can register/login/logout**

**User can buy subscription => it is requirement to start payment operation (create Payment entity with specific subscription plan) => if payment is success (status) => create new entity User_Subscription related to User with start_time => User can watch films/series (PEREMOGA!!!)**

**User can cancel Payment or Subscription**

