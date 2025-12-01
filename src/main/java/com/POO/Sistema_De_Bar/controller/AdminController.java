package com.POO.Sistema_De_Bar.controller;

import com.POO.Sistema_De_Bar.dto.ConfiguracaoDTO;
import com.POO.Sistema_De_Bar.dto.ProdutoDTO;
import com.POO.Sistema_De_Bar.model.ConfiguracaoModel;
import com.POO.Sistema_De_Bar.model.ProdutoModel;
import com.POO.Sistema_De_Bar.service.AdminService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class AdminController {
    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
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
}
