package com.POO.Sistema_De_Bar.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "comandas")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ComandaModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "mesa_id", nullable = false)
    private MesaModel mesa;

    @Column(nullable = false)
    private Integer qtdePessoas;

    @Column(nullable = false)
    private boolean couvertHabilitado = true;

    @Column(name = "valor_couvert_registrado")
    private BigDecimal valorCouvert;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusComanda status = StatusComanda.ABERTA;

    @Column(nullable = false, updatable = false)
    private LocalDateTime dataAbertura = LocalDateTime.now();

    private LocalDateTime dataFechamento;
}
