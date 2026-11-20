package com.redsalud.andina.agenda.application;

import com.redsalud.andina.agenda.persistence.Cupo;
import com.redsalud.andina.agenda.persistence.CupoRepositorio;
import com.redsalud.andina.comun.AccesoClinico;
import com.redsalud.andina.comun.Actor;
import com.redsalud.andina.comun.CupoReservado;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AgendaService {

    private final CupoRepositorio cupos;
    private final ApplicationEventPublisher eventos;

    public AgendaService(CupoRepositorio cupos, ApplicationEventPublisher eventos) {
        this.cupos = cupos;
        this.eventos = eventos;
    }

    @Transactional
    public void reservar(String cupoId, String pacienteId, String centroId, Actor actor) {
        Cupo cupo = cupos.findById(cupoId).orElseGet(() -> cupos.save(new Cupo(cupoId, "especialista")));
        if (cupo.isReservado()) {
            throw new CupoYaReservado(cupoId);
        }
        cupo.reservar();
        eventos.publishEvent(new CupoReservado(cupoId, pacienteId, centroId));
        eventos.publishEvent(new AccesoClinico(actor.id(), "cupo:" + cupoId, "reserva", true));
    }
}
