package com.redsalud.andina.receta.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

@Entity
@Table(schema = "receta", name = "receta")
public class Receta {

    @Id
    private String id;

    @Column(name = "episodio_id", nullable = false)
    private String episodioId;

    @Column(nullable = false)
    private boolean firmada;

    @Version
    private long version;

    protected Receta() {
    }

    public Receta(String id, String episodioId) {
        this.id = id;
        this.episodioId = episodioId;
        this.firmada = true;
    }

    public String getId() {
        return id;
    }
}
