package com.redsalud.andina.historial.adapter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class TraduccionLegadoTest {

    @Test
    void traduceElTextoDelLegadoAlResumenNuestro() {
        var lectura = TraduccionLegado.aCanonico("p-1", "p-1|Control prenatal");
        assertThat(lectura.getResumen()).isEqualTo("Control prenatal");
    }

    @Test
    void rechazaUnTextoQueNoTraeResumen() {
        assertThatThrownBy(() -> TraduccionLegado.aCanonico("p-1", "sin-separador"))
                .isInstanceOf(FormatoLegadoInvalido.class);
    }
}
