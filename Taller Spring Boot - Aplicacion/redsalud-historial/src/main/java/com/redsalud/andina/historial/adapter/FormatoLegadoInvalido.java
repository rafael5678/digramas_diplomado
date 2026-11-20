package com.redsalud.andina.historial.adapter;

public class FormatoLegadoInvalido extends RuntimeException {
    public FormatoLegadoInvalido(String crudo) {
        super("el legado no trae el formato id|resumen: " + crudo);
    }
}
