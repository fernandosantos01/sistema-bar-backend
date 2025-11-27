package com.POO.Sistema_De_Bar.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "configuracoes")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ConfiguracaoModel {
    @Id
    @GeneratedValue
    private UUID id;

    private BigDecimal valorCovert;
    private BigDecimal pecentualGorjetaComida;
    private BigDecimal pecentualGorjetaBebida;


}
