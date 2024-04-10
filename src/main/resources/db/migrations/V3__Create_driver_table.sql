CREATE TABLE drivers
(
    id              BIGSERIAL PRIMARY KEY,
    name            VARCHAR(255) NOT NULL,
    available       BOOLEAN NOT NULL DEFAULT true,
    activation_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    car_id          BIGINT,
    CONSTRAINT fk_car
        FOREIGN KEY (car_id)
            REFERENCES cars (id)
            ON DELETE SET NULL
);