package com.redsalud.andina.receta.application;

import com.redsalud.andina.receta.persistence.MensajeOutbox;
import com.redsalud.andina.receta.persistence.OutboxRepositorio;
import java.util.List;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DespachoOutbox {

    private final OutboxRepositorio salida;
    private final ColaFarmacia farmacia;

    public DespachoOutbox(OutboxRepositorio salida, ColaFarmacia farmacia) {
        this.salida = salida;
        this.farmacia = farmacia;
    }

    @Scheduled(fixedDelayString = "${redsalud.outbox.pausa:5000}")
    @Transactional
    public int despachar() {
        List<MensajeOutbox> pendientes = salida.findAll();
        for (MensajeOutbox mensaje : pendientes) {
            farmacia.enviar(mensaje.getCuerpo());
        }
        salida.deleteAll(pendientes);
        return pendientes.size();
    }
}
