CREATE SCHEMA IF NOT EXISTS interconsulta;
CREATE TABLE interconsulta.interconsulta (
  id VARCHAR(64) PRIMARY KEY,
  estado VARCHAR(40) NOT NULL,
  cupo_id VARCHAR(64),
  version BIGINT NOT NULL
);
