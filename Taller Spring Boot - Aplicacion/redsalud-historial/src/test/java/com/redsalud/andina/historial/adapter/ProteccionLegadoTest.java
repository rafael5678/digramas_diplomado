package com.redsalud.andina.historial.adapter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.Duration;
import org.junit.jupiter.api.Test;

class ProteccionLegadoTest {

    @Test
    void siElLegadoTardaMasDelLimiteNoEspera() {
        LegadoCliente lento = pacienteId -> {
            try {
                Thread.sleep(5_000);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
            return pacienteId + "|tarde";
        };
        ProteccionLegado proteccion = new ProteccionLegado(lento, Duration.ofMillis(200), 3, Duration.ofSeconds(30));

        assertThatThrownBy(() -> proteccion.traerResumen("p-1")).isInstanceOf(LegadoNoDisponible.class);
    }

    @Test
    void trasTresFallosElCircuitoNoVuelveALlamar() {
        int[] llamadas = {0};
        LegadoCliente caido = pacienteId -> {
            llamadas[0]++;
            throw new LegadoNoDisponible();
        };
        ProteccionLegado proteccion = new ProteccionLegado(caido, Duration.ofSeconds(2), 3, Duration.ofSeconds(30));

        for (int i = 0; i < 3; i++) {
            assertThatThrownBy(() -> proteccion.traerResumen("p-1")).isInstanceOf(LegadoNoDisponible.class);
        }
        assertThatThrownBy(() -> proteccion.traerResumen("p-1")).isInstanceOf(LegadoNoDisponible.class);
        assertThat(llamadas[0]).isEqualTo(3);
    }
}
