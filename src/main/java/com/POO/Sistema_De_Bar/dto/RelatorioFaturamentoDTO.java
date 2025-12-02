package com.POO.Sistema_De_Bar.dto;

import java.math.BigDecimal;

public record RelatorioFaturamentoDTO(
        BigDecimal faturamentoTotal,
        Long qtdPagamentos
) {}