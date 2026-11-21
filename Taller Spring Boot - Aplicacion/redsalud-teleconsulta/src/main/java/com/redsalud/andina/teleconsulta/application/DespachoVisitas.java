package com.redsalud.andina.teleconsulta.application;

import com.redsalud.andina.teleconsulta.persistence.VisitaPendiente;
import com.redsalud.andina.teleconsulta.persistence.VisitaPendienteRepositorio;
import java.util.List;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DespachoVisitas {

    private final VisitaPendienteRepositorio salida;
    private final ColaVisitas cola;

    public DespachoVisitas(VisitaPendienteRepositorio salida, ColaVisitas cola) {
        this.salida = salida;
        this.cola = cola;
    }

    @Scheduled(fixedDelayString = "${redsalud.outbox.pausa:5000}")
    @Transactional
    public int despachar() {
        List<VisitaPendiente> pendientes = salida.findAll();
        for (VisitaPendiente visita : pendientes) {
            cola.enviar(visita.getEventoId() + "|" + visita.getEpisodioId());
        }
        salida.deleteAll(pendientes);
        return pendientes.size();
    }
}
