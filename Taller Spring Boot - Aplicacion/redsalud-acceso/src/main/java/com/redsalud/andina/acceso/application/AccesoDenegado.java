package com.redsalud.andina.acceso.application;

public class AccesoDenegado extends RuntimeException {
    public AccesoDenegado(String mensaje) {
        super(mensaje);
    }
}
