CREATE TABLE rides
(
    id           BIGSERIAL PRIMARY KEY,
    pickup_id    BIGINT       NOT NULL,
    dropoff_id   BIGINT,
    status       VARCHAR(255) NOT NULL,
    driver_id    BIGINT,
    passenger_id BIGINT       NOT NULL,
    FOREIGN KEY (pickup_id) REFERENCES addresses (id),
    FOREIGN KEY (dropoff_id) REFERENCES addresses (id),
    FOREIGN KEY (driver_id) REFERENCES drivers (id),
    FOREIGN KEY (passenger_id) REFERENCES passengers (id)
);