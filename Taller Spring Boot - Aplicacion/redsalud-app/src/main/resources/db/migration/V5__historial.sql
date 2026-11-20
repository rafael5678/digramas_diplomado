CREATE SCHEMA IF NOT EXISTS historial;
CREATE TABLE historial.historial_lectura (
  paciente_id VARCHAR(64) PRIMARY KEY,
  resumen VARCHAR(2000) NOT NULL,
  fresca_hasta TIMESTAMP NOT NULL
);
