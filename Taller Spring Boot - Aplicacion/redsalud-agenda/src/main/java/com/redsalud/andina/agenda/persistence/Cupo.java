package com.redsalud.andina.agenda.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

@Entity
@Table(schema = "agenda", name = "cupo")
public class Cupo {

    @Id
    private String id;

    @Column(nullable = false)
    private String especialista;

    @Column(nullable = false)
    private boolean reservado;

    @Version
    private long version;

    protected Cupo() {
    }

    public Cupo(String id, String especialista) {
        this.id = id;
        this.especialista = especialista;
    }

    public void reservar() {
        this.reservado = true;
    }

    public boolean isReservado() {
        return reservado;
    }

    public String getId() {
        return id;
    }
}
