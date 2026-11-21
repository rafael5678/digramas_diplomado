package com.redsalud.andina.sincronizacion.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(schema = "sincronizacion", name = "llave")
public class LlaveGuardada {

    @Id
    private String clave;

    @Column(nullable = false)
    private int codigo;

    @Column(nullable = false, length = 2000)
    private String respuesta;

    protected LlaveGuardada() {
    }

    public LlaveGuardada(String clave, int codigo, String respuesta) {
        this.clave = clave;
        this.codigo = codigo;
        this.respuesta = respuesta;
    }

    public int getCodigo() {
        return codigo;
    }

    public String getRespuesta() {
        return respuesta;
    }
}
