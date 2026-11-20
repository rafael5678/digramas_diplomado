package com.redsalud.andina.receta.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RecetaRepositorio extends JpaRepository<Receta, String> {
}
