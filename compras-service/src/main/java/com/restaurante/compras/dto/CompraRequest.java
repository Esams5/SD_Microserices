package com.restaurante.compras.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CompraRequest(
        @NotNull Long pratoId,
        @NotBlank String pratoNome,
        @NotNull @Min(1) Integer quantidade,
        @NotNull @DecimalMin("0.0") BigDecimal precoUnitario
) {
}

