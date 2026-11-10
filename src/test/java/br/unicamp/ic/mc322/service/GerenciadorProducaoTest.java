// Testes dos recursos, dos lotes e dos pedidos da fabrica.
package br.unicamp.ic.mc322.service;

import br.unicamp.ic.mc322.exception.DemandaInvalidaException;
import br.unicamp.ic.mc322.exception.PlantaException;
import br.unicamp.ic.mc322.exception.RecursoInsuficienteException;
import br.unicamp.ic.mc322.model.*;
import br.unicamp.ic.mc322.strategy.EstrategiaOrdemChegada;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GerenciadorProducaoTest {
    @Test
    void compraDescontaBudgetESomaEstoque() {
        GerenciadorProducao fabrica = fabrica(100, 100, true);
        assertTrue(fabrica.comprarMateriaPrima(20));
        assertEquals(120, fabrica.getMateriaPrima().getQuantidade());
        assertEquals(98, fabrica.getBudget(), 0.000001);
    }

    @Test
    void compraRecusadaPreservaRecursos() {
        GerenciadorProducao fabrica = fabrica(100, 1, true);
        assertThrows(RecursoInsuficienteException.class, () -> fabrica.comprarMateriaPrima(20));
        assertThrows(IllegalArgumentException.class, () -> fabrica.comprarMateriaPrima(-1));
        assertEquals(100, fabrica.getMateriaPrima().getQuantidade());
        assertEquals(1, fabrica.getBudget());
    }

    @Test
    void producaoAprovadaConcluiPedidoEGuardaInstanciasNovas() {
        GerenciadorProducao fabrica = fabrica(100, 100, true);
        Produto modelo = fabrica.getProdutosModelos().get(0);
        Demanda demanda = new Demanda(modelo.getTipo(), 2, 0);
        fabrica.registrarDemanda(demanda);
        assertTrue(fabrica.fabricarDemanda(demanda, modelo));
        assertEquals(StatusDemanda.CONCLUIDA, demanda.getStatus());
        assertEquals(60, fabrica.getMateriaPrima().getQuantidade());
        assertEquals(82, fabrica.getBudget());
        assertEquals(2, fabrica.getProdutosFabricados().size());
        Produto primeiro = fabrica.getProdutosFabricados().get(0);
        Produto segundo = fabrica.getProdutosFabricados().get(1);
        assertNotSame(modelo, primeiro);
        assertNotSame(primeiro, segundo);
        assertNotEquals(primeiro.getId(), segundo.getId());
        assertEquals(1, primeiro.getLote());
        assertEquals("Aguardando", modelo.getStatus());
        assertEquals(2, fabrica.getProximoLote());
        assertEquals(3, fabrica.getProximoProduto());
    }

    @Test
    void rejeicoesFicamPendentesComRecursosConsumidos() {
        GerenciadorProducao fabrica = fabrica(100, 100, false);
        Produto modelo = fabrica.getProdutosModelos().get(0);
        Demanda demanda = new Demanda(modelo.getTipo(), 2, 0);
        fabrica.registrarDemanda(demanda);
        assertFalse(fabrica.fabricarDemanda(demanda, modelo));
        assertEquals(StatusDemanda.PENDENTE, demanda.getStatus());
        assertEquals(2, demanda.getQuantidadeProdutos());
        assertEquals(18, demanda.getCustoTotal());
        assertEquals(60, fabrica.getMateriaPrima().getQuantidade());
        assertEquals(82, fabrica.getBudget());
        assertTrue(fabrica.getProdutosFabricados().isEmpty());
    }

    @Test
    void faltaDeEstoqueCancelaSemConsumirOuDebitar() {
        GerenciadorProducao fabrica = fabrica(10, 100, true);
        conferirCancelamentoSemConsumo(fabrica);
        assertEquals(10, fabrica.getMateriaPrima().getQuantidade());
        assertEquals(100, fabrica.getBudget());
    }

    @Test
    void faltaDeBudgetCancelaSemConsumirOuDebitar() {
        GerenciadorProducao fabrica = fabrica(100, 1, true);
        conferirCancelamentoSemConsumo(fabrica);
        assertEquals(100, fabrica.getMateriaPrima().getQuantidade());
        assertEquals(1, fabrica.getBudget());
    }

    @Test
    void cadastroImpedeDuplicacaoEMudancaDaLinha() {
        GerenciadorProducao fabrica = fabrica(100, 100, true);
        Demanda demanda = new Demanda(TipoProduto.HIDRATANTE, 1, 0);
        fabrica.registrarDemanda(demanda);
        assertThrows(DemandaInvalidaException.class, () -> fabrica.registrarDemanda(demanda));
        assertThrows(PlantaException.class, () -> fabrica.adicionarMaquina(new MaquinaInspecao("Outra", 100, 0, 1, 100, 0)));
        assertEquals(9, demanda.getCustoTotal());
        fabrica.atualizarDemanda(TipoProduto.HIDRATANTE, 3);
        assertEquals(27, demanda.getCustoTotal());
        assertEquals(1, fabrica.getDemandas().size());
    }

    private void conferirCancelamentoSemConsumo(GerenciadorProducao fabrica) {
        Produto modelo = fabrica.getProdutosModelos().get(0);
        Demanda demanda = new Demanda(modelo.getTipo(), 2, 0);
        fabrica.registrarDemanda(demanda);
        assertThrows(RecursoInsuficienteException.class, () -> fabrica.fabricarDemanda(demanda, modelo));
        assertEquals(StatusDemanda.CANCELADA, demanda.getStatus());
        assertTrue(fabrica.getProdutosFabricados().isEmpty());
        assertEquals(1, fabrica.getProximoLote());
        assertEquals(1, fabrica.getProximoProduto());
    }

    private GerenciadorProducao fabrica(double estoque, double budget, boolean aprovar) {
        GerenciadorProducao fabrica = new GerenciadorProducao(
                new MateriaPrima("MP", "Oleo", estoque, "mL", 0.1), budget,
                new ArrayList<>(List.of(new ProdutoHidratante("P", "Hidratante", 20))),
                new EstrategiaOrdemChegada(), 0);
        fabrica.adicionarMaquina(new MaquinaHomogeneizador("Mistura", 100, 0, 2, 100, 0));
        fabrica.adicionarMaquina(new MaquinaEmpacotadora("Envase", 100, 0, 3, 100, 0));
        // Aqui o teste avalia o gerenciador; a inspecao tem seus testes proprios.
        fabrica.adicionarMaquina(new MaquinaInspecao("Inspecao", 100, 0, 4, 100, 0) {
            @Override public boolean processar(Produto produto) {
                produto.setStatus(aprovar ? "Aprovado" : "Rejeitado");
                return aprovar;
            }
        });
        return fabrica;
    }
}
