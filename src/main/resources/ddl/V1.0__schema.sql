DROP TABLE IF exists log_income_messages;
DROP TABLE IF exists log_outgoing_messages;
DROP TABLE IF exists log_events;
DROP TABLE IF exists messages;
DROP TABLE IF exists sessions;

CREATE TABLE messages (
    time TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    beginstring       VARCHAR(8)    NOT NULL,
    sendercompid      VARCHAR(64)   NOT NULL,
    sendersubid       VARCHAR(64)   NOT NULL,
    senderlocid       VARCHAR(64)   NOT NULL,
    targetcompid      VARCHAR(64)   NOT NULL,
    targetsubid       VARCHAR(64)   NOT NULL,
    targetlocid       VARCHAR(64)   NOT NULL,
    session_qualifier VARCHAR(64)   NOT NULL,
    msgseqnum         INT           NOT NULL,
    message           VARCHAR(2048) NOT NULL,
    PRIMARY KEY(time, beginstring)
);

CREATE TABLE log_income_messages (
    time TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    beginstring       VARCHAR(8)    NOT NULL,
    sendercompid      VARCHAR(64)   NOT NULL,
    sendersubid       VARCHAR(64)   NOT NULL,
    senderlocid       VARCHAR(64)   NOT NULL,
    targetcompid      VARCHAR(64)   NOT NULL,
    targetsubid       VARCHAR(64)   NOT NULL,
    targetlocid       VARCHAR(64)   NOT NULL,
    session_qualifier VARCHAR(64)   NOT NULL,
    text              VARCHAR(2048) NOT NULL,
    PRIMARY KEY(time, beginstring)

);

CREATE TABLE log_outgoing_messages (
    time TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    beginstring       VARCHAR(8)    NOT NULL,
    sendercompid      VARCHAR(64)   NOT NULL,
    sendersubid       VARCHAR(64)   NOT NULL,
    senderlocid       VARCHAR(64)   NOT NULL,
    targetcompid      VARCHAR(64)   NOT NULL,
    targetsubid       VARCHAR(64)   NOT NULL,
    targetlocid       VARCHAR(64)   NOT NULL,
    session_qualifier VARCHAR(64)   NOT NULL,
    text              VARCHAR(2048) NOT NULL,
    PRIMARY KEY(time, beginstring)

);

CREATE TABLE log_events (
    time TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    beginstring       VARCHAR(8)    NOT NULL,
    sendercompid      VARCHAR(64)   NOT NULL,
    sendersubid       VARCHAR(64)   NOT NULL,
    senderlocid       VARCHAR(64)   NOT NULL,
    targetcompid      VARCHAR(64)   NOT NULL,
    targetsubid       VARCHAR(64)   NOT NULL,
    targetlocid       VARCHAR(64)   NOT NULL,
    session_qualifier VARCHAR(64),
    text              VARCHAR(2048) NOT NULL,
    PRIMARY KEY(time, beginstring)
);

CREATE TABLE sessions (
    beginstring       VARCHAR(8)  NOT NULL,
    sendercompid      VARCHAR(64) NOT NULL,
    sendersubid       VARCHAR(64) NOT NULL,
    senderlocid       VARCHAR(64) NOT NULL,
    targetcompid      VARCHAR(64) NOT NULL,
    targetsubid       VARCHAR(64) NOT NULL,
    targetlocid       VARCHAR(64) NOT NULL,
    session_qualifier VARCHAR(64) NOT NULL,
    creation_time     TIMESTAMP   NOT NULL,
    incoming_seqnum   INT         NOT NULL,
    outgoing_seqnum   INT         NOT NULL,
    PRIMARY KEY (creation_time, beginstring)
);