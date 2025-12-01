package com.POO.Sistema_De_Bar.dto;

import java.math.BigDecimal;

public record ItemContaDTO(
        Long id,
        String nomeProduto,
        BigDecimal precoUnitario,
        Integer quantidade,
        BigDecimal totalItem,
        String categoria
) {
}
