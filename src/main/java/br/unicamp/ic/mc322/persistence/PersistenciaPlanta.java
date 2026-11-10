// Coordena o salvamento e a recuperacao do estado da ScentWare.
package br.unicamp.ic.mc322.persistence;

import br.unicamp.ic.mc322.exception.PersistenciaException;
import br.unicamp.ic.mc322.service.GerenciadorProducao;
import br.unicamp.ic.mc322.persistence.conversores.PlantaXml;
import br.unicamp.ic.mc322.persistence.xml.DocumentoXml;
import java.io.IOException;
import java.nio.file.Path;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * Coordena o salvamento e a recuperacao do estado da ScentWare.
 * @author Luana Oliveira e Roberta Santos
 */
public class PersistenciaPlanta {
    private final Path arquivo;
    private final PlantaXml plantas = new PlantaXml();
    private final DocumentoXml documentos = new DocumentoXml();

    /**
     * Define o arquivo usado nas duas operacoes.
     * @param arquivo caminho do XML, podendo ainda nao existir
     */
    public PersistenciaPlanta(Path arquivo) {
        if (arquivo == null) throw new IllegalArgumentException("Informe o arquivo de persistencia.");
        this.arquivo = arquivo.toAbsolutePath();
    }

    /**
     * Valida o estado e pede a gravacao do documento.
     * @param fabrica fabrica que sera salva
     * @throws PersistenciaException se os dados ou a escrita forem invalidos
     */
    public void salvar(GerenciadorProducao fabrica) {
        try {
            Document documento = plantas.escrever(fabrica);
            // Confere tudo antes de tocar no arquivo anterior.
            plantas.ler(documento.getDocumentElement());
            documentos.salvar(documento, arquivo);
        } catch (IOException | ParserConfigurationException | TransformerException | RuntimeException e) {
            throw new PersistenciaException("Nao foi possivel salvar a planta em " + arquivo + ". " + e.getMessage(), e);
        }
    }

    /**
     * Recupera outra fabrica sem alterar o estado em uso.
     * @return fabrica validada pelos conversores e pelo dominio
     * @throws PersistenciaException se o arquivo faltar ou tiver dados invalidos
     */
    public GerenciadorProducao carregar() {
        try {
            return plantas.ler(documentos.ler(arquivo).getDocumentElement());
        } catch (IOException | SAXException | ParserConfigurationException | RuntimeException e) {
            throw new PersistenciaException("Nao foi possivel carregar a planta de " + arquivo
                    + ". Confira se o arquivo existe e contem um XML valido. " + e.getMessage(), e);
        }
    }

}
