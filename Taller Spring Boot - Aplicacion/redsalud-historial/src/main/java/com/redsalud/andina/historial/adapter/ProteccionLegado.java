package com.redsalud.andina.historial.adapter;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProteccionLegado {

    private final LegadoCliente legado;
    private final Duration limite;
    private final int umbral;
    private final Duration pausa;
    private int fallos;
    private Instant abiertoHasta = Instant.EPOCH;

    @Autowired
    public ProteccionLegado(LegadoCliente legado) {
        this(legado, Duration.ofSeconds(2), 3, Duration.ofSeconds(30));
    }

    ProteccionLegado(LegadoCliente legado, Duration limite, int umbral, Duration pausa) {
        this.legado = legado;
        this.limite = limite;
        this.umbral = umbral;
        this.pausa = pausa;
    }

    public String traerResumen(String pacienteId) {
        if (circuitoAbierto()) {
            throw new LegadoNoDisponible();
        }
        try (ExecutorService hilos = Executors.newVirtualThreadPerTaskExecutor()) {
            Future<String> futuro = hilos.submit(() -> legado.traerResumen(pacienteId));
            try {
                String texto = futuro.get(limite.toMillis(), TimeUnit.MILLISECONDS);
                exito();
                return texto;
            } catch (TimeoutException ex) {
                futuro.cancel(true);
                fallo();
                throw new LegadoNoDisponible();
            } catch (ExecutionException ex) {
                fallo();
                if (ex.getCause() instanceof RuntimeException causa) {
                    throw causa;
                }
                throw new LegadoNoDisponible();
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
                fallo();
                throw new LegadoNoDisponible();
            }
        }
    }

    private synchronized boolean circuitoAbierto() {
        return Instant.now().isBefore(abiertoHasta);
    }

    private synchronized void exito() {
        fallos = 0;
        abiertoHasta = Instant.EPOCH;
    }

    private synchronized void fallo() {
        fallos++;
        if (fallos >= umbral) {
            abiertoHasta = Instant.now().plus(pausa);
            fallos = 0;
        }
    }
}
