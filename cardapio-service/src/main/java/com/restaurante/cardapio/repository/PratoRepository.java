package com.restaurante.cardapio.repository;

import com.restaurante.cardapio.model.Prato;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PratoRepository extends JpaRepository<Prato, Long> {
}

