# RedSalud Andina — esqueleto Spring Boot

Un solo proceso. Nueve módulos de negocio y una API que es la única entrada. El centro no llama a un módulo directo.

| Módulo | Qué guarda |
| --- | --- |
| Acceso | El rol de quien entra |
| Agenda | El cupo de los 9 especialistas |
| Interconsulta | El estado solicitada, agendada, atendida o cerrada |
| Teleconsulta | La visita y su cierre |
| Receta | La receta firmada |
| Sincronización | La cola del centro, sin duplicar envíos |
| Historial | La copia del legado y su fecha de vigencia |
| Auditoría | Quién vio un documento |
| Reportes | Las cifras, a partir de eventos |

Historial es la frontera con el legado: puerto `HistorialClinicoPort`, traducción al modelo nuestro y degradación. La llamada al SOAP espera como máximo 2 segundos y, tras tres fallos, el circuito deja de llamarlo. Si el SOAP no responde, la API sigue contestando con la última copia y la cabecera `X-Data-Freshness`.

La receta firmada no sale directo a farmacia. Se escribe en `receta.outbox` en la misma transacción y un despacho la entrega a la cola. Cuando la cola la recibe, la fila se borra: el outbox solo transporta.

`POST /api/sync` guarda la visita del centro. La llave queda en la tabla `sincronizacion.llave`, no en memoria: el mismo lote responde 201 la primera vez y 200 con la misma respuesta después, aunque el proceso haya reiniciado. El episodio entra en Teleconsulta y el evento sale por `teleconsulta.outbox`. El cupo y la receta firmada no se confirman en esa cola.

## Cómo correrlo

Hace falta Java 21.

```
mvn test
mvn -pl redsalud-app spring-boot:run
```

La base de este esqueleto es H2 en memoria, con un esquema por módulo. En el despliegue real esos esquemas van en Aurora, en una sola región.

## Entradas

- `POST /api/agenda/cupos/{id}/reservas`
- `POST /api/teleconsulta/episodios/{id}/cierre`
- `POST /api/recetas`
- `POST /api/sync`
- `GET /api/historial/{pacienteId}`
