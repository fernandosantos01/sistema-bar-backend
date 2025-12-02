package com.POO.Sistema_De_Bar.controller;

import com.POO.Sistema_De_Bar.dto.*;
import com.POO.Sistema_De_Bar.model.ComandaModel;
import com.POO.Sistema_De_Bar.model.ItemComandaModel;
import com.POO.Sistema_De_Bar.model.MesaModel;
import com.POO.Sistema_De_Bar.model.PagamentoModel;
import com.POO.Sistema_De_Bar.service.ComandaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


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
    public ResponseEntity<ItemComandaModel> adicionarItem(@PathVariable Long id, @RequestBody AdicionarItemDTO dados) {
        ItemComandaModel itemCriado = comandaService.adicionarItem(id, dados);
        return ResponseEntity.status(HttpStatus.CREATED).body(itemCriado);
    }

    @GetMapping("/{id}/resumo")
    public ResponseEntity<ResumoContaDTO> verConta(@PathVariable Long id) {
        ResumoContaDTO resumo = comandaService.calcularContaResumo(id);
        return ResponseEntity.ok(resumo);
    }

    @PostMapping("/{id}/pagar")
    public ResponseEntity<PagamentoModel> pagar(@PathVariable Long id, @RequestBody RegistrarPagamentoDTO dados) {
        PagamentoModel pagamento = comandaService.registrarPagamento(id, dados.valor(), dados.formaPagamento());
        return ResponseEntity.status(HttpStatus.CREATED).body(pagamento);
    }

    @PostMapping("/{id}/fechar")
    public ResponseEntity<ComandaModel> fecharConta(@PathVariable Long id) {
        ComandaModel comandaFechada = comandaService.fecharComanda(id);
        return ResponseEntity.ok(comandaFechada);
    }

    @DeleteMapping("/{comandaId}/itens/{itemId}")
    public ResponseEntity<Void> cancelarItem(
            @PathVariable Long comandaId,
            @PathVariable Long itemId,
            @RequestBody CancelarItemDTO dados) {

        comandaService.cancelarItem(comandaId, itemId, dados.motivo());

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/couvert")
    public ResponseEntity<ComandaModel> atualizarCouvert(
            @PathVariable Long id,
            @RequestParam boolean habilitado) {

        ComandaModel comandaAtualizada = comandaService.atualizarCouvert(id, habilitado);
        return ResponseEntity.ok(comandaAtualizada);
    }

    @GetMapping("/mesas")
    public ResponseEntity<List<MesaDashboardDTO>> listarMesasAtivas() {
        return ResponseEntity.ok(comandaService.listarMesasParaDashboard());
    }
}