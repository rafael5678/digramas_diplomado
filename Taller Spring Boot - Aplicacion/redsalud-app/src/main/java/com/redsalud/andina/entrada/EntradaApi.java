package com.redsalud.andina.entrada;

import com.redsalud.andina.acceso.application.AccesoService;
import com.redsalud.andina.agenda.application.AgendaService;
import com.redsalud.andina.comun.Actor;
import com.redsalud.andina.historial.api.HistorialConsulta;
import com.redsalud.andina.historial.application.AdaptadorHistorial;
import com.redsalud.andina.receta.application.RecetaService;
import com.redsalud.andina.sincronizacion.application.SincronizacionService;
import com.redsalud.andina.teleconsulta.application.TeleconsultaService;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class EntradaApi {

    private final AccesoService acceso;
    private final AgendaService agenda;
    private final TeleconsultaService teleconsulta;
    private final RecetaService receta;
    private final SincronizacionService sincronizacion;
    private final AdaptadorHistorial historial;

    public EntradaApi(AccesoService acceso, AgendaService agenda, TeleconsultaService teleconsulta,
                      RecetaService receta, SincronizacionService sincronizacion, AdaptadorHistorial historial) {
        this.acceso = acceso;
        this.agenda = agenda;
        this.teleconsulta = teleconsulta;
        this.receta = receta;
        this.sincronizacion = sincronizacion;
        this.historial = historial;
    }

    @PostMapping("/agenda/cupos/{id}/reservas")
    public Map<String, String> reservar(@PathVariable String id, @RequestBody Map<String, String> cuerpo,
                                        @RequestHeader("X-Actor") String actorId,
                                        @RequestHeader("X-Rol") String rol,
                                        @RequestHeader(value = "X-Centro", required = false) String centro) {
        Actor actor = acceso.resolver(actorId, rol, centro);
        agenda.reservar(id, cuerpo.getOrDefault("pacienteId", ""), centro == null ? "" : centro, actor);
        return Map.of("cupoId", id, "estado", "reservado");
    }

    @PostMapping("/teleconsulta/episodios/{id}/cierre")
    public Map<String, String> cerrar(@PathVariable String id, @RequestBody Map<String, String> cuerpo,
                                      @RequestHeader("X-Actor") String actorId,
                                      @RequestHeader("X-Rol") String rol) {
        Actor actor = acceso.resolver(actorId, rol, "");
        teleconsulta.cerrar(id, cuerpo.getOrDefault("cupoId", ""), actor);
        return Map.of("episodioId", id, "estado", "cerrado");
    }

    @PostMapping("/recetas")
    public Map<String, String> firmar(@RequestBody Map<String, String> cuerpo,
                                      @RequestHeader("X-Actor") String actorId,
                                      @RequestHeader("X-Rol") String rol) {
        Actor actor = acceso.resolver(actorId, rol, "");
        String id = receta.firmar(cuerpo.getOrDefault("episodioId", ""), actor);
        return Map.of("recetaId", id, "estado", "firmada");
    }

    @PostMapping("/sync")
    public Map<String, Object> sincronizar(@RequestHeader("Idempotency-Key") String clave,
                                           @RequestBody(required = false) String cuerpo) {
        boolean primeraVez = sincronizacion.aceptar(clave, cuerpo);
        return Map.of("aceptado", true, "primeraVez", primeraVez);
    }

    @GetMapping("/historial/{pacienteId}")
    public ResponseEntity<Map<String, String>> historial(@PathVariable String pacienteId,
                                                         @RequestHeader("X-Actor") String actorId,
                                                         @RequestHeader("X-Rol") String rol) {
        Actor actor = acceso.resolver(actorId, rol, "");
        HistorialConsulta consulta = historial.consultar(pacienteId, actor);
        return ResponseEntity.ok()
                .header("X-Data-Freshness", consulta.frescura())
                .body(Map.of("pacienteId", pacienteId, "resumen", consulta.resumen()));
    }
}
