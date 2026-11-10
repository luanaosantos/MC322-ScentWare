// Testes dos tipos e do estado dos produtos recuperados.
package br.unicamp.ic.mc322.persistence.conversores;

import br.unicamp.ic.mc322.model.*;
import br.unicamp.ic.mc322.persistence.xml.DocumentoXml;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import static br.unicamp.ic.mc322.persistence.xml.ElementosXml.*;
import static org.junit.jupiter.api.Assertions.*;

class ProdutoXmlTest {
    @Test
    void tiposRecuperadosMantemComportamentoEEstadoProprios() throws Exception {
        Element produtos = raiz();
        ProdutoXml xml = new ProdutoXml();
        List<Produto> originais = List.of(new ProdutoHidratante("P1", "Hidratante", 20),
                new ProdutoCremeDeMaos("P2", "Creme", 30), new ProdutoEsfoliante("P3", "Esfoliante", 40));
        for (Produto produto : originais) {
            produto.restaurarEstado("Aprovado", 0.25, 2);
            xml.escrever(produtos, produto);
        }
        List<Produto> recuperados = xml.ler(produtos);
        for (int i = 0; i < originais.size(); i++) {
            Produto recuperado = recuperados.get(i);
            assertEquals(originais.get(i).getClass(), recuperado.getClass());
            assertEquals(originais.get(i).calcularTempoProducao(), recuperado.calcularTempoProducao());
            assertEquals(originais.get(i).getQualidade(), recuperado.getQualidade());
            assertEquals("Aprovado", recuperado.getStatus());
            assertEquals(0.25, recuperado.getProbabilidadeFalhaAcumulada());
            assertEquals(2, recuperado.getLote());
        }
        recuperados.get(0).setStatus("Rejeitado");
        assertEquals("Aprovado", originais.get(0).getStatus());
    }

    @Test
    void tipoDesconhecidoEConsumoInvalidoSaoRecusados() throws Exception {
        Element produtos = raiz();
        ProdutoXml xml = new ProdutoXml();
        xml.escrever(produtos, new ProdutoHidratante("P", "Hidratante", 20));
        Element produto = unico(produtos, "produto");
        produto.setAttribute("tipo", "DESCONHECIDO");
        assertThrows(IllegalArgumentException.class, () -> xml.ler(produtos));
        produto.setAttribute("tipo", "HIDRATANTE");
        produto.setAttribute("consumo", "NaN");
        assertThrows(IllegalArgumentException.class, () -> xml.ler(produtos));
    }

    private Element raiz() throws Exception {
        Document documento = new DocumentoXml().criar();
        Element raiz = documento.createElement("produtos");
        documento.appendChild(raiz);
        return raiz;
    }
}
