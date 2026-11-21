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
