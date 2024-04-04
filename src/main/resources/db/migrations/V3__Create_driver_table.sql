CREATE TABLE drivers
(
    id        BIGSERIAL PRIMARY KEY,
    name      VARCHAR(255) NOT NULL,
    available BOOLEAN      NOT NULL DEFAULT true,
    car_id    BIGINT,
    CONSTRAINT fk_car
        FOREIGN KEY (car_id)
            REFERENCES cars (id)
            ON DELETE SET NULL
);
