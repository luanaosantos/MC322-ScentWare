// Testes dos nomes salvos e das regras de escolha recuperadas.
package br.unicamp.ic.mc322.persistence.conversores;

import br.unicamp.ic.mc322.model.*;
import br.unicamp.ic.mc322.strategy.*;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EstrategiaXmlTest {
    @Test
    void estrategiasRecuperadasContinuamEscolhendoPelasMesmasRegras() {
        EstrategiaXml xml = new EstrategiaXml();
        Demanda primeira = new Demanda(TipoProduto.HIDRATANTE, 2, 4);
        Demanda maior = new Demanda(TipoProduto.HIDRATANTE, 10, 100);
        Demanda cabeNoBudget = new Demanda(TipoProduto.HIDRATANTE, 5, 10);
        List<Demanda> demandas = List.of(primeira, maior, cabeNoBudget);
        for (EstrategiaProducao original : List.of(new EstrategiaOrdemChegada(),
                new EstrategiaMaiorDemanda(), new EstrategiaMaximoProdutos())) {
            EstrategiaProducao recuperada = xml.ler(xml.escrever(original));
            assertNotSame(original, recuperada);
            assertSame(original.selecionarDemanda(demandas, 10), recuperada.selecionarDemanda(demandas, 10));
        }
    }

    @Test
    void estrategiaDesconhecidaNaoViraOrdemDeChegada() {
        EstrategiaXml xml = new EstrategiaXml();
        assertThrows(IllegalArgumentException.class, () -> xml.ler("OUTRA_REGRA"));
        EstrategiaProducao outra = new EstrategiaProducao() {
            @Override public Demanda selecionarDemanda(List<Demanda> demandas, double budget) { return null; }
            @Override public String getNomeEstrategia() { return "Outra regra"; }
        };
        assertThrows(IllegalArgumentException.class, () -> xml.escrever(outra));
    }
}
