package com.POO.Sistema_De_Bar.repository;

import com.POO.Sistema_De_Bar.model.ProdutoModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ProdutoRepository extends JpaRepository<ProdutoModel, Long> {
    List<ProdutoModel> findByDisponivelTrue();
}
