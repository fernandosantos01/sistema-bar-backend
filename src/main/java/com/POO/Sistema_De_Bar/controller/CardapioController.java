package com.POO.Sistema_De_Bar.controller;

import com.POO.Sistema_De_Bar.model.ProdutoModel;
import com.POO.Sistema_De_Bar.service.ComandaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

public class CardapioController {
    private final ComandaService comandaService;

    public CardapioController(ComandaService comandaService) {
        this.comandaService = comandaService;
    }

    @GetMapping
    public ResponseEntity<List<ProdutoModel>> listarCardapio() {
        return ResponseEntity.ok(comandaService.listarProdutosDisponiveis());
    }
}
