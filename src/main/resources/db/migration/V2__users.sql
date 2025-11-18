DROP TABLE IF EXISTS users CASCADE;

CREATE TABLE users
(
    id UUID PRIMARY KEY,
    username   VARCHAR(255) NOT NULL UNIQUE,
    password   VARCHAR(255),
    name       VARCHAR(255) NOT NULL,
    role       VARCHAR(255) NOT NULL,

    created_at TIMESTAMP    NULL,
    updated_at TIMESTAMP    NULL,
    deleted_at TIMESTAMP    NULL
);