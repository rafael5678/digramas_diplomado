package com.redsalud.andina.historial.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;

@Entity
@Table(schema = "historial", name = "historial_lectura")
public class HistorialLectura {

    @Id
    @Column(name = "paciente_id")
    private String pacienteId;

    @Column(nullable = false, length = 2000)
    private String resumen;

    @Column(name = "fresca_hasta", nullable = false)
    private Instant frescaHasta;

    protected HistorialLectura() {
    }

    public HistorialLectura(String pacienteId, String resumen, Instant frescaHasta) {
        this.pacienteId = pacienteId;
        this.resumen = resumen;
        this.frescaHasta = frescaHasta;
    }

    public String getResumen() {
        return resumen;
    }

    public boolean vigente(Instant ahora) {
        return frescaHasta.isAfter(ahora);
    }
}
