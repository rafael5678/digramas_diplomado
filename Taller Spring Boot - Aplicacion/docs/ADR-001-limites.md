# ADR-001 — Límites de módulo

Los nueve módulos compilan dentro de un solo Spring Boot. El equipo es pequeño y el caso pide reutilizar el dominio.

Cada módulo escribe solo su dato. Entre ellos circulan eventos, no tablas. La API no tiene esquema: recibe la petición y la entrega al dueño.

Auditoría sigue siendo la primera candidata a salir del proceso, porque su archivo no vive en Aurora.
