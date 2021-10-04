DROP TABLE IF exists routing_table;
DROP SEQUENCE IF EXISTS routing_table_seq;

CREATE SEQUENCE routing_table_seq;
CREATE TABLE routing_table (
    id                          BIGINT      NOT NULL DEFAULT nextval('routing_table_seq'::regclass),
    deliver_to_comp_id          VARCHAR(64) UNIQUE NOT NULL,
    sender_comp_id              VARCHAR(64) NOT NULL,
    target_comp_id              VARCHAR(64) NOT NULL,
    PRIMARY KEY (id)
);