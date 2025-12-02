package com.POO.Sistema_De_Bar.repository;

import com.POO.Sistema_De_Bar.model.ItemComandaModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface ItemComandaRepository extends JpaRepository<ItemComandaModel, Long> {
    List<ItemComandaModel> findByComandaId(Long comandaId);

    @Query(value = """
        SELECT p.nome, SUM(i.quantidade) as qtd, SUM(i.quantidade * i.preco_unitario) as total
        FROM itens_comanda i
        JOIN produtos p ON i.produto_id = p.id
        WHERE i.cancelado = false
        GROUP BY p.id, p.nome
        """, nativeQuery = true)
    List<Object[]> gerarRelatorioVendas();

}
