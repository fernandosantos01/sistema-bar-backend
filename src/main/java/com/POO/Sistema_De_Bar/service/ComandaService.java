package com.POO.Sistema_De_Bar.service;

import com.POO.Sistema_De_Bar.dto.AbrirMesaDTO;
import com.POO.Sistema_De_Bar.dto.AdicionarItemDTO;
import com.POO.Sistema_De_Bar.dto.ItemContaDTO;
import com.POO.Sistema_De_Bar.dto.ResumoContaDTO;
import com.POO.Sistema_De_Bar.model.*;
import com.POO.Sistema_De_Bar.repository.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class ComandaService {
    private final ComandaRepository comandaRepository;
    private final ItemComandaRepository itemComandaRepository;
    private final ConfiguracaoRepository configuracaoRepository;
    private final MesaRepository mesaRepository;
    private final ProdutoRepository produtoRepository;
    private final PagamentoRepository pagamentoRepository;

    public ComandaService(ComandaRepository comandaRepository, ItemComandaRepository itemComandaRepository, ConfiguracaoRepository configuracaoRepository, MesaRepository mesaRepository, ProdutoRepository produtoRepository, PagamentoRepository pagamentoRepository) {
        this.comandaRepository = comandaRepository;
        this.itemComandaRepository = itemComandaRepository;
        this.configuracaoRepository = configuracaoRepository;
        this.mesaRepository = mesaRepository;
        this.produtoRepository = produtoRepository;
        this.pagamentoRepository = pagamentoRepository;
    }

    public ResumoContaDTO calcularContaResumo(Long comandaId) {
        ComandaModel comanda = comandaRepository.findById(comandaId)
                .orElseThrow(() -> new RuntimeException("Comanda não encontrada"));

        List<ItemComandaModel> itens = itemComandaRepository.findByComandaId(comandaId);

        ConfiguracaoModel config = configuracaoRepository.findAll().stream().findFirst()
                .orElseThrow(() -> new RuntimeException("Configuração não encontrada"));

        BigDecimal subtotalComida = BigDecimal.ZERO;
        BigDecimal subtotalBebida = BigDecimal.ZERO;
        List<ItemContaDTO> itensDTO = new ArrayList<>();

        for (ItemComandaModel item : itens) {
            if (item.isCancelado()) continue;

            BigDecimal totalItem = item.getPrecoUnitario().multiply(new BigDecimal(item.getQuantidade()));

            itensDTO.add(new ItemContaDTO(
                    item.getProduto().getNome(),
                    item.getPrecoUnitario(),
                    item.getQuantidade(),
                    totalItem,
                    item.getProduto().getCategoria().name()
            ));
            if (item.getProduto().getCategoria() == CategoriaProduto.COMIDA) {
                subtotalComida = subtotalComida.add(totalItem);
            } else {
                subtotalBebida = subtotalBebida.add(totalItem);
            }
        }

        BigDecimal gorjetaComida = subtotalComida.multiply(config.getPecentualGorjetaComida()).divide(new BigDecimal(100));
        BigDecimal gorjetaBebida = subtotalBebida.multiply(config.getPecentualGorjetaBebida()).divide(new BigDecimal(100));

        BigDecimal totalCouvert = BigDecimal.ZERO;
        if (comanda.isCouvertHabilitado()) {
            totalCouvert = config.getValorCovert().multiply(new BigDecimal(comanda.getQtdePessoas()));
        }

        BigDecimal totalGeral = subtotalComida.add(subtotalBebida)
                .add(gorjetaComida).add(gorjetaBebida)
                .add(totalCouvert);

        List<PagamentoModel> pagamentos = pagamentoRepository.findByComandaId(comandaId);

        BigDecimal totalPago = pagamentos.stream()
                .map(PagamentoModel::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal saldoRestante = totalGeral.subtract(totalPago);

        return new ResumoContaDTO(
                itensDTO,
                subtotalComida,
                subtotalBebida,
                gorjetaComida,
                gorjetaBebida,
                totalCouvert,
                totalGeral,
                totalPago,
                saldoRestante,
                comanda.getStatus().name()
        );
    }

    public ComandaModel abrirComanda(AbrirMesaDTO dados) {
        MesaModel mesa = mesaRepository.findByNumero(dados.numeroMesa())
                .orElseThrow(() -> new RuntimeException("Mesa não encontrada!"));

        if (comandaRepository.findByMesaIdAndStatus(mesa.getId(), StatusComanda.ABERTA).isPresent()) {
            throw new RuntimeException("Esta mesa já está ocupada!");
        }

        ComandaModel comanda = new ComandaModel();
        comanda.setMesa(mesa);
        comanda.setQtdePessoas(dados.qtdPessoas());
        comanda.setStatus(StatusComanda.ABERTA);

        mesa.setStatus(StatusMesa.OCUPADA);
        mesaRepository.save(mesa);

        return comandaRepository.save(comanda);
    }

    public ItemComandaModel adicionarItem(Long comandaId, AdicionarItemDTO dados) {
        ComandaModel comanda = comandaRepository.findById(comandaId)
                .orElseThrow(() -> new RuntimeException("Comanda não encontrada"));

        if (comanda.getStatus() != StatusComanda.ABERTA) {
            throw new RuntimeException("Não é possível adicionar itens em uma conta fechada/paga.");
        }

        ProdutoModel produto = produtoRepository.findById(dados.produtoId())
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        ItemComandaModel item = new ItemComandaModel();
        item.setComanda(comanda);
        item.setProduto(produto);
        item.setQuantidade(dados.quantidade());
        item.setPrecoUnitario(produto.getPreco());

        return itemComandaRepository.save(item);
    }

    public PagamentoModel registrarPagamento(Long comandaId, BigDecimal valor, String formaPagamento) {
        ComandaModel comanda = comandaRepository.findById(comandaId)
                .orElseThrow(() -> new RuntimeException("Comanda não encontrada"));

        if (comanda.getStatus() == StatusComanda.PAGA) {
            throw new RuntimeException("Esta comanda já está totalmente paga e encerrada.");
        }

        PagamentoModel pagamento = new PagamentoModel();
        pagamento.setComanda(comanda);
        pagamento.setValor(valor);
        pagamento.setFormaPagamento(formaPagamento);

        return pagamentoRepository.save(pagamento);
    }

    public ComandaModel fecharComanda(Long comandaId) {
        ComandaModel comanda = comandaRepository.findById(comandaId)
                .orElseThrow(() -> new RuntimeException("Comanda não encontrada"));

        ResumoContaDTO resumo = calcularContaResumo(comandaId);
        BigDecimal totalAPagar = resumo.totalGeral();

        List<PagamentoModel> pagamentos = pagamentoRepository.findByComandaId(comandaId);
        BigDecimal totalPago = pagamentos.stream()
                .map(PagamentoModel::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (totalPago.compareTo(totalAPagar) < 0) {
            BigDecimal restante = totalAPagar.subtract(totalPago);
            throw new RuntimeException("Não é possível fechar a conta. Saldo pendente: R$ " + restante);
        }

        comanda.setStatus(StatusComanda.PAGA);
        comanda.setDataFechamento(java.time.LocalDateTime.now());

        MesaModel mesa = comanda.getMesa();
        mesa.setStatus(StatusMesa.LIVRE);
        mesaRepository.save(mesa);

        return comandaRepository.save(comanda);
    }
}
