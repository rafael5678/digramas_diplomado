package com.redsalud.andina.historial.adapter;

public class LegadoNoDisponible extends RuntimeException {
    public LegadoNoDisponible() {
        super("el legado no respondió");
    }
}
