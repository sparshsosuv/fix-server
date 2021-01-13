

DROP TABLE IF EXISTS fix_message;

CREATE TABLE fix_message (
  id INT AUTO_INCREMENT  PRIMARY KEY,
  created_at DATE NOT NULL,
  message CLOB NOT NULL
);