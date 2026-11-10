// Testes dos arquivos XML, separados dos dados reais da fabrica.
package br.unicamp.ic.mc322.persistence.xml;

import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import static org.junit.jupiter.api.Assertions.*;

class DocumentoXmlTest {
    @TempDir Path pasta;

    @Test
    void escritaPreservaCaracteresEAtualizaArquivo() throws Exception {
        DocumentoXml xml = new DocumentoXml();
        Document documento = xml.criar();
        documento.appendChild(documento.createElement("planta"));
        documento.getDocumentElement().setAttribute("nome", "Oleo & creme <teste>");
        Path arquivo = pasta.resolve("dados/planta.xml");
        xml.salvar(documento, arquivo);
        assertEquals("Oleo & creme <teste>", xml.ler(arquivo).getDocumentElement().getAttribute("nome"));
        documento.getDocumentElement().setAttribute("nome", "Outra fabrica");
        xml.salvar(documento, arquivo);
        assertEquals("Outra fabrica", xml.ler(arquivo).getDocumentElement().getAttribute("nome"));
        try (var arquivos = Files.list(arquivo.getParent())) {
            assertEquals(1, arquivos.count());
        }
    }

    @Test
    void leituraRecusaDtdEArquivoMalformado() throws Exception {
        Path arquivo = pasta.resolve("planta.xml");
        DocumentoXml xml = new DocumentoXml();
        Files.writeString(arquivo, "<!DOCTYPE planta [<!ENTITY dado 'teste'>]><planta/>");
        assertThrows(SAXException.class, () -> xml.ler(arquivo));
        Files.writeString(arquivo, "<planta>");
        assertThrows(SAXException.class, () -> xml.ler(arquivo));
    }
}
