package com.redsalud.andina.interconsulta.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

@Entity
@Table(schema = "interconsulta", name = "interconsulta")
public class Interconsulta {

    @Id
    private String id;

    @Column(nullable = false)
    private String estado;

    @Column(name = "cupo_id")
    private String cupoId;

    @Version
    private long version;

    protected Interconsulta() {
    }

    public Interconsulta(String id, String estado) {
        this.id = id;
        this.estado = estado;
    }

    public void marcar(String estado, String cupoId) {
        this.estado = estado;
        this.cupoId = cupoId;
    }

    public String getEstado() {
        return estado;
    }

    public String getId() {
        return id;
    }
}
