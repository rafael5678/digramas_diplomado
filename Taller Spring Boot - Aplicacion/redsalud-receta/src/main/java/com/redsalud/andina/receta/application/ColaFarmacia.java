package com.redsalud.andina.receta.application;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.springframework.stereotype.Component;

@Component
public class ColaFarmacia {

    private final List<String> mensajes = new CopyOnWriteArrayList<>();

    public void enviar(String cuerpo) {
        mensajes.add(cuerpo);
    }

    public List<String> mensajes() {
        return List.copyOf(mensajes);
    }
}
