package com.restaurante.auth.repository;

import com.restaurante.auth.model.Sessao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SessaoRepository extends JpaRepository<Sessao, Long> {
    Optional<Sessao> findByToken(String token);
}

