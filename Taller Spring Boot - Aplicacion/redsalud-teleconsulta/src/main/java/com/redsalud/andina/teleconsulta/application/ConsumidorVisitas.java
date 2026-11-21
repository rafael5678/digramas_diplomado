package com.redsalud.andina.teleconsulta.application;

import com.redsalud.andina.comun.AccesoClinico;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
public class ConsumidorVisitas {

    private final ColaVisitas cola;
    private final ApplicationEventPublisher eventos;
    private final Set<String> vistos = ConcurrentHashMap.newKeySet();

    public ConsumidorVisitas(ColaVisitas cola, ApplicationEventPublisher eventos) {
        this.cola = cola;
        this.eventos = eventos;
    }

    public int drenar() {
        int aplicados = 0;
        for (String cuerpo : cola.tomar()) {
            if (aplicar(cuerpo)) {
                aplicados++;
            }
        }
        return aplicados;
    }

    public boolean aplicar(String cuerpo) {
        String[] partes = cuerpo.split("\\|", 2);
        String eventoId = partes[0];
        if (!vistos.add(eventoId)) {
            return false;
        }
        String episodioId = partes.length == 2 ? partes[1] : eventoId;
        eventos.publishEvent(new AccesoClinico("sync", "visita:" + episodioId, "entrega", true));
        return true;
    }
}
