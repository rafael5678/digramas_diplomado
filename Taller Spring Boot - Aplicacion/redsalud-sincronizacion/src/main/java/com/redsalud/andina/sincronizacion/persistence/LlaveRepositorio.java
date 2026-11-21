package com.redsalud.andina.sincronizacion.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LlaveRepositorio extends JpaRepository<LlaveGuardada, String> {
}
