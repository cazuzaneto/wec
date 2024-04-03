CREATE TABLE cars
(
    id            BIGSERIAL PRIMARY KEY,
    license_plate VARCHAR(255) NOT NULL UNIQUE,
    model         VARCHAR(255) NOT NULL,
    color         VARCHAR(255) NOT NULL
);