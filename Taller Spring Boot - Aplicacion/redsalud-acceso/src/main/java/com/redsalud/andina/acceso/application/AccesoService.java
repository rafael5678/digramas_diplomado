package com.redsalud.andina.acceso.application;

import com.redsalud.andina.comun.Actor;
import org.springframework.stereotype.Service;

@Service
public class AccesoService {

    public Actor resolver(String actorId, String rol, String centroId) {
        if (actorId == null || actorId.isBlank()) {
            throw new AccesoDenegado("falta quien entra");
        }
        if (rol == null || rol.isBlank()) {
            throw new AccesoDenegado("falta el rol");
        }
        return new Actor(actorId, rol, centroId == null ? "" : centroId);
    }
}
