INSERT INTO users (name, surname, email, password, birthday, deleted, role)
SELECT 'admin', 'admin', 'admin@example.com',
       '$2a$12$R4s1A6yly1v5aKaC5jc/Nu8wI3gqObCLHWjqNkvU6f97x6vzK4Oc2',
       '1990-01-01',
       FALSE,
       'ROLE_ADMIN'