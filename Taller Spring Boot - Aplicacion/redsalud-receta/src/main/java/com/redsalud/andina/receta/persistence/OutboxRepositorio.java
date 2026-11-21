package com.redsalud.andina.receta.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OutboxRepositorio extends JpaRepository<MensajeOutbox, String> {
}
