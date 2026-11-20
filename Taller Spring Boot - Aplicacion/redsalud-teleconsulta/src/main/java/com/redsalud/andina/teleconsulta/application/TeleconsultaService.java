package com.redsalud.andina.teleconsulta.application;

import com.redsalud.andina.comun.AccesoClinico;
import com.redsalud.andina.comun.Actor;
import com.redsalud.andina.comun.ConsultaCerrada;
import com.redsalud.andina.teleconsulta.persistence.Episodio;
import com.redsalud.andina.teleconsulta.persistence.EpisodioRepositorio;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TeleconsultaService {

    private final EpisodioRepositorio episodios;
    private final ApplicationEventPublisher eventos;

    public TeleconsultaService(EpisodioRepositorio episodios, ApplicationEventPublisher eventos) {
        this.episodios = episodios;
        this.eventos = eventos;
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
