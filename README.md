# Lab 2

## SQL scripts

> All scripts put to special folder from witch they will copy to database docker image and run after start image

```yml
volumes:
    - ./init_db:/docker-entrypoint-initdb.d
```

1. [Init database tables](\init_db\00_init_tables.sql)

2. [Insert into table director](init_db/01_director.sql)

3. [Insert into table actor](init_db\02_actor.sql)

4. [Insert into table subscription_plan](init_db\03_subscription_plan.sql)

5. [Insert into table movie](init_db\04_movie.sql)

6. [Insert into table performance](init_db\05_performance.sql)

7. [Insert into table included_movie](init_db\06_included_movie.sql)

8. [Insert into table user](init_db\07_user.sql)

## Tables information

> Already mentioned in [lab1](https://github.com/markOone/StreamingService_BD/tree/lab1?tab=readme-ov-file#3-field-level-constraints)

## Evidence of our hard work 1

![select user](img\select_user.png)

## Evidence of our hard work 2

![public schema](img\public_schema.png)
