package com.redsalud.andina.interconsulta.persistence;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InterconsultaRepositorio extends JpaRepository<Interconsulta, String> {
    Optional<Interconsulta> findByCupoId(String cupoId);
}
