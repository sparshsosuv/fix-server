
DROP TABLE IF EXISTS fix_message;

CREATE TABLE fix_message (
  id INT AUTO_INCREMENT  PRIMARY KEY,
  created_at DATE NOT NULL,
  message CLOB NOT NULL
);

DROP TABLE IF EXISTS sessions;

create table sessions (
  beginstring  char(8) not null,
  sendercompid varchar(64) not null,
  sendersubid  varchar(64) not null,
  senderlocid  varchar(64) not null,
  targetcompid varchar(64) not null,
  targetsubid  varchar(64) not null,
  targetlocid  varchar(64) not null,
  session_qualifier varchar(64) not null,
  creation_time timestamp not null,
  incoming_seqnum integer not null,
  outgoing_seqnum integer not null,
  primary key (beginstring, sendercompid, sendersubid, senderlocid,
               targetcompid, targetsubid, targetlocid, session_qualifier)
);

DROP TABLE IF EXISTS messages;

create table messages (
  beginstring char(8) not null,
  sendercompid varchar(64) not null,
  sendersubid varchar(64) not null,
  senderlocid varchar(64) not null,
  targetcompid varchar(64) not null,
  targetsubid varchar(64) not null,
  targetlocid varchar(64) not null,
  session_qualifier varchar(64) not null,
  msgseqnum integer not null,
  message text not null,
  primary key (beginstring, sendercompid, sendersubid, senderlocid,
               targetcompid, targetsubid, targetlocid, session_qualifier,
               msgseqnum)
);

DROP TABLE IF EXISTS log_income;

create table log_income (
  id INT AUTO_INCREMENT  PRIMARY KEY,
  time timestamp not null,
  beginstring char(8) not null,
  sendercompid varchar(64) not null,
  sendersubid varchar(64) not null,
  senderlocid varchar(64) not null,
  targetcompid varchar(64) not null,
  targetsubid varchar(64) not null,
  targetlocid varchar(64) not null,
  session_qualifier varchar(64),
  text text not null
);

DROP TABLE IF EXISTS log_outgoing;

create table log_outgoing (
  id INT AUTO_INCREMENT  PRIMARY KEY,
  time timestamp not null,
  beginstring char(8) not null,
  sendercompid varchar(64) not null,
  sendersubid varchar(64) not null,
  senderlocid varchar(64) not null,
  targetcompid varchar(64) not null,
  targetsubid varchar(64) not null,
  targetlocid varchar(64) not null,
  session_qualifier varchar(64),
  text text not null
);

DROP TABLE IF EXISTS log_event;
create table log_event (
  id INT AUTO_INCREMENT  PRIMARY KEY,
  time timestamp not null,
  beginstring char(8) not null,
  sendercompid varchar(64) not null,
  sendersubid varchar(64) not null,
  senderlocid varchar(64) not null,
  targetcompid varchar(64) not null,
  targetsubid varchar(64) not null,
  targetlocid varchar(64) not null,
  session_qualifier varchar(64),
  text text not null
);
