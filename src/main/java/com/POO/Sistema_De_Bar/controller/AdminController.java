package com.POO.Sistema_De_Bar.controller;

import com.POO.Sistema_De_Bar.dto.*;
import com.POO.Sistema_De_Bar.model.ConfiguracaoModel;
import com.POO.Sistema_De_Bar.model.MesaModel;
import com.POO.Sistema_De_Bar.model.ProdutoModel;
import com.POO.Sistema_De_Bar.service.AdminService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {
    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/produtos")
    public ResponseEntity<List<ProdutoModel>> listarTodosProdutos() {
        return ResponseEntity.ok(adminService.listarTodosProdutos());
    }

    @GetMapping("/configuracoes")
    public ResponseEntity<ConfiguracaoModel> buscarConfiguracao() {
        return ResponseEntity.ok(adminService.buscarConfiguracao());
    }

    @PostMapping("/produtos")
    public ResponseEntity<ProdutoModel> cadastrarProduto(@RequestBody ProdutoDTO dados) {
        ProdutoModel produto = adminService.cadastrarProduto(dados);
        return ResponseEntity.status(HttpStatus.CREATED).body(produto);
    }

    @PutMapping("/produtos/{id}")
    public ResponseEntity<ProdutoModel> atualizarProduto(@PathVariable Long id, @RequestBody ProdutoDTO dados) {
        ProdutoModel produto = adminService.atualizarProduto(id, dados);
        return ResponseEntity.ok(produto);
    }

    @PutMapping("/configuracoes")
    public ResponseEntity<ConfiguracaoModel> atualizarConfiguracao(@RequestBody ConfiguracaoDTO dados) {
        ConfiguracaoModel config = adminService.atualizarConfiguracao(dados);
        return ResponseEntity.ok(config);
    }

    @PostMapping("/mesas")
    public ResponseEntity<MesaModel> cadastrarMesa(@RequestBody MesaDTO dados) {
        MesaModel novaMesa = adminService.cadastrarMesa(dados);
        return ResponseEntity.status(HttpStatus.CREATED).body(novaMesa);
    }

    @DeleteMapping("/mesas/{id}")
    public ResponseEntity<Void> deletarMesa(@PathVariable Long id) {
        adminService.deletarMesa(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/relatorios/faturamento")
    public ResponseEntity<RelatorioFaturamentoDTO> getFaturamento(
            @RequestParam LocalDate inicio,
            @RequestParam LocalDate fim) {
        return ResponseEntity.ok(adminService.gerarRelatorioFaturamento(inicio, fim));
    }

    @GetMapping("/relatorios/itens-mais-vendidos")
    public ResponseEntity<List<RelatorioItemDTO>> getItensMaisVendidos() {
        return ResponseEntity.ok(adminService.gerarRelatorioItens(false));
    }

    @GetMapping("/relatorios/itens-maior-faturamento")
    public ResponseEntity<List<RelatorioItemDTO>> getItensMaiorFaturamento() {
        return ResponseEntity.ok(adminService.gerarRelatorioItens(true));
    }
}
