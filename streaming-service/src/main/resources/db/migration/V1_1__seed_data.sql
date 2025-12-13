INSERT INTO director (name, surname, biography)
VALUES ('Christopher', 'Nolan',
        'British-American film director known for Inception and The Dark Knight.'),
       ('Steven', 'Spielberg', 'American director famous for Jurassic Park and E.T.'),
       ('Quentin', 'Tarantino', 'American filmmaker known for Pulp Fiction and Kill Bill.'),
       ('Sofia', 'Coppola', 'American director, screenwriter, and actress.'),
       ('Joel', 'Schumacher', 'Was an American film director, producer and screenwriter.'),
       ('Tim', 'Burton', 'Is an American filmmaker and artist.'),
       ('Zack', 'Snyder', 'Is an American filmmaker.'),
       ('Matt', 'Reeves', 'Is an American filmmaker.');

INSERT INTO actor (name, surname, biography)
VALUES ('Leonardo', 'DiCaprio', 'American actor known for Titanic and Inception.'),
       ('Samuel', 'Jackson', 'American actor and producer, Pulp Fiction, Avengers.'),
       ('Scarlett', 'Johansson', 'American actress, known for Lucy and Avengers.'),
       ('Tom', 'Hanks', 'American actor, Forrest Gump, Saving Private Ryan.'),
       ('Michael', 'Keaton', 'Is an American actor.'),
       ('Val', 'Kilmer', 'Was an American actor.'),
       ('George', 'Clooney', 'Is an American actor and filmmaker.'),
       ('Christian', 'Bale', 'Is an English actor.'),
       ('Ben', 'Affleck', 'Is an American actor and filmmaker.'),
       ('Robert', 'Pattinson', 'is an English actor.');

INSERT INTO subscription_plan (name, description, price, duration)
VALUES ('Basic', 'Access to limited movies', 4.99, 30),
       ('Standard', 'Access to full library in HD', 9.99, 30),
       ('Premium', 'Access to full library in 4K + extras', 14.99, 30),
       ('Annual', 'Yearly subscription with discount', 99.99, 365),
       ('Batman Classics Collection', 'Access to Best Batman movies', 2.99, 30);

INSERT INTO users (name, surname, email, password, birthday)
VALUES ('Alice', 'Smith', 'alice@example.com',
        '$2a$12$cNFQwOfBpG5CyTlTM/USpe2H2wRGFgmYeueCNQdVvU/D/oKoAGAAS',
        '1990-05-12'), -- password1
       ('Bob', 'Johnson', 'bob@example.com',
        '$2a$12$80iU24lKu2x.uhKt5OjLd.rTFkyg2ecdKPvKnxQp/m5XY98Wj51Ie',
        '1985-09-23'), -- password2
       ('Charlie', 'Brown', 'charlie@example.com',
        '$2a$12$JELhzDIWOUjsfYbr/KFcLOBCT9gPQCw/47HfxbU5QRFQkSL/C62c.',
        '1993-01-14'), -- password3
       ('Diana', 'Prince', 'diana@example.com',
        '$2a$12$RYYr10cBQ44npbp8sz4jAOR6IVJt9RDIKdL3vqBw5B31CDk3AYf4a',
        '1988-07-07'), -- password4
       ('Batman enjoyer', 'I like him', 'keatonthebest@example.com',
        '$2a$12$OWOaJCo3IUmdcZC/DdMKBu/GaLOK8EQhEGeLQIRZLLAylTZWAcP1O',
        '1980-03-17'); -- password5

