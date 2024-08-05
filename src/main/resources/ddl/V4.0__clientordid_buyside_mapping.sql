DROP TABLE IF exists clientordid_buyside_mapping;
DROP SEQUENCE IF EXISTS clientordid_buyside_mapping_seq;

CREATE SEQUENCE clientordid_buyside_mapping_seq;
CREATE TABLE clientordid_buyside_mapping (
                               id                          BIGINT      NOT NULL DEFAULT nextval('routing_table_seq'::regclass),
                               client_order_id             VARCHAR(64) UNIQUE NOT NULL,
                               buy_side_session            VARCHAR(64) NOT NULL,
                               PRIMARY KEY (id)
);