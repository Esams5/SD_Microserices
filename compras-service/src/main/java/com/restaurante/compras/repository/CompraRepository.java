package com.restaurante.compras.repository;

import com.restaurante.compras.model.Compra;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CompraRepository extends JpaRepository<Compra, Long> {
    List<Compra> findByUsuarioIdOrderByIdDesc(Long usuarioId);
}
