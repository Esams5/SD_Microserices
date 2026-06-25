package com.restaurante.compras.dto;

public record AuthValidationResponse(
        boolean valido,
        Long id,
        String nome,
        String email
) {
}

