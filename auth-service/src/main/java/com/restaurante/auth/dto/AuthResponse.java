package com.restaurante.auth.dto;

public record AuthResponse(
        Long id,
        String nome,
        String email,
        String token,
        String mensagem
) {
}

