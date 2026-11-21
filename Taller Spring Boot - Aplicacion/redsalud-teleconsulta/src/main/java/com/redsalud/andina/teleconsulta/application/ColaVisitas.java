package com.redsalud.andina.teleconsulta.application;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.springframework.stereotype.Component;

@Component
public class ColaVisitas {

    private final List<String> mensajes = new CopyOnWriteArrayList<>();

    public void enviar(String cuerpo) {
        mensajes.add(cuerpo);
    }

    public List<String> tomar() {
        List<String> copia = new ArrayList<>(mensajes);
        mensajes.clear();
        return copia;
    }
}
