package com.redsalud.andina.receta.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;

@Entity
@Table(schema = "receta", name = "outbox")
public class MensajeOutbox {

    @Id
    private String id;

    @Column(nullable = false)
    private String tipo;

    @Column(nullable = false, length = 2000)
    private String cuerpo;

    @Column(nullable = false)
    private String estado;

    @Column(name = "creado_en", nullable = false)
    private Instant creadoEn;

    protected MensajeOutbox() {
    }

    public MensajeOutbox(String id, String tipo, String cuerpo, Instant creadoEn) {
        this.id = id;
        this.tipo = tipo;
        this.cuerpo = cuerpo;
        this.estado = "PENDIENTE";
        this.creadoEn = creadoEn;
    }

    public String getCuerpo() {
        return cuerpo;
    }
}
