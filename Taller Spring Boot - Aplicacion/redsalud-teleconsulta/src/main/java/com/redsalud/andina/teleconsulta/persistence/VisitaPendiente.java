package com.redsalud.andina.teleconsulta.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(schema = "teleconsulta", name = "outbox")
public class VisitaPendiente {

    @Id
    private String id;

    @Column(name = "evento_id", nullable = false)
    private String eventoId;

    @Column(name = "episodio_id", nullable = false)
    private String episodioId;

    @Column(nullable = false)
    private String estado;

    protected VisitaPendiente() {
    }

    public VisitaPendiente(String id, String eventoId, String episodioId) {
        this.id = id;
        this.eventoId = eventoId;
        this.episodioId = episodioId;
        this.estado = "PENDIENTE";
    }

    public String getEventoId() {
        return eventoId;
    }

    public String getEpisodioId() {
        return episodioId;
    }
}
