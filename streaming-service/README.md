# Movie streaming application

## Description
> The project is inspired by Netflix and Megogo.
It implements the backend part of a simple streaming application with a test payment system.
Users can choose and purchase different subscription plans, each containing a list of movies.
They can also view detailed information about movies, directors, actors, and subscription plans.
Admin roles are implemented to manage content and user access.

## Team
| Domain                    | GitHub                                      |
|---------------------------|---------------------------------------------|
| Movie, Actor, Performance | [markOone](https://github.com/markOone) |
| Subscption                | [arcctg](https://github.com/arcctg)        |
| Payment, User             | [StudentPP1](https://github.com/StudentPP1) |

## Tech stack
+ Java 21
+ Spring framework
+ Hibernate ORM
+ PostgreSQL
+ JUnit, Mockito, Testcontainer

## Config & Setup

### Clone repo
```bash
git clone https://github.com/markOone/StreamingService_BD.git
```

```bash
cd streaming-service
```

### Add payment provider
1. [Download the Stripe CLI](https://stripe.com/docs/stripe-cli#install)

2. login
```bash
stripe login
```

3. add webhook endpoint
```bash
stripe listen --forward-to localhost:8081/api/payments/webhook
```

### Run project

+ start (everything in docker)
```bash
docker compose --profile app up -d
```

+ start (only services needed for local run)
```bash
docker compose up -d
```

### Run tests

+ All tests
```bash
mvn clean test
```

+ One file
```bash
mvn -Dtest=PaymentRepositoryTest test
```

```bash
mvn -Dtest=PaymentRepositoryTest#shouldSavePayment test
```

## Project structure

```
streaming-service/
├── src/main/java/dev/studentpp1/streamingservice/
│   ├── auth/               # authentication and authorization
│   ├── users/              # user management
│   ├── payments/           # Stripe payment flow
│   ├── subscription/       # subscription logic and plans
│   ├── movies/             # movies, actors, directors
│   └── common/             # configs, exceptions, utilities
│
├── src/main/resources/
│   ├── application.yml     # app configuration
│   └── db/migration/       # Flyway SQL migrations
│
└── src/test/java/          # unit & integration tests
```

### Architecture of modules
Each module has a typical structure:

+ controller/ — REST API controllers
+ service/ — business login
+ repository/ — access to DB (Spring Data JPA)
+ entity/ — JPA entities (models)
+ dto/ — domain transport object
+ mapper/ — MapStruct mappers
+ exception/ — custom exceptions

## API examples

### Users endpoints

+ Register
```http request
POST /api/auth/register HTTP/1.1
Host: localhost:8081
Content-Type: application/json
Content-Length: 134

{
  "name": "Jonh",
  "surname": "Black",
  "email": "jonh@gmail.com",
  "password": "1234Ahadh~!",
  "birthday": "2004-12-03"
}
```

+ Get info about current user
```http request
GET /api/users/info HTTP/1.1
Host: localhost:8081
```

+ Login
```http request
POST /api/auth/login HTTP/1.1
Host: localhost:8081
Content-Type: application/json
Content-Length: 63

{
  "email": "jonh@gmail.com",
  "password": "1234Ahadh~!"
}
```

+ Update user details
```http request
POST /api/users/update HTTP/1.1
Host: localhost:8081
Content-Type: application/json
Content-Length: 45

{
  "name": "Mike",
  "surname": "White"
}
```

+ Logout
```http request
POST /api/auth/logout HTTP/1.1
Host: localhost:8081
```

## Payment endpoints
+ Buy subscription
```http request
POST /api/payments/checkout HTTP/1.1
Host: localhost:8081
Content-Type: application/json
Content-Length: 17

{
    "id": 1
}
```

+ Payment history of current user
```http request
GET /api/payments/user HTTP/1.1
Host: localhost:8081
```

+ Payment history of selected user's subscription
```http request
GET /api/payments/user/subscription/1 HTTP/1.1
Host: localhost:8081
```

## Admin endpoints
+ Soft delete user by id
```http request
DELETE /api/users/2 HTTP/1.1
Host: localhost:8081
```

+ Watch percent of subscription plan gained money by month
```http request
GET /api/payments/monthly-plans HTTP/1.1
Host: localhost:8081
```