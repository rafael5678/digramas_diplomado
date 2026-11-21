package com.redsalud.andina;

import static org.assertj.core.api.Assertions.assertThat;

import com.redsalud.andina.auditoria.application.AuditoriaService;
import com.redsalud.andina.historial.adapter.LegadoSoapSimulado;
import com.redsalud.andina.interconsulta.application.InterconsultaService;
import com.redsalud.andina.receta.application.ColaFarmacia;
import com.redsalud.andina.receta.application.DespachoOutbox;
import com.redsalud.andina.teleconsulta.application.ConsumidorVisitas;
import com.redsalud.andina.teleconsulta.application.DespachoVisitas;
import com.redsalud.andina.teleconsulta.persistence.EpisodioRepositorio;
import org.springframework.web.client.HttpClientErrorException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class EsqueletoTest {

    @LocalServerPort
    int puerto;

    @Autowired
    InterconsultaService interconsulta;

    @Autowired
    AuditoriaService auditoria;

    @Autowired
    LegadoSoapSimulado legado;

    @Autowired
    DespachoOutbox despacho;

    @Autowired
    ColaFarmacia farmacia;

    @Autowired
    EpisodioRepositorio episodios;

    @Autowired
    DespachoVisitas despachoVisitas;

    @Autowired
    ConsumidorVisitas consumidor;

    @Test
    void reservarUnCupoPasaLaInterconsultaAAgendada() {
        post("/api/agenda/cupos/cupo-1/reservas", "{\"pacienteId\":\"p-1\"}");
        assertThat(interconsulta.estadoDe("cupo-1")).isEqualTo("AGENDADA");
        assertThat(auditoria.cantidad()).isPositive();
    }

    @Test
    void siElLegadoCaeElHistorialSigueRespondiendo() {
        legado.caido(true);
        ResponseEntity<String> respuesta = intercambio("/api/historial/p-9", HttpMethod.GET, null);
        assertThat(respuesta.getStatusCode().value()).isEqualTo(200);
        assertThat(respuesta.getHeaders().getFirst("X-Data-Freshness")).isEqualTo("VENCIDO");
        assertThat(respuesta.getBody()).contains("sin copia disponible");
    }

    @Test
    void laRecetaFirmadaSalePorElOutboxYSeBorra() {
        intercambio("/api/recetas", HttpMethod.POST, "{\"episodioId\":\"ep-1\"}");
        assertThat(despacho.despachar()).isEqualTo(1);
        assertThat(farmacia.mensajes()).anyMatch(mensaje -> mensaje.endsWith("|ep-1"));
        assertThat(despacho.despachar()).isZero();
    }

    @Test
    void elMismoEnvioDeLaColaNoSeProcesaDosVeces() {
        long antes = episodios.count();
        HttpHeaders headers = headers();
        headers.set("Idempotency-Key", "lote-1");
        RestTemplate http = new RestTemplate();
        String url = "http://localhost:" + puerto + "/api/sync";
        ResponseEntity<String> primera = http.exchange(url, HttpMethod.POST, new HttpEntity<>("{}", headers), String.class);
        ResponseEntity<String> segunda = http.exchange(url, HttpMethod.POST, new HttpEntity<>("{}", headers), String.class);
        assertThat(primera.getStatusCode().value()).isEqualTo(201);
        assertThat(segunda.getStatusCode().value()).isEqualTo(200);
        assertThat(segunda.getBody()).isEqualTo(primera.getBody());
        assertThat(episodios.count()).isEqualTo(antes + 1);
        assertThat(despachoVisitas.despachar()).isPositive();
        consumidor.drenar();
        assertThat(consumidor.aplicar("lote-1|lote-1")).isFalse();
    }

    @Test
    void unEpisodioRepetidoGuardaLosDosValoresEnAuditoria() {
        enviar("visita-1", "{\"episodioId\":\"ep-visita\",\"cupoId\":\"c-1\"}");
        enviar("visita-2", "{\"episodioId\":\"ep-visita\",\"cupoId\":\"c-2\"}");
        assertThat(auditoria.conflictos())
                .anyMatch(conflicto -> conflicto.guardado().equals("c-1") && conflicto.llegado().equals("c-2"));
    }

    @Test
    void elCupoYLaRecetaNoSeConfirmanEnLaCola() {
        HttpHeaders headers = headers();
        headers.set("Idempotency-Key", "lote-fuerte");
        RestTemplate http = new RestTemplate();
        String url = "http://localhost:" + puerto + "/api/sync";
        try {
            http.exchange(url, HttpMethod.POST, new HttpEntity<>("{\"tipo\":\"receta\"}", headers), String.class);
        } catch (HttpClientErrorException ex) {
            assertThat(ex.getStatusCode().value()).isEqualTo(409);
            assertThat(ex.getResponseBodyAsString()).contains("se confirman en el servidor");
            return;
        }
        throw new AssertionError("la cola aceptó un dato de consistencia fuerte");
    }

    private void enviar(String clave, String cuerpo) {
        HttpHeaders headers = headers();
        headers.set("Idempotency-Key", clave);
        new RestTemplate().exchange("http://localhost:" + puerto + "/api/sync", HttpMethod.POST, new HttpEntity<>(cuerpo, headers), String.class);
    }

    private void post(String ruta, String cuerpo) {
        intercambio(ruta, HttpMethod.POST, cuerpo);
    }

    private ResponseEntity<String> intercambio(String ruta, HttpMethod metodo, String cuerpo) {
        RestTemplate http = new RestTemplate();
        return http.exchange("http://localhost:" + puerto + ruta, metodo, new HttpEntity<>(cuerpo, headers()), String.class);
    }

    private HttpHeaders headers() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Actor", "medico-1");
        headers.set("X-Rol", "MEDICO_RURAL");
        headers.set("X-Centro", "centro-3");
        headers.set("Content-Type", "application/json");
        return headers;
    }
}
