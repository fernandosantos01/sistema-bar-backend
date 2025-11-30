package com.POO.Sistema_De_Bar.dto;

import java.math.BigDecimal;
import java.util.List;

public record ResumoContaDTO(
        List<ItemContaDTO> itens,
        BigDecimal subtotalComida,
        BigDecimal subtotalBebida,
        BigDecimal valorGorjetaComida,
        BigDecimal valorGorjetaBebida,
        BigDecimal valorCouvert,
        BigDecimal totalGeral,
        BigDecimal totalPago,
        BigDecimal saldoRestante,
        String status
) {
}
