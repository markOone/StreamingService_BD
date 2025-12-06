-- Тестові дані
INSERT INTO director (name, surname, biography)
VALUES ('Matt', 'Reeves', 'Director for test data');

INSERT INTO users (name, surname, email, password, birthday)
VALUES
    ('Keaton', 'Best', 'keatonthebest@example.com', 'password123', '1990-01-01'),
    ('Alice',  'Smith','alice@example.com',          'password123', '1995-05-05');

INSERT INTO movie (title, description, year, rating, director_id)
VALUES
    ('The Batman',     'Test movie', 2022, 8.4, (SELECT director_id FROM director WHERE name='Matt' AND surname='Reeves' LIMIT 1)),
    ('The Dark Knight','Test movie', 2008, 9.0, (SELECT director_id FROM director WHERE name='Matt' AND surname='Reeves' LIMIT 1));


-- Додамо телефон лише одному користувачу, щоб перевірити, чи працює поле
UPDATE users
SET phone = '+380991234567'
WHERE email = 'keatonthebest@example.com';

-- Перевірка:
-- Користувач з телефоном має відобразити номер
-- Інші користувачі (Alice) мають мати NULL у полі phone
SELECT user_id, name, email, phone
FROM users
WHERE email IN ('keatonthebest@example.com', 'alice@example.com');

-- Перевірка constraint:
-- Має виникнути помилка через неправильний формат
-- UPDATE users SET phone = 'not-a-number' WHERE user_id = 1;


-- Додаємо відгук
INSERT INTO review (rating, comment, movie_id, user_id)
VALUES (10,
        'I am Vengeance! Great movie.',
        (SELECT movie_id FROM movie WHERE title = 'The Batman' LIMIT 1),
        (SELECT user_id FROM users WHERE email = 'keatonthebest@example.com' LIMIT 1));

-- Виводимо дані з JOIN задля перевірки цілісності зв'язків
SELECT u.name  AS user_name,
       u.phone AS user_phone,
       m.title AS movie_title,
       r.rating,
       r.comment,
       r.created_at
FROM review r
         JOIN users u ON r.user_id = u.user_id
         JOIN movie m ON r.movie_id = m.movie_id
WHERE m.title = 'The Batman';


-- Ми видалили колонки paid_at та user_subscription_id
-- Цей запит має успішно виконатися і показати таблицю без цих колонок
SELECT *
FROM payment
LIMIT 5;