package com.POO.Sistema_De_Bar.dto;

import java.math.BigDecimal;

public record ConfiguracaoDTO(
        BigDecimal valorCouvert,
        BigDecimal percentualGorjetaComida,
        BigDecimal percentualGorjetaBebida
) {}