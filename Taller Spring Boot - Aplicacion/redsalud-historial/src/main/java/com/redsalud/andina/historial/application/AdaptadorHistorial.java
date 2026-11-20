package com.redsalud.andina.historial.application;

import com.redsalud.andina.comun.AccesoClinico;
import com.redsalud.andina.comun.Actor;
import com.redsalud.andina.historial.adapter.LegadoCliente;
import com.redsalud.andina.historial.adapter.TraduccionLegado;
import com.redsalud.andina.historial.api.HistorialClinicoPort;
import com.redsalud.andina.historial.api.HistorialConsulta;
import com.redsalud.andina.historial.persistence.HistorialLectura;
import com.redsalud.andina.historial.persistence.HistorialRepositorio;
import java.time.Instant;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdaptadorHistorial implements HistorialClinicoPort {

    private final HistorialRepositorio copias;
    private final LegadoCliente legado;
    private final ApplicationEventPublisher eventos;

    public AdaptadorHistorial(HistorialRepositorio copias, LegadoCliente legado, ApplicationEventPublisher eventos) {
        this.copias = copias;
        this.legado = legado;
        this.eventos = eventos;
    }

    @Override
    @Transactional
    public HistorialConsulta consultar(String pacienteId) {
        HistorialLectura copia = copias.findById(pacienteId).orElse(null);
        HistorialConsulta consulta;
        if (copia != null && copia.vigente(Instant.now())) {
            consulta = new HistorialConsulta(copia.getResumen(), "FRESCO");
        } else {
            consulta = consultarLegado(pacienteId, copia);
        }
        return consulta;
    }

    public HistorialConsulta consultar(String pacienteId, Actor actor) {
        HistorialConsulta consulta = consultar(pacienteId);
        eventos.publishEvent(new AccesoClinico(actor.id(), "historial:" + pacienteId, "lectura", true));
        return consulta;
    }

    private HistorialConsulta consultarLegado(String pacienteId, HistorialLectura copia) {
        try {
            HistorialLectura traducida = TraduccionLegado.aCanonico(pacienteId, legado.traerResumen(pacienteId));
            copias.save(traducida);
            return new HistorialConsulta(traducida.getResumen(), "LEGADO");
        } catch (RuntimeException ex) {
            if (copia != null) {
                return new HistorialConsulta(copia.getResumen(), "VENCIDO");
            }
            return new HistorialConsulta("sin copia disponible", "VENCIDO");
        }
    }
}
