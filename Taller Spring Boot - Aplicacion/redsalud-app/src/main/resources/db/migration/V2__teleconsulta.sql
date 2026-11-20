CREATE SCHEMA IF NOT EXISTS teleconsulta;
CREATE TABLE teleconsulta.episodio (
  id VARCHAR(64) PRIMARY KEY,
  cupo_id VARCHAR(64) NOT NULL,
  cerrado BOOLEAN NOT NULL,
  version BIGINT NOT NULL
);
