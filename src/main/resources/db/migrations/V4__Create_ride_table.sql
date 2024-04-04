CREATE TABLE rides
(
    id         BIGSERIAL PRIMARY KEY,
    pickup_id  BIGINT       NOT NULL,
    dropoff_id BIGINT       NOT NULL,
    status     VARCHAR(255) NOT NULL,
    FOREIGN KEY (pickup_id) REFERENCES addresses (id),
    FOREIGN KEY (dropoff_id) REFERENCES addresses (id)
);