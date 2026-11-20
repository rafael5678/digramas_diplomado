package com.redsalud.andina.historial.adapter;

import com.redsalud.andina.historial.persistence.HistorialLectura;
import java.time.Duration;
import java.time.Instant;

public final class TraduccionLegado {

    private TraduccionLegado() {
    }

    public static HistorialLectura aCanonico(String pacienteId, String crudo) {
        String[] partes = crudo.split("\\|", 2);
        if (partes.length != 2 || partes[1].isBlank()) {
            throw new FormatoLegadoInvalido(crudo);
        }
        return new HistorialLectura(pacienteId, partes[1].trim(), Instant.now().plus(Duration.ofHours(12)));
    }
}
