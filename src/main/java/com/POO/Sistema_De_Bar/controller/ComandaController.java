package com.POO.Sistema_De_Bar.controller;

import com.POO.Sistema_De_Bar.dto.AbrirMesaDTO;
import com.POO.Sistema_De_Bar.dto.AdicionarItemDTO;
import com.POO.Sistema_De_Bar.dto.RegistrarPagamentoDTO;
import com.POO.Sistema_De_Bar.dto.ResumoContaDTO;
import com.POO.Sistema_De_Bar.model.ComandaModel;
import com.POO.Sistema_De_Bar.model.ItemComandaModel;
import com.POO.Sistema_De_Bar.model.PagamentoModel;
import com.POO.Sistema_De_Bar.service.ComandaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


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
}