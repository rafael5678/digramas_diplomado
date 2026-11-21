# ADR-002 — Adaptador del legado

El legado no se modifica. Solo Historial conoce el texto que llega por SOAP.

## Mapa de traducción

La línea del legado es `id|resumen`. Entra al modelo nuestro como `HistorialConsulta`: el resumen y la frescura. El id del legado no cruza el puerto.

El adaptador lo traduce a un resumen nuestro y lo guarda con fecha de vigencia. Si el legado no responde, se devuelve la última copia y la cabecera `X-Data-Freshness: VENCIDO`. La API no responde 500 por una falla del sistema viejo.

Cambiar el proveedor del legado toca solo el paquete `com.redsalud.andina.historial`.
