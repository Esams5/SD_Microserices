package com.restaurante.auth.dto;

public record AuthValidationResponse(
        boolean valido,
        Long id,
        String nome,
        String email
) {
}
