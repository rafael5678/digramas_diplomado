CREATE SCHEMA IF NOT EXISTS sincronizacion;

CREATE TABLE sincronizacion.llave (
  clave VARCHAR(128) PRIMARY KEY,
  codigo INT NOT NULL,
  respuesta VARCHAR(2000) NOT NULL
);

CREATE TABLE teleconsulta.outbox (
  id VARCHAR(64) PRIMARY KEY,
  evento_id VARCHAR(64) NOT NULL,
  episodio_id VARCHAR(64) NOT NULL,
  estado VARCHAR(32) NOT NULL
);
