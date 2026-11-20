package com.redsalud.andina.interconsulta.application;

import com.redsalud.andina.comun.ConsultaCerrada;
import com.redsalud.andina.comun.CupoReservado;
import com.redsalud.andina.interconsulta.persistence.Interconsulta;
import com.redsalud.andina.interconsulta.persistence.InterconsultaRepositorio;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Service
public class InterconsultaService {

    private final InterconsultaRepositorio repositorio;

    public InterconsultaService(InterconsultaRepositorio repositorio) {
        this.repositorio = repositorio;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void alReservar(CupoReservado evento) {
        Interconsulta actual = repositorio.findByCupoId(evento.cupoId())
                .orElseGet(() -> new Interconsulta(evento.cupoId(), "SOLICITADA"));
        actual.marcar("AGENDADA", evento.cupoId());
        repositorio.save(actual);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void alCerrar(ConsultaCerrada evento) {
        repositorio.findByCupoId(evento.cupoId()).ifPresent(actual -> {
            actual.marcar("ATENDIDA", evento.cupoId());
            repositorio.save(actual);
        });
    }

    public String estadoDe(String cupoId) {
        return repositorio.findByCupoId(cupoId).map(Interconsulta::getEstado).orElse("SOLICITADA");
    }
}
