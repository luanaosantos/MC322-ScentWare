// Testes do estoque representado no XML.
package br.unicamp.ic.mc322.persistence.conversores;

import br.unicamp.ic.mc322.model.MateriaPrima;
import br.unicamp.ic.mc322.persistence.xml.DocumentoXml;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import static br.unicamp.ic.mc322.persistence.xml.ElementosXml.*;
import static org.junit.jupiter.api.Assertions.*;

class MateriaPrimaXmlTest {
    @Test
    void estoqueZeroPodeSerRecuperadoESerReposto() throws Exception {
        Element raiz = raiz();
        MateriaPrimaXml xml = new MateriaPrimaXml();
        xml.escrever(raiz, new MateriaPrima("MP1", "Oleo", 0, "mL", 0.1));
        MateriaPrima recuperada = xml.ler(unico(raiz, "materiaPrima"));
        recuperada.adicionarEstoque(10);
        assertEquals(10, recuperada.getQuantidade());
        assertEquals("mL", recuperada.getUnidade());
        assertEquals(0.1, recuperada.getCustoPorUnidade());
    }

    @Test
    void estoqueNegativoDoArquivoNaoCriaMateriaPrima() throws Exception {
        Element raiz = raiz();
        MateriaPrimaXml xml = new MateriaPrimaXml();
        xml.escrever(raiz, new MateriaPrima("MP1", "Oleo", 10, "mL", 0.1));
        Element materia = unico(raiz, "materiaPrima");
        materia.setAttribute("quantidade", "-1");
        assertThrows(IllegalArgumentException.class, () -> xml.ler(materia));
    }

    private Element raiz() throws Exception {
        Document documento = new DocumentoXml().criar();
        Element raiz = documento.createElement("planta");
        documento.appendChild(raiz);
        return raiz;
    }
}
