package com.POO.Sistema_De_Bar.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "itens_comanda")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ItemComandaModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "comanda_id", nullable = false)
    private ComandaModel comanda;

    @ManyToOne
    @JoinColumn(name = "produto_id", nullable = false)
    private ProdutoModel produto;

    @Column(nullable = false)
    private Integer quantidade;

    @Column(nullable = false)
    private BigDecimal precoUnitario;

    private LocalDateTime dataHoraPedido = LocalDateTime.now();

    private boolean cancelado = false;

    private String motivoCancelamento;
}
