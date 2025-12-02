package com.POO.Sistema_De_Bar.repository;

import com.POO.Sistema_De_Bar.model.PagamentoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface PagamentoRepository extends JpaRepository<PagamentoModel, Long> {
    List<PagamentoModel> findByComandaId(Long comandaId);

    @Query("SELECT SUM(p.valor) FROM PagamentoModel p WHERE p.dataPagamento BETWEEN :inicio AND :fim")
    BigDecimal somarFaturamento(@Param("inicio") LocalDateTime inicio, @Param("fim") LocalDateTime fim);

    @Query("SELECT COUNT(p) FROM PagamentoModel p WHERE p.dataPagamento BETWEEN :inicio AND :fim")
    Long contarPagamentos(@Param("inicio") LocalDateTime inicio, @Param("fim") LocalDateTime fim);
}