package com.redsalud.andina.teleconsulta.application;

import com.redsalud.andina.comun.AccesoClinico;
import com.redsalud.andina.comun.Actor;
import com.redsalud.andina.comun.ConsultaCerrada;
import com.redsalud.andina.comun.ValoresEnConflicto;
import com.redsalud.andina.teleconsulta.persistence.Episodio;
import com.redsalud.andina.teleconsulta.persistence.EpisodioRepositorio;
import com.redsalud.andina.teleconsulta.persistence.VisitaPendiente;
import com.redsalud.andina.teleconsulta.persistence.VisitaPendienteRepositorio;
import java.util.UUID;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TeleconsultaService {

    private final EpisodioRepositorio episodios;
    private final VisitaPendienteRepositorio pendientes;
    private final ApplicationEventPublisher eventos;

    public TeleconsultaService(EpisodioRepositorio episodios, VisitaPendienteRepositorio pendientes,
                               ApplicationEventPublisher eventos) {
        this.episodios = episodios;
        this.pendientes = pendientes;
        this.eventos = eventos;
    }

    @Transactional
    public void registrar(String episodioId, String cupoId) {
        var ya = episodios.findById(episodioId);
        if (ya.isPresent()) {
            if (!ya.get().getCupoId().equals(cupoId)) {
                eventos.publishEvent(new ValoresEnConflicto(episodioId, ya.get().getCupoId(), cupoId));
            }
            return;
        }
        try {
            episodios.saveAndFlush(new Episodio(episodioId, cupoId));
            pendientes.save(new VisitaPendiente(UUID.randomUUID().toString(), episodioId, episodioId));
        } catch (DataIntegrityViolationException ex) {
            // La restricción única del episodio ya ganó. No se inserta otra fila.
        }
    }

    @Transactional
    public void cerrar(String episodioId, String cupoId, Actor actor) {
        Episodio episodio = episodios.findById(episodioId).orElseGet(() -> new Episodio(episodioId, cupoId));
        episodio.cerrar();
        episodios.save(episodio);
        eventos.publishEvent(new ConsultaCerrada(episodioId, cupoId));
        eventos.publishEvent(new AccesoClinico(actor.id(), "episodio:" + episodioId, "cierre", true));
    }
}
