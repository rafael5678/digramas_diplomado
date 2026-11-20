package com.redsalud.andina.historial.adapter;

import org.springframework.stereotype.Component;

@Component
public class LegadoSoapSimulado implements LegadoCliente {

    private boolean caido;

    public void caido(boolean caido) {
        this.caido = caido;
    }

    @Override
    public String traerResumen(String pacienteId) {
        if (caido) {
            throw new LegadoNoDisponible();
        }
        return pacienteId + "|Historia disponible en el legado";
    }
}
