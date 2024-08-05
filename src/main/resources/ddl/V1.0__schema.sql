DROP TABLE IF exists log_income_messages;
DROP TABLE IF exists log_outgoing_messages;
DROP TABLE IF exists log_events;
DROP TABLE IF exists messages;
DROP TABLE IF exists sessions;

CREATE TABLE messages (
    time TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    beginstring       VARCHAR(8)    NOT NULL,
    sendercompid      VARCHAR(256)   NOT NULL,
    sendersubid       VARCHAR(256)   NOT NULL,
    senderlocid       VARCHAR(256)   NOT NULL,
    targetcompid      VARCHAR(256)   NOT NULL,
    targetsubid       VARCHAR(256)   NOT NULL,
    targetlocid       VARCHAR(256)   NOT NULL,
    session_qualifier VARCHAR(256)   NOT NULL,
    msgseqnum         INT           NOT NULL,
    message           VARCHAR(4096) NOT NULL,
    PRIMARY KEY(time, beginstring, sendercompid, targetcompid)
);

CREATE TABLE log_income_messages (
    time TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    beginstring       VARCHAR(8)    NOT NULL,
    sendercompid      VARCHAR(256)   NOT NULL,
    sendersubid       VARCHAR(256)   NOT NULL,
    senderlocid       VARCHAR(256)   NOT NULL,
    targetcompid      VARCHAR(256)   NOT NULL,
    targetsubid       VARCHAR(256)   NOT NULL,
    targetlocid       VARCHAR(256)   NOT NULL,
    session_qualifier VARCHAR(256)   NOT NULL,
    text              VARCHAR(4096) NOT NULL,
    PRIMARY KEY(time, beginstring, sendercompid, targetcompid)

);

CREATE TABLE log_outgoing_messages (
    time TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    beginstring       VARCHAR(8)    NOT NULL,
    sendercompid      VARCHAR(256)   NOT NULL,
    sendersubid       VARCHAR(256)   NOT NULL,
    senderlocid       VARCHAR(256)   NOT NULL,
    targetcompid      VARCHAR(256)   NOT NULL,
    targetsubid       VARCHAR(256)   NOT NULL,
    targetlocid       VARCHAR(256)   NOT NULL,
    session_qualifier VARCHAR(256)   NOT NULL,
    text              VARCHAR(4096) NOT NULL,
    PRIMARY KEY(time, beginstring, sendercompid, targetcompid)

);

CREATE TABLE log_events (
    time TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    beginstring       VARCHAR(8)    NOT NULL,
    sendercompid      VARCHAR(256)   NOT NULL,
    sendersubid       VARCHAR(256)   NOT NULL,
    senderlocid       VARCHAR(256)   NOT NULL,
    targetcompid      VARCHAR(256)   NOT NULL,
    targetsubid       VARCHAR(256)   NOT NULL,
    targetlocid       VARCHAR(256)   NOT NULL,
    session_qualifier VARCHAR(256),
    text              VARCHAR(4096) NOT NULL,
    PRIMARY KEY(time, beginstring, sendercompid, targetcompid)
);

CREATE TABLE sessions (
                          beginstring       VARCHAR(8)  NOT NULL,
                          sendercompid      VARCHAR(256) NOT NULL,
                          sendersubid       VARCHAR(256) NOT NULL,
                          senderlocid       VARCHAR(256) NOT NULL,
                          targetcompid      VARCHAR(256) NOT NULL,
                          targetsubid       VARCHAR(256) NOT NULL,
                          targetlocid       VARCHAR(256) NOT NULL,
                          session_qualifier VARCHAR(256) NOT NULL,
                          creation_time     TIMESTAMP   NOT NULL,
                          incoming_seqnum   INT         NOT NULL,
                          outgoing_seqnum   INT         NOT NULL,
                          incoming_seqnum_edit INT DEFAULT 1,
                          outgoing_seqnum_edit INT DEFAULT 1,
                          is_incoming_edited BOOLEAN,
                          is_outgoing_edited BOOLEAN,
                          PRIMARY KEY(creation_time, beginstring, sendercompid, targetcompid)
);
