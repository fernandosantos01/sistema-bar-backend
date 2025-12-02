package com.POO.Sistema_De_Bar.dto;

import java.math.BigDecimal;

public record RelatorioItemDTO(
        String nomeProduto,
        Long quantidadeVendida,
        BigDecimal totalFaturado
) {}