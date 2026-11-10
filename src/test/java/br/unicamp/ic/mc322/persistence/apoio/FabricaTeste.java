// Dados comuns dos testes de persistencia.
package br.unicamp.ic.mc322.persistence.apoio;

import br.unicamp.ic.mc322.model.*;
import br.unicamp.ic.mc322.service.GerenciadorProducao;
import br.unicamp.ic.mc322.strategy.*;
import java.util.ArrayList;
import java.util.List;

public final class FabricaTeste {
    private FabricaTeste() { }

    public static GerenciadorProducao criar() {
        ArrayList<Produto> modelos = new ArrayList<>(List.of(
                new ProdutoHidratante("P1", "Hidratante", 20),
                new ProdutoCremeDeMaos("P2", "Creme", 30),
                new ProdutoEsfoliante("P3", "Esfoliante", 40)));
        LinhaDeProducao linha = new LinhaDeProducao("Linha de teste");
        MaquinaHomogeneizador mistura = new MaquinaHomogeneizador("Mistura", 500, 0.2, 2, 35, 4, 3);
        mistura.getMotor().restaurarEstado(false, 19);
        mistura.getSensores().get(0).restaurarEstado(false, false);
        mistura.getValvula().restaurarEstado(0, false);
        mistura.restaurarEstado(false, 4);
        MaquinaEmpacotadora envase = new MaquinaEmpacotadora("Envase", 600, 0.3, 3, 85, 2, 1);
        envase.getMotor().restaurarEstado(true, 85);
        envase.getSensores().get(0).restaurarEstado(true, true);
        envase.getBico().restaurarEstado(true, true);
        envase.restaurarEstado(true, 1);
        MaquinaInspecao inspecao = new MaquinaInspecao("Inspecao", 700, 0.4, 4, 45, 6, 2);
        inspecao.getAtuador().restaurarEstado(false, 12);
        linha.adicionarMaquina(mistura);
        linha.adicionarMaquina(envase);
        linha.adicionarMaquina(inspecao);
        List<Demanda> demandas = List.of(
                Demanda.restaurar(TipoProduto.HIDRATANTE, 2, 18, StatusDemanda.PENDENTE),
                Demanda.restaurar(TipoProduto.CREME_DE_MAOS, 1, 9, StatusDemanda.EM_PRODUCAO),
                Demanda.restaurar(TipoProduto.ESFOLIANTE, 0, 0, StatusDemanda.CONCLUIDA),
                Demanda.restaurar(TipoProduto.HIDRATANTE, 3, 27, StatusDemanda.CANCELADA));
        Produto aprovado = new ProdutoEsfoliante("P3-7", "Esfoliante", 40);
        aprovado.restaurarEstado("Aprovado", 0.3, 2);
        return GerenciadorProducao.restaurar(new MateriaPrima("MP1", "Oleo de amendoas", 300, "mL", 0.1),
                200, modelos, new EstrategiaMaximoProdutos(), 0.2, linha, demandas,
                List.of(aprovado), 3, 8, Cenario.APOCALIPTICO);
    }
}
