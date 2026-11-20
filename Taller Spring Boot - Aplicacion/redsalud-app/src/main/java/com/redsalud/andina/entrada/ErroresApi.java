package com.redsalud.andina.entrada;

import com.redsalud.andina.acceso.application.AccesoDenegado;
import com.redsalud.andina.agenda.application.CupoYaReservado;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ErroresApi {

    @ExceptionHandler(AccesoDenegado.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public Map<String, String> acceso(AccesoDenegado ex) {
        return Map.of("error", ex.getMessage());
    }

    @ExceptionHandler(CupoYaReservado.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Map<String, String> cupo(CupoYaReservado ex) {
        return Map.of("error", ex.getMessage());
    }
}
