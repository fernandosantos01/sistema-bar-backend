package com.POO.Sistema_De_Bar.repository;

import com.POO.Sistema_De_Bar.model.PagamentoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PagamentoRepository extends JpaRepository<PagamentoModel, Long> {
    List<PagamentoModel> findByComandaId(Long comandaId);
}