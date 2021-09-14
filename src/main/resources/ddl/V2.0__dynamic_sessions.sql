DROP TABLE IF exists dynamic_sessions;

CREATE TABLE dynamic_sessions (
    start_time                      VARCHAR(8) NOT NULL,
    end_time                        VARCHAR(8) NOT NULL,
    heart_bt_int                    VARCHAR(10) NOT NULL,
    valid_order_types               VARCHAR(64) NOT NULL,
    use_data_dictionary             VARCHAR(64) NOT NULL,
    default_market_price            VARCHAR(10) NOT NULL,
    validate_user_defined_fields    VARCHAR(1) NOT NULL,
    validate_fields_out_of_order    VARCHAR(1) NOT NULL,
    validate_fields_have_value      VARCHAR(1) NOT NULL,
    persist_messages                VARCHAR(1) NOT NULL,
    reset_on_logout                 VARCHAR(1) NOT NULL,
    reset_on_disconnect             VARCHAR(1) NOT NULL,
    begin_string                    VARCHAR(25) NOT NULL,
    connection_type                 VARCHAR(25) NOT NULL,
    socket_connect_host             VARCHAR(255) NOT NULL,
    socket_connect_port             VARCHAR(4) NOT NULL,
    socket_accept_port              VARCHAR(4) NOT NULL,
    sender_comp_id                  VARCHAR(255) NOT NULL,
    target_comp_id                  VARCHAR(255) NOT NULL,
    data_dictionary                 VARCHAR(255) NOT NULL,
    PRIMARY KEY (begin_string, sender_comp_id, target_comp_id)
);