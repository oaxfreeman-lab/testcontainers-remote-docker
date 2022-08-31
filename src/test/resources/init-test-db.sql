CREATE TABLE IF NOT EXISTS messages (
    id serial PRIMARY KEY,
    message VARCHAR ( 128 ),
    type VARCHAR (32) NOT NULL
);