INSERT INTO movie (title, description, year, rating, director_id)
VALUES ('Inception', 'A mind-bending thriller about dreams within dreams.', 2010, 8.8, 1),
       ('Jurassic Park', 'Dinosaurs are brought back to life in a theme park.', 1993, 8.1, 2),
       ('Pulp Fiction', 'Interwoven stories of crime in Los Angeles.', 1994, 8.9, 3),
       ('Lost in Translation', 'Two lost souls meet in Tokyo.', 2003, 7.7, 4),
       ('Batman', 'The Dark Knight of Gotham City begins his war on crime.', 1989, 7.5, 6),
       ('Batman Returns', 'Second Batman movie with Michael Keaton.', 1992, 7.1, 6),
       ('Batman Forever', '', 1995, 5.4, 5),
       ('Batman & Robin', '', 1997, 3.8, 5),
       ('Batman Begins', '', 2005, 8.2, 1),
       ('Dark Knight', '', 2008, 9.1, 1),
       ('The Dark Knight Rises', '', 2012, 8.4, 1),
       ('Batman v Superman: Dawn of Justice', '', 2016, 6.4, 7),
       ('The Batman', '', 2022, 7.8, 8);

INSERT INTO included_movie (movie_id, subscription_plan_id)
VALUES (1, 1),
       (1, 2),
       (2, 2),
       (2, 3),
       (3, 3),
       (4, 4),
       (5, 5),
       (6, 5),
       (7, 5),
       (8, 5),
       (9, 5),
       (10, 5),
       (11, 5),
       (12, 5),
       (13, 5);

INSERT INTO performance (character_name, description, actor_id, movie_id)
VALUES ('Cobb', 'A skilled thief.', 1, 1),
       ('Jules Winnfield', 'Philosophical hitman.', 2, 3),
       ('Charlotte', 'Young woman adrift in Tokyo.', 3, 4),
       ('Alan Grant', 'Paleontologist.', 4, 2),
       ('Batman', 'Superhero', 5, 5),
       ('Batman', 'Superhero', 5, 6),
       ('Batman', 'Superhero', 6, 7),
       ('Batman', 'Superhero', 7, 8),
       ('Batman', 'Superhero', 8, 9),
       ('Batman', 'Superhero', 8, 10),
       ('Batman', 'Superhero', 8, 11),
       ('Batman', 'Superhero', 9, 12),
       ('Batman', 'Superhero', 10, 13);

INSERT INTO user_subscription (start_time, end_time, status, subscription_plan_id, user_id)
VALUES ('2024-01-01 10:00:00', '2024-02-01 10:00:00', 'EXPIRED', 1, 1),
       ('2024-01-15 12:00:00', '2024-02-15 12:00:00', 'CANCELLED', 2, 2),
       ('2024-02-01 09:00:00', '2024-03-01 09:00:00', 'EXPIRED', 3, 3),
       ('2024-02-10 18:00:00', '2025-02-10 18:00:00', 'EXPIRED', 4, 4),
       ('2025-10-12 19:08:47', '2025-11-12 19:08:47', 'ACTIVE', 5, 5),
       ('2025-02-01 10:00:00', '2026-02-01 10:00:00', 'ACTIVE', 4, 3),
       ('2025-01-01 10:00:00', '2026-01-01 10:00:00', 'ACTIVE', 4, 1);

INSERT INTO payment (paid_at, amount, status, user_subscription_id)
VALUES ('2024-01-01 10:05:00', 4.99, 'COMPLETED', 1),
       ('2024-01-15 12:05:00', 9.99, 'REFUNDED', 2),
       ('2024-02-01 09:05:00', 14.99, 'COMPLETED', 3),
       ('2024-03-01 09:00:00', 14.99, 'FAILED', 3),
       ('2024-05-02 09:00:00', 14.99, 'FAILED', 3),
       ('2024-06-02 09:00:00', 14.99, 'FAILED', 3),
       ('2024-02-10 18:05:00', 99.99, 'COMPLETED', 4),
       ('2025-10-12 19:08:47', 2.99, 'COMPLETED', 5),
       ('2025-11-12 19:08:47', 2.99, 'FAILED', 5),
       ('2025-12-12 19:08:47', 2.99, 'FAILED', 5),
       ('2025-01-01 10:05:00', 99.99, 'COMPLETED', 6),
       ('2025-02-01 10:05:00', 99.99, 'COMPLETED', 7);