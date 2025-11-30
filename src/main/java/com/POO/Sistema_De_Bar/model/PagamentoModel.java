package com.POO.Sistema_De_Bar.model;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "pagamentos")
public class PagamentoModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "comanda_id", nullable = false)
    private ComandaModel comanda;

    @Column(nullable = false)
    private BigDecimal valor;

    private LocalDateTime dataPagamento = LocalDateTime.now();

    private String formaPagamento;
}