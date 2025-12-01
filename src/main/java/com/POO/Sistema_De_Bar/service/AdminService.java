package com.POO.Sistema_De_Bar.service;

import com.POO.Sistema_De_Bar.dto.ConfiguracaoDTO;
import com.POO.Sistema_De_Bar.dto.ProdutoDTO;
import com.POO.Sistema_De_Bar.model.ConfiguracaoModel;
import com.POO.Sistema_De_Bar.model.ProdutoModel;
import com.POO.Sistema_De_Bar.repository.ConfiguracaoRepository;
import com.POO.Sistema_De_Bar.repository.ProdutoRepository;
import org.springframework.stereotype.Service;

@Service
public class AdminService {
    private final ProdutoRepository produtoRepository;
    private final ConfiguracaoRepository configuracaoRepository;

    public AdminService(ProdutoRepository produtoRepository, ConfiguracaoRepository configuracaoRepository) {
        this.produtoRepository = produtoRepository;
        this.configuracaoRepository = configuracaoRepository;
    }

    public ProdutoModel cadastrarProduto(ProdutoDTO dados) {
        ProdutoModel produto = new ProdutoModel();
        produto.setNome(dados.nome());
        produto.setDescricao(dados.descricao());
        produto.setPreco(dados.preco());
        produto.setCategoria(dados.categoria());
        produto.setDisponivel(true);

        return produtoRepository.save(produto);
    }

    public ProdutoModel atualizarProduto(Long id, ProdutoDTO dados) {
        ProdutoModel produto = produtoRepository.findById(id).orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        if (dados.nome() != null) produto.setNome(dados.nome());
        if (dados.descricao() != null) produto.setDescricao(dados.descricao());
        if (dados.preco() != null) produto.setPreco(dados.preco());
        if (dados.categoria() != null) produto.setCategoria(dados.categoria());
        if (dados.disponivel() != null) produto.setDisponivel(dados.disponivel());

        return produtoRepository.save(produto);
    }

    public ConfiguracaoModel atualizarConfiguracao(ConfiguracaoDTO dados) {
        ConfiguracaoModel config = configuracaoRepository.findAll().stream().findFirst().orElseThrow(() -> new RuntimeException("Configuração não inicializada (rode o DataLoader)"));

        if (dados.valorCouvert() != null) config.setValorCovert(dados.valorCouvert());
        if (dados.percentualGorjetaComida() != null) config.setPecentualGorjetaComida(dados.percentualGorjetaComida());
        if (dados.percentualGorjetaBebida() != null) config.setPecentualGorjetaBebida(dados.percentualGorjetaBebida());

        return configuracaoRepository.save(config);
    }
}