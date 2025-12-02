package com.POO.Sistema_De_Bar.service;

import com.POO.Sistema_De_Bar.dto.*;
import com.POO.Sistema_De_Bar.model.*;
import com.POO.Sistema_De_Bar.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class AdminService {
    private final ProdutoRepository produtoRepository;
    private final ConfiguracaoRepository configuracaoRepository;
    private final MesaRepository mesaRepository;
    private final ComandaRepository comandaRepository;
    private final ItemComandaRepository itemComandaRepository;
    private final PagamentoRepository pagamentoRepository;

    public AdminService(ProdutoRepository produtoRepository, ConfiguracaoRepository configuracaoRepository, MesaRepository mesaRepository, ComandaRepository comandaRepository, ItemComandaRepository itemComandaRepository, PagamentoRepository pagamentoRepository) {
        this.produtoRepository = produtoRepository;
        this.configuracaoRepository = configuracaoRepository;
        this.mesaRepository = mesaRepository;
        this.comandaRepository = comandaRepository;
        this.itemComandaRepository = itemComandaRepository;
        this.pagamentoRepository = pagamentoRepository;
    }

    public List<ProdutoModel> listarTodosProdutos() {
        return produtoRepository.findAll();
    }

    @Transactional
    public MesaModel cadastrarMesa(MesaDTO dados) {
        if (mesaRepository.findByNumeroAndAtivaTrue(dados.numero()).isPresent()) {
            throw new RuntimeException("Já existe uma mesa com o número " + dados.numero());
        }

        MesaModel mesa = new MesaModel();
        mesa.setNumero(dados.numero());
        mesa.setStatus(StatusMesa.LIVRE);
        return mesaRepository.save(mesa);
    }

    @Transactional
    public void deletarMesa(Long id) {
        // 1. Busca a mesa
        MesaModel mesa = mesaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Mesa não encontrada"));

        // 2. Validação: Se estiver ocupada, lança erro 409 (Conflict)
        if (comandaRepository.findByMesaIdAndStatus(id, StatusComanda.ABERTA).isPresent()) {
            // MUDANÇA AQUI: Usamos ResponseStatusException para gerar um erro HTTP controlado
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Esta mesa está ocupada e não pode ser excluída.");
        }

        // 3. Soft Delete
        mesa.setAtiva(false);
        mesaRepository.save(mesa);
    }

    @Transactional
    public ProdutoModel cadastrarProduto(ProdutoDTO dados) {
        ProdutoModel produto = new ProdutoModel();
        produto.setNome(dados.nome());
        produto.setDescricao(dados.descricao());
        produto.setPreco(dados.preco());
        produto.setCategoria(dados.categoria());
        produto.setDisponivel(true);

        return produtoRepository.save(produto);
    }

    @Transactional
    public ProdutoModel atualizarProduto(Long id, ProdutoDTO dados) {
        ProdutoModel produto = produtoRepository.findById(id).orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        if (dados.nome() != null) produto.setNome(dados.nome());
        if (dados.descricao() != null) produto.setDescricao(dados.descricao());
        if (dados.preco() != null) produto.setPreco(dados.preco());
        if (dados.categoria() != null) produto.setCategoria(dados.categoria());
        if (dados.disponivel() != null) produto.setDisponivel(dados.disponivel());

        return produtoRepository.save(produto);
    }

    @Transactional
    public ConfiguracaoModel atualizarConfiguracao(ConfiguracaoDTO dados) {
        ConfiguracaoModel config = configuracaoRepository.findAll().stream().findFirst().orElseThrow(() -> new RuntimeException("Configuração não inicializada (rode o DataLoader)"));

        if (dados.valorCouvert() != null) config.setValorCovert(dados.valorCouvert());
        if (dados.percentualGorjetaComida() != null) config.setPecentualGorjetaComida(dados.percentualGorjetaComida());
        if (dados.percentualGorjetaBebida() != null) config.setPecentualGorjetaBebida(dados.percentualGorjetaBebida());

        return configuracaoRepository.save(config);
    }

    public ConfiguracaoModel buscarConfiguracao() {
        return configuracaoRepository.findAll().stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Configuração não inicializada."));
    }

    public RelatorioFaturamentoDTO gerarRelatorioFaturamento(LocalDate inicio, LocalDate fim) {
        // Ajusta as datas para pegar o dia inteiro (00:00 até 23:59)
        LocalDateTime dataInicio = inicio.atStartOfDay();
        LocalDateTime dataFim = fim.atTime(LocalTime.MAX);

        BigDecimal total = pagamentoRepository.somarFaturamento(dataInicio, dataFim);
        Long qtd = pagamentoRepository.contarPagamentos(dataInicio, dataFim);

        if (total == null) total = BigDecimal.ZERO;

        return new RelatorioFaturamentoDTO(total, qtd);
    }

    public List<RelatorioItemDTO> gerarRelatorioItens(boolean ordenarPorFaturamento) {
        List<Object[]> resultados = itemComandaRepository.gerarRelatorioVendas();
        List<RelatorioItemDTO> relatorio = new ArrayList<>();

        for (Object[] row : resultados) {
            String nome = (String) row[0];
            Long qtd = ((Number) row[1]).longValue();
            BigDecimal total = (BigDecimal) row[2];

            relatorio.add(new RelatorioItemDTO(nome, qtd, total));
        }

        // Ordenação via Java (Mais flexível)
        if (ordenarPorFaturamento) {
            relatorio.sort(Comparator.comparing(RelatorioItemDTO::totalFaturado).reversed());
        } else {
            relatorio.sort(Comparator.comparing(RelatorioItemDTO::quantidadeVendida).reversed());
        }

        return relatorio;
    }
}