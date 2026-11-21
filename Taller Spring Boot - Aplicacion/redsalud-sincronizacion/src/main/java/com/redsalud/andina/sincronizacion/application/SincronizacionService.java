package com.redsalud.andina.sincronizacion.application;

import com.redsalud.andina.sincronizacion.persistence.LlaveGuardada;
import com.redsalud.andina.sincronizacion.persistence.LlaveRepositorio;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SincronizacionService {

    private final LlaveRepositorio llaves;

    public SincronizacionService(LlaveRepositorio llaves) {
        this.llaves = llaves;
    }

    @Transactional(readOnly = true)
    public Optional<LlaveGuardada> buscar(String clave) {
        if (clave == null || clave.isBlank()) {
            throw new IllegalArgumentException("falta la llave de idempotencia");
        }
        return llaves.findById(clave);
    }

    @Transactional
    public void guardar(String clave, int codigo, String respuesta) {
        if (llaves.existsById(clave)) {
            return;
        }
        llaves.save(new LlaveGuardada(clave, codigo, respuesta));
    }
}
