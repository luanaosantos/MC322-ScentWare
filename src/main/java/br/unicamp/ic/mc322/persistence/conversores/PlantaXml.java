// Reune as secoes do XML e valida as relacoes da fabrica.
package br.unicamp.ic.mc322.persistence.conversores;

import br.unicamp.ic.mc322.model.*;
import br.unicamp.ic.mc322.service.GerenciadorProducao;
import br.unicamp.ic.mc322.persistence.xml.DocumentoXml;
import javax.xml.parsers.ParserConfigurationException;
import java.util.ArrayList;
import org.w3c.dom.*;
import static br.unicamp.ic.mc322.persistence.xml.ElementosXml.*;

/**
 * Reune as secoes do XML e valida as relacoes da fabrica.
 * @author Luana Oliveira e Roberta Santos
 */
public class PlantaXml {
    private final DocumentoXml documentos = new DocumentoXml();
    private final MateriaPrimaXml materias = new MateriaPrimaXml();
    private final ProdutoXml produtos = new ProdutoXml();
    private final MaquinaXml maquinas = new MaquinaXml();
    private final DemandaXml demandas = new DemandaXml();
    private final EstrategiaXml estrategias = new EstrategiaXml();

    /**
     * Monta as secoes do XML usando os conversores de cada parte.
     * @param fabrica estado que sera salvo
     * @return documento ainda em memoria
     * @throws ParserConfigurationException se nao for possivel criar o documento
     */
    public Document escrever(GerenciadorProducao fabrica) throws ParserConfigurationException {
        Document documento = documentos.criar();
        Element planta = documento.createElement("planta");
        documento.appendChild(planta);
        atributos(planta, "versao", 1, "budget", fabrica.getBudget(),
                "riscoInicial", fabrica.getRiscoInicialProduto(), "cenario", fabrica.getCenario().name(),
                "estrategia", estrategias.escrever(fabrica.getEstrategiaAtual()),
                "proximoLote", fabrica.getProximoLote(), "proximoProduto", fabrica.getProximoProduto());
        materias.escrever(planta, fabrica.getMateriaPrima());
        Element modelos = adicionar(planta, "modelos");
        for (Produto produto : fabrica.getProdutosModelos()) produtos.escrever(modelos, produto);
        Element linha = adicionar(planta, "linha");
        atributos(linha, "nome", fabrica.getLinha().getNome());
        for (Maquina maquina : fabrica.getLinha().getMaquinas()) maquinas.escrever(linha, maquina);
        demandas.escrever(planta, fabrica.getDemandas());
        Element armazem = adicionar(planta, "armazem");
        for (Produto produto : fabrica.getProdutosFabricados()) produtos.escrever(armazem, produto);
        return documento;
    }

    /**
     * Recupera outra fabrica depois de conferir as secoes e suas relacoes.
     * @param planta raiz do documento
     * @return fabrica reconstruida, sem alterar a que esta em uso
     */
    public GerenciadorProducao ler(Element planta) {
        if (!planta.getTagName().equals("planta") || !valor(planta, "versao").equals("1")) {
            throw new IllegalArgumentException("Estrutura ou versao da planta desconhecida.");
        }
        conferirFilhos(planta, "materiaPrima", "modelos", "linha", "demandas", "armazem");
        MateriaPrima materia = materias.ler(unico(planta, "materiaPrima"));
        ArrayList<Produto> modelos = produtos.ler(unico(planta, "modelos"));
        Element elementoLinha = unico(planta, "linha");
        LinhaDeProducao linha = new LinhaDeProducao(valor(elementoLinha, "nome"));
        for (Element maquina : filhos(elementoLinha, "maquina")) linha.adicionarMaquina(maquinas.ler(maquina));
        ArrayList<Demanda> fila = demandas.ler(unico(planta, "demandas"));
        return GerenciadorProducao.restaurar(materia, numero(planta, "budget"),
                modelos, estrategias.ler(valor(planta, "estrategia")),
                numero(planta, "riscoInicial"), linha, fila,
                produtos.ler(unico(planta, "armazem")), inteiro(planta, "proximoLote"),
                inteiro(planta, "proximoProduto"), Cenario.valueOf(valor(planta, "cenario")));
    }

}
