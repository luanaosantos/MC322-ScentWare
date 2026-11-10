// Testes das regras comuns da estrutura XML.
package br.unicamp.ic.mc322.persistence.xml;

import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import static br.unicamp.ic.mc322.persistence.xml.ElementosXml.*;
import static org.junit.jupiter.api.Assertions.*;

class ElementosXmlTest {
    @Test
    void secaoObrigatoriaNaoPodeFaltarNemRepetir() throws Exception {
        Element raiz = raiz();
        assertThrows(IllegalArgumentException.class, () -> unico(raiz, "motor"));
        Element motor = adicionar(raiz, "motor");
        assertSame(motor, unico(raiz, "motor"));
        adicionar(raiz, "motor");
        assertThrows(IllegalArgumentException.class, () -> unico(raiz, "motor"));
    }

    @Test
    void textoCdataEFilhosDesconhecidosSaoRecusados() throws Exception {
        Element raiz = raiz();
        raiz.appendChild(raiz.getOwnerDocument().createCDATASection("dado perdido"));
        assertThrows(IllegalArgumentException.class, () -> conferirFilhos(raiz));
        raiz.setTextContent("   ");
        conferirFilhos(raiz);
        adicionar(raiz, "desconhecido");
        assertThrows(IllegalArgumentException.class, () -> filhos(raiz, "sensor"));
    }

    @Test
    void atributosAusentesEValoresIncompativeisNaoViraramPadrao() throws Exception {
        Element raiz = raiz();
        assertThrows(IllegalArgumentException.class, () -> numero(raiz, "saude"));
        atributos(raiz, "ligado", "talvez", "quantidade", "1.5");
        assertThrows(IllegalArgumentException.class, () -> logico(raiz, "ligado"));
        assertThrows(NumberFormatException.class, () -> inteiro(raiz, "quantidade"));
    }

    private Element raiz() throws Exception {
        Document documento = new DocumentoXml().criar();
        Element raiz = documento.createElement("planta");
        documento.appendChild(raiz);
        return raiz;
    }
}
