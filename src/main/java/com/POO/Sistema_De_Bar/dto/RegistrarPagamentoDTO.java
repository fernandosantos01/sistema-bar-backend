package com.POO.Sistema_De_Bar.dto;

import java.math.BigDecimal;

public record RegistrarPagamentoDTO(
        BigDecimal valor,
        String formaPagamento
) {}