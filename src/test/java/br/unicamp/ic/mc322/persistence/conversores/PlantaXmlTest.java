// Testes das relacoes entre as secoes da planta.
package br.unicamp.ic.mc322.persistence.conversores;

import br.unicamp.ic.mc322.model.TipoProduto;
import br.unicamp.ic.mc322.service.GerenciadorProducao;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Element;
import static br.unicamp.ic.mc322.persistence.apoio.FabricaTeste.criar;
import static br.unicamp.ic.mc322.persistence.xml.ElementosXml.*;
import static org.junit.jupiter.api.Assertions.*;

class PlantaXmlTest {
    @Test
    void demandaPrecisaDeModeloDaMesmaFabrica() throws Exception {
        PlantaXml xml = new PlantaXml();
        Element planta = xml.escrever(criar()).getDocumentElement();
        Element modelos = unico(planta, "modelos");
        Element hidratante = filhos(modelos, "produto").stream()
                .filter(produto -> produto.getAttribute("tipo").equals("HIDRATANTE")).findFirst().orElseThrow();
        modelos.removeChild(hidratante);
        assertThrows(IllegalArgumentException.class, () -> xml.ler(planta));
    }

    @Test
    void custoDaDemandaPrecisaCorresponderALinhaRecuperada() throws Exception {
        PlantaXml xml = new PlantaXml();
        Element planta = xml.escrever(criar()).getDocumentElement();
        Element maquina = filhos(unico(planta, "linha"), "maquina").get(0);
        maquina.setAttribute("custo", "20");
        assertThrows(IllegalArgumentException.class, () -> xml.ler(planta));
    }

    @Test
    void leituraPreservaFilaSemCompartilharEstadoComFabricaAnterior() throws Exception {
        PlantaXml xml = new PlantaXml();
        GerenciadorProducao original = criar();
        GerenciadorProducao recuperada = xml.ler(xml.escrever(original).getDocumentElement());
        recuperada.atualizarDemanda(TipoProduto.HIDRATANTE, 8);
        assertEquals(2, original.buscarDemandaPendente(TipoProduto.HIDRATANTE).getQuantidadeProdutos());
        assertEquals(8, recuperada.buscarDemandaPendente(TipoProduto.HIDRATANTE).getQuantidadeProdutos());
        assertEquals(4, recuperada.getDemandas().size());
        assertEquals(3, recuperada.getLinha().getMaquinas().size());
    }
}
