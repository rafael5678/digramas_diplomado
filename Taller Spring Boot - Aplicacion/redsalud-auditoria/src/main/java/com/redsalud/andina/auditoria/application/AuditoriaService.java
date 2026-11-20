package com.redsalud.andina.auditoria.application;

import com.redsalud.andina.comun.AccesoClinico;
import java.util.ArrayList;
import java.util.List;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
public class AuditoriaService {

    private final List<AccesoClinico> registros = new ArrayList<>();

    @EventListener
    public void registrar(AccesoClinico evento) {
        registros.add(evento);
    }

    public int cantidad() {
        return registros.size();
    }
}
