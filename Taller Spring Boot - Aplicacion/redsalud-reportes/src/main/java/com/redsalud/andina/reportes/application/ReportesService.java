package com.redsalud.andina.reportes.application;

import com.redsalud.andina.comun.CupoReservado;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
public class ReportesService {

    private int cuposReservados;

    @EventListener
    public void alReservar(CupoReservado evento) {
        cuposReservados++;
    }

    public int cuposReservados() {
        return cuposReservados;
    }
}
