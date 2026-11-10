// Le e grava documentos XML, sem conhecer as regras da fabrica.
package br.unicamp.ic.mc322.persistence.xml;

import java.io.IOException;
import java.nio.file.*;
import javax.xml.XMLConstants;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Le e grava documentos XML, sem conhecer as regras da fabrica.
 * @author Luana Oliveira e Roberta Santos
 */
public class DocumentoXml {

    /**
     * Cria um documento vazio.
     * @return documento em memoria
     * @throws ParserConfigurationException se o leitor nao puder ser configurado
     */
    public Document criar() throws ParserConfigurationException {
        return criarLeitor().newDocument();
    }

    /**
     * Le um arquivo sem permitir DTD nem recursos externos.
     * @param arquivo caminho do XML
     * @return documento lido
     * @throws ParserConfigurationException se o leitor nao puder ser configurado
     * @throws SAXException se o XML estiver malformado
     * @throws IOException se a leitura falhar
     */
    public Document ler(Path arquivo) throws ParserConfigurationException, SAXException, IOException {
        return criarLeitor().parse(arquivo.toFile());
    }

    /**
     * Conclui a escrita em temporario antes de substituir o arquivo anterior.
     * @param documento XML que sera gravado
     * @param arquivo destino da escrita
     * @throws IOException se o arquivo nao puder ser gravado ou substituido
     * @throws TransformerException se a conversao para texto falhar
     */
    public void salvar(Document documento, Path arquivo) throws IOException, TransformerException {
        arquivo = arquivo.toAbsolutePath();
        Path temporario = null;
        try {
            Files.createDirectories(arquivo.getParent());
            temporario = Files.createTempFile(arquivo.getParent(), "scentware-", ".xml");
            TransformerFactory factory = TransformerFactory.newInstance();
            factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
            factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_STYLESHEET, "");
            Transformer escritor = factory.newTransformer();
            escritor.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            escritor.setOutputProperty(OutputKeys.INDENT, "yes");
            escritor.transform(new DOMSource(documento), new StreamResult(temporario.toFile()));
            try {
                Files.move(temporario, arquivo, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
            } catch (AtomicMoveNotSupportedException e) {
                Files.move(temporario, arquivo, StandardCopyOption.REPLACE_EXISTING);
            }
        } finally {
            if (temporario != null) {
                try { Files.deleteIfExists(temporario); } catch (IOException e) { /* Mantem o erro da gravacao. */ }
            }
        }
    }

    /** Cria o leitor sem permitir DTD nem acesso a arquivos externos. */
    private DocumentBuilder criarLeitor() throws ParserConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
        factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
        factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
        factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
        factory.setXIncludeAware(false);
        factory.setExpandEntityReferences(false);
        DocumentBuilder leitor = factory.newDocumentBuilder();
        leitor.setErrorHandler(new DefaultHandler() {
            @Override public void error(org.xml.sax.SAXParseException e) throws SAXException { throw e; }
            @Override public void fatalError(org.xml.sax.SAXParseException e) throws SAXException { throw e; }
        });
        return leitor;
    }
}
