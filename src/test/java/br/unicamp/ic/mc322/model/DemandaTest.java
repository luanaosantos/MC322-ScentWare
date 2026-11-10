// Testes do pedido, das transicoes e do estoque.
package br.unicamp.ic.mc322.model;

import br.unicamp.ic.mc322.exception.DemandaInvalidaException;
import br.unicamp.ic.mc322.exception.RecursoInsuficienteException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DemandaTest {
    @Test
    void concluirZeraQuantidadeECusto() {
        Demanda demanda = new Demanda(TipoProduto.HIDRATANTE, 3, 12);
        demanda.iniciarProducao();
        demanda.concluir();
        assertEquals(StatusDemanda.CONCLUIDA, demanda.getStatus());
        assertEquals(0, demanda.getQuantidadeProdutos());
        assertEquals(0, demanda.getCustoTotal());
        assertThrows(DemandaInvalidaException.class, () -> demanda.atualizarQuantidade(2));
    }

    @Test
    void rejeicoesVoltamPraFilaComQuantidadeRestante() {
        Demanda demanda = new Demanda(TipoProduto.CREME_DE_MAOS, 5, 25);
        demanda.iniciarProducao();
        demanda.voltarParaPendente();
        demanda.atualizarQuantidade(2);
        demanda.setCustoTotal(10);
        assertEquals(StatusDemanda.PENDENTE, demanda.getStatus());
        assertEquals(60, demanda.calcularMateriaPrimaNecessaria(new ProdutoCremeDeMaos("P", "Creme", 30)));
        assertTrue(demanda.viabilidadeFinanceira(10));
        assertFalse(demanda.viabilidadeFinanceira(9));
    }

    @Test
    void cancelarPreservaPedidoEImpedeReabertura() {
        Demanda demanda = new Demanda(TipoProduto.ESFOLIANTE, 4, 18);
        demanda.cancelar();
        assertEquals(StatusDemanda.CANCELADA, demanda.getStatus());
        assertEquals(4, demanda.getQuantidadeProdutos());
        assertEquals(18, demanda.getCustoTotal());
        assertThrows(DemandaInvalidaException.class, demanda::iniciarProducao);
        assertThrows(DemandaInvalidaException.class, demanda::concluir);
    }

    @Test
    void quantidadeZeroEsperaAtualizacao() {
        Demanda demanda = new Demanda(TipoProduto.HIDRATANTE, 0, 0);
        assertFalse(demanda.viabilidadeFinanceira(100));
        assertThrows(DemandaInvalidaException.class, demanda::iniciarProducao);
        demanda.atualizarQuantidade(1);
        assertTrue(demanda.iniciarProducao());
    }

    @Test
    void entradasInvalidasSaoRecusadas() {
        assertThrows(DemandaInvalidaException.class, () -> new Demanda(null, 1, 1));
        assertThrows(DemandaInvalidaException.class, () -> new Demanda(TipoProduto.HIDRATANTE, -1, 1));
        assertThrows(IllegalArgumentException.class, () -> new Demanda(TipoProduto.HIDRATANTE, 1, Double.NaN));
        Demanda demanda = new Demanda(TipoProduto.HIDRATANTE, 1, 1);
        assertThrows(IllegalArgumentException.class, () -> demanda.calcularMateriaPrimaNecessaria(new ProdutoEsfoliante("P", "Esfoliante", 40)));
        assertThrows(DemandaInvalidaException.class, demanda::concluir);
    }

    @Test
    void consumoRecusadoNaoAlteraEstoque() {
        MateriaPrima oleo = new MateriaPrima("MP", "Oleo", 30, "mL", 0.1);
        assertThrows(RecursoInsuficienteException.class, () -> oleo.consumir(31));
        assertEquals(30, oleo.getQuantidade());
        assertThrows(IllegalArgumentException.class, () -> oleo.adicionarEstoque(Double.POSITIVE_INFINITY));
        assertEquals(30, oleo.getQuantidade());
        oleo.consumir(30);
        assertEquals(0, oleo.getQuantidade());
    }
}
