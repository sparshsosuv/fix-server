DROP TABLE IF exists QueuedMessage;
DROP SEQUENCE IF EXISTS QueuedMessage_seq;

CREATE TABLE QueuedMessage (
                               id BIGSERIAL PRIMARY KEY,
                               clientOrderId VARCHAR(255),
                               message TEXT,
                               processed BOOLEAN,
                               sender VARCHAR(255),
                               receiver VARCHAR(255)
);
