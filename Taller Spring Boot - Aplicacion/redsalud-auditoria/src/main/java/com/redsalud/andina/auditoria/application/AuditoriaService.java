package com.redsalud.andina.auditoria.application;

import com.redsalud.andina.comun.AccesoClinico;
import com.redsalud.andina.comun.ValoresEnConflicto;
import java.util.ArrayList;
import java.util.List;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
public class AuditoriaService {

    private final List<AccesoClinico> registros = new ArrayList<>();
    private final List<ValoresEnConflicto> conflictos = new ArrayList<>();

    @EventListener
    public void registrar(AccesoClinico evento) {
        registros.add(evento);
    }

    @EventListener
    public void registrar(ValoresEnConflicto evento) {
        conflictos.add(evento);
    }

    public int cantidad() {
        return registros.size();
    }

    public List<ValoresEnConflicto> conflictos() {
        return List.copyOf(conflictos);
    }
}
