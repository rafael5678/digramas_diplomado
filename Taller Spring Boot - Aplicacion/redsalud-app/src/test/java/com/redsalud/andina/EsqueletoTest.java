package com.redsalud.andina;

import static org.assertj.core.api.Assertions.assertThat;

import com.redsalud.andina.auditoria.application.AuditoriaService;
import com.redsalud.andina.historial.adapter.LegadoSoapSimulado;
import com.redsalud.andina.interconsulta.application.InterconsultaService;
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
    void elMismoEnvioDeLaColaNoSeProcesaDosVeces() {
        HttpHeaders headers = headers();
        headers.set("Idempotency-Key", "lote-1");
        RestTemplate http = new RestTemplate();
        String url = "http://localhost:" + puerto + "/api/sync";
        ResponseEntity<String> primera = http.exchange(url, HttpMethod.POST, new HttpEntity<>("{}", headers), String.class);
        ResponseEntity<String> segunda = http.exchange(url, HttpMethod.POST, new HttpEntity<>("{}", headers), String.class);
        assertThat(primera.getBody()).contains("\"primeraVez\":true");
        assertThat(segunda.getBody()).contains("\"primeraVez\":false");
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
