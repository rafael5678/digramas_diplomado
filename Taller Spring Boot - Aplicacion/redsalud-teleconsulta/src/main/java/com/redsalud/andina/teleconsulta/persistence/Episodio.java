package com.redsalud.andina.teleconsulta.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

@Entity
@Table(schema = "teleconsulta", name = "episodio")
public class Episodio {

    @Id
    private String id;

    @Column(name = "cupo_id", nullable = false)
    private String cupoId;

    @Column(nullable = false)
    private boolean cerrado;

    @Version
    private long version;

    protected Episodio() {
    }

    public Episodio(String id, String cupoId) {
        this.id = id;
        this.cupoId = cupoId;
    }

    public void cerrar() {
        this.cerrado = true;
    }

    public boolean isCerrado() {
        return cerrado;
    }

    public String getCupoId() {
        return cupoId;
    }
}
