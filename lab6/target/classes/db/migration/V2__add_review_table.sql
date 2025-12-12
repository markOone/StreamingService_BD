CREATE TABLE review
(
    review_id  SERIAL PRIMARY KEY,
    rating     INT NOT NULL CHECK (rating BETWEEN 1 AND 10),
    comment    TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    movie_id   INT NOT NULL REFERENCES movie (movie_id) ON DELETE CASCADE,
    user_id    INT NOT NULL REFERENCES "user" (user_id) ON DELETE CASCADE
);