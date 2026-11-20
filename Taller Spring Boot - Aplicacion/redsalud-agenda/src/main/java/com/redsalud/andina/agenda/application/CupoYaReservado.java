package com.redsalud.andina.agenda.application;

public class CupoYaReservado extends RuntimeException {
    public CupoYaReservado(String cupoId) {
        super("el cupo " + cupoId + " ya fue tomado");
    }
}
