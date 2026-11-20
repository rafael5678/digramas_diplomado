package com.redsalud.andina.historial.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface HistorialRepositorio extends JpaRepository<HistorialLectura, String> {
}
