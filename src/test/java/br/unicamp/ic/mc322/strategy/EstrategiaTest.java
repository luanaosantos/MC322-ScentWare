// Testes da escolha de demandas e dos desempates.
package br.unicamp.ic.mc322.strategy;

import br.unicamp.ic.mc322.model.Demanda;
import br.unicamp.ic.mc322.model.TipoProduto;
import java.util.List;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EstrategiaTest {
    @Test
    void ordemDeChegadaIgnoraEncerradasEQuantidadeZero() {
        Demanda cancelada = demanda(10, 20);
        cancelada.cancelar();
        Demanda primeira = demanda(2, 4);
        assertSame(primeira, new EstrategiaOrdemChegada().selecionarDemanda(
                List.of(cancelada, demanda(0, 0), primeira, demanda(8, 16)), 0));
    }

    @Test
    void maiorDemandaDesempataPelaOrdemDeRegistro() {
        Demanda primeiraMaior = demanda(8, 16);
        assertSame(primeiraMaior, new EstrategiaMaiorDemanda().selecionarDemanda(
                List.of(demanda(2, 4), primeiraMaior, demanda(8, 16)), 0));
    }

    @Test
    void maximoProdutosFiltraBudgetSemMudarPedidos() {
        Demanda cara = demanda(10, 100);
        Demanda elegivel = demanda(5, 10);
        Demanda empate = demanda(5, 10);
        List<Demanda> fila = List.of(cara, elegivel, empate);
        assertSame(elegivel, new EstrategiaMaximoProdutos().selecionarDemanda(fila, 10));
        assertEquals(10, cara.getQuantidadeProdutos());
        assertEquals(5, elegivel.getQuantidadeProdutos());
        assertNull(new EstrategiaMaximoProdutos().selecionarDemanda(fila, 9));
    }

    @Test
    void filaVaziaNaoEscolheDemanda() {
        for (EstrategiaProducao estrategia : List.of(new EstrategiaOrdemChegada(),
                new EstrategiaMaiorDemanda(), new EstrategiaMaximoProdutos())) {
            assertNull(estrategia.selecionarDemanda(List.of(), 100));
        }
    }

    private Demanda demanda(int quantidade, double custo) {
        return new Demanda(TipoProduto.HIDRATANTE, quantidade, custo);
    }
}
