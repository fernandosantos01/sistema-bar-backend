package com.POO.Sistema_De_Bar.controller;

import com.POO.Sistema_De_Bar.dto.AbrirMesaDTO;
import com.POO.Sistema_De_Bar.dto.AdicionarItemDTO;
import com.POO.Sistema_De_Bar.dto.ResumoContaDTO;
import com.POO.Sistema_De_Bar.model.ComandaModel;
import com.POO.Sistema_De_Bar.model.ItemComandaModel;
import com.POO.Sistema_De_Bar.service.ComandaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/comandas")
public class ComandaController {

    private final ComandaService comandaService;

    public ComandaController(ComandaService comandaService) {
        this.comandaService = comandaService;
    }

    @PostMapping("/abrir")
    public ResponseEntity<ComandaModel> abrirMesa(@RequestBody AbrirMesaDTO dados) {
        ComandaModel novaComanda = comandaService.abrirComanda(dados);
        return ResponseEntity.status(HttpStatus.CREATED).body(novaComanda);
    }

    @PostMapping("/{id}/itens")
    public ResponseEntity<ItemComandaModel> adicionarItem(@PathVariable UUID id, @RequestBody AdicionarItemDTO dados) {
        ItemComandaModel itemCriado = comandaService.adicionarItem(id, dados);
        return ResponseEntity.status(HttpStatus.CREATED).body(itemCriado);
    }

    @GetMapping("/{id}/resumo")
    public ResponseEntity<ResumoContaDTO> verConta(@PathVariable UUID id) {
        ResumoContaDTO resumo = comandaService.calcularContaResumo(id);
        return ResponseEntity.ok(resumo);
    }
}