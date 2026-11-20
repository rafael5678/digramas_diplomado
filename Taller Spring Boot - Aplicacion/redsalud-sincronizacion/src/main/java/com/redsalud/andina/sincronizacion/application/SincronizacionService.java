package com.redsalud.andina.sincronizacion.application;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Service;

@Service
public class SincronizacionService {

    private final Map<String, String> procesados = new ConcurrentHashMap<>();

    public boolean aceptar(String clave, String cuerpo) {
        if (clave == null || clave.isBlank()) {
            throw new IllegalArgumentException("falta la llave de idempotencia");
        }
        return procesados.putIfAbsent(clave, cuerpo == null ? "" : cuerpo) == null;
    }
}
