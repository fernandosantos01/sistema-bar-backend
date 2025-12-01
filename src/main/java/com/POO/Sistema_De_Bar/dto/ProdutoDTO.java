package com.POO.Sistema_De_Bar.dto;

import com.POO.Sistema_De_Bar.model.CategoriaProduto;
import java.math.BigDecimal;

public record ProdutoDTO(
        String nome,
        String descricao,
        BigDecimal preco,
        CategoriaProduto categoria,
        Boolean disponivel
) {}