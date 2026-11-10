// Testes da fila de pedidos e dos estados salvos.
package br.unicamp.ic.mc322.persistence.conversores;

import br.unicamp.ic.mc322.model.*;
import br.unicamp.ic.mc322.exception.DemandaInvalidaException;
import br.unicamp.ic.mc322.persistence.xml.DocumentoXml;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import static br.unicamp.ic.mc322.persistence.xml.ElementosXml.*;
import static org.junit.jupiter.api.Assertions.*;

class DemandaXmlTest {
    @Test
    void ordemDaFilaEStatusContinuamDepoisDaLeitura() throws Exception {
        Element raiz = raiz();
        DemandaXml xml = new DemandaXml();
        Demanda pendente = new Demanda(TipoProduto.HIDRATANTE, 0, 0);
        Demanda cancelada = new Demanda(TipoProduto.ESFOLIANTE, 3, 9);
        cancelada.cancelar();
        xml.escrever(raiz, List.of(pendente, cancelada));
        List<Demanda> fila = xml.ler(unico(raiz, "demandas"));
        assertEquals(TipoProduto.HIDRATANTE, fila.get(0).getTipoProduto());
        fila.get(0).atualizarQuantidade(2);
        assertEquals(StatusDemanda.CANCELADA, fila.get(1).getStatus());
        assertThrows(DemandaInvalidaException.class, fila.get(1)::iniciarProducao);
        assertEquals(0, pendente.getQuantidadeProdutos());
    }

    @Test
    void demandaConcluidaNaoPodeGuardarQuantidadePendente() throws Exception {
        Element raiz = raiz();
        DemandaXml xml = new DemandaXml();
        xml.escrever(raiz, List.of(new Demanda(TipoProduto.HIDRATANTE, 1, 3)));
        Element demandas = unico(raiz, "demandas");
        unico(demandas, "demanda").setAttribute("status", "CONCLUIDA");
        assertThrows(DemandaInvalidaException.class, () -> xml.ler(demandas));
    }

    private Element raiz() throws Exception {
        Document documento = new DocumentoXml().criar();
        Element raiz = documento.createElement("planta");
        documento.appendChild(raiz);
        return raiz;
    }
}
