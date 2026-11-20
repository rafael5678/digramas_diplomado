package com.redsalud.andina.receta.application;

import com.redsalud.andina.comun.AccesoClinico;
import com.redsalud.andina.comun.Actor;
import com.redsalud.andina.comun.RecetaFirmada;
import com.redsalud.andina.receta.persistence.Receta;
import com.redsalud.andina.receta.persistence.RecetaRepositorio;
import java.util.UUID;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RecetaService {

    private final RecetaRepositorio recetas;
    private final ApplicationEventPublisher eventos;

    public RecetaService(RecetaRepositorio recetas, ApplicationEventPublisher eventos) {
        this.recetas = recetas;
        this.eventos = eventos;
    }

    @Transactional
    public String firmar(String episodioId, Actor actor) {
        String id = UUID.randomUUID().toString();
        recetas.save(new Receta(id, episodioId));
        eventos.publishEvent(new RecetaFirmada(id, episodioId));
        eventos.publishEvent(new AccesoClinico(actor.id(), "receta:" + id, "firma", true));
        return id;
    }
}
