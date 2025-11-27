package com.POO.Sistema_De_Bar.repository;

import com.POO.Sistema_De_Bar.model.ItemComandaModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface ItemComandaRepository extends JpaRepository<ItemComandaModel, UUID> {
    List<ItemComandaModel> findByComandaId(UUID comandaId);

    @Query(value = """
            SELECT p.nome, SUM(i.quantidade) as total 
            FROM itens_comanda i 
            JOIN produtos p ON i.produto_id = p.id 
            GROUP BY p.nome 
            ORDER BY total DESC
            """, nativeQuery = true)
    List<Object[]> findItensMaisVendidos();

}
