# Streaming service 

## Docker
+ stop all in docker:
```bash
docker compose down --rmi all --volumes --remove-orphans
```

+ start (everything in docker)
```bash
docker compose --profile app up -d
```

+ start (only services needed for local run)
```bash
docker compose up -d
```

## Stripe (local)
1. [Download the Stripe CLI](https://stripe.com/docs/stripe-cli#install)

2. login
```bash
stripe login
```

3. add webhook endpoint
```bash
stripe listen --forward-to localhost:8081/api/payments/webhook
```

### Requirements
- [x] Мінімум 5-7 пов'язаних таблиць, що представляють обрану предметну область
- [x] 3НФ - третя нормальна форма
- [x] Коректні типи даних для всіх стовпців
- [x] Первинні ключі в усіх таблицях
- [x] Зовнішні ключі з правильними обмеженнями
- [x] Щонайменше 2-3 індекси: [V4__add_indexes.sql](src/main/resources/db/migration/V4__add_indexes.sql)
- [x] CHECK-обмеження: [V1__init_schema.sql](src/main/resources/db/migration/V1__init_schema.sql)
- [x] Кілька операцій INSERT в одній транзакції: [PaymentStatusService.java](src/main/java/dev/studentpp1/streamingservice/payments/service/PaymentStatusService.java)
- [x] Операції UPDATE з умовами WHERE: [UserService.java](src/main/java/dev/studentpp1/streamingservice/users/service/UserService.java)
- [x] Жорстке або м'яке видалення з правильною поведінкою CASCADE: [PaymentService.java](src/main/java/dev/studentpp1/streamingservice/payments/service/PaymentService.java)
- [x] Прості SELECT-запити: [PaymentService.java](src/main/java/dev/studentpp1/streamingservice/payments/service/PaymentService.java)
- [x] Складні аналітичні запити: [PaymentAnalyticsService.java](src/main/java/dev/studentpp1/streamingservice/payments/service/PaymentAnalyticsService.java)
- [x] Тести: [streamingservice](src/test/java/dev/studentpp1/streamingservice)
- [x] Структура docker-compose.yml
- [ ] Документація