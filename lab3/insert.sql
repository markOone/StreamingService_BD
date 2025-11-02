-- 1) Insert a new director
INSERT INTO director (name, surname, biography)
VALUES ('Patty', 'Jenkins', 'Director of Wonder Woman.');

SELECT * FROM director;

-- 2) Insert a new movie
INSERT INTO movie (title, description, year, rating, director_id)
VALUES ('Wonder Woman', 'Amazonian warrior princess.', 2017, 7.4, 1);

SELECT * FROM movie;

-- 3) Insert a new user
INSERT INTO "user" (name, surname, email, password, birthday)
VALUES ('Eve', 'Adams', 'eve@example.com', 'hashed_pass', '1992-11-05');

SELECT * FROM "user";