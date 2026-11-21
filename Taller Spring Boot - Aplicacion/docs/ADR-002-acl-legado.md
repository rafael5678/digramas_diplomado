# ADR-002 — Adaptador del legado

El legado no se modifica. Solo Historial conoce el texto que llega por SOAP.

## Mapa de traducción

La línea del legado es `id|resumen`. Entra al modelo nuestro como `HistorialConsulta`: el resumen y la frescura. El id del legado no cruza el puerto.

## Degradación

La llamada espera como máximo 2 segundos, el mismo límite del especialista. Tres fallos seguidos abren el circuito 30 segundos y, mientras está abierto, no se vuelve a llamar al SOAP.

Si la copia sigue vigente, la respuesta es `FRESCO`. Si acaba de traducirse, `LEGADO`. Si el legado no responde, se devuelve la última copia y la cabecera `X-Data-Freshness: VENCIDO`. Sin copia, el resumen es «sin copia disponible». La API no responde 500 por una falla del sistema viejo.

Cambiar el proveedor del legado toca solo el paquete `com.redsalud.andina.historial`.
