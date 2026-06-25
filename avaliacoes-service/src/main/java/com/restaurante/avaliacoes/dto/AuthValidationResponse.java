package com.restaurante.avaliacoes.dto;

public record AuthValidationResponse(
        boolean valido,
        Long id,
        String nome,
        String email
) {
}

