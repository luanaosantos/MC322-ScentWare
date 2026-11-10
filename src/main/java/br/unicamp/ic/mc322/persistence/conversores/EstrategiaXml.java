// Guarda os nomes das estrategias aceitas no XML.
package br.unicamp.ic.mc322.persistence.conversores;

import br.unicamp.ic.mc322.strategy.*;

/**
 * Guarda os nomes das estrategias aceitas no XML.
 * @author Luana Oliveira e Roberta Santos
 */
public class EstrategiaXml {
    /**
     * Usa nomes fixos no XML, sem depender de nomes completos de classes Java.
     * @param estrategia regra atual da fabrica
     * @return nome usado no XML
     * @throws IllegalArgumentException se a estrategia nao for suportada
     */
    public String escrever(EstrategiaProducao estrategia) {
        if (estrategia instanceof EstrategiaOrdemChegada) return "ORDEM_CHEGADA";
        if (estrategia instanceof EstrategiaMaiorDemanda) return "MAIOR_DEMANDA";
        if (estrategia instanceof EstrategiaMaximoProdutos) return "MAXIMO_PRODUTOS";
        throw new IllegalArgumentException("Estrategia nao suportada pelo XML.");
    }

    /**
     * Recupera uma das regras ja usadas pela fabrica.
     * @param tipo nome salvo no XML
     * @return estrategia correspondente
     * @throws IllegalArgumentException se o nome for desconhecido
     */
    public EstrategiaProducao ler(String tipo) {
        switch (tipo) {
            case "ORDEM_CHEGADA": return new EstrategiaOrdemChegada();
            case "MAIOR_DEMANDA": return new EstrategiaMaiorDemanda();
            case "MAXIMO_PRODUTOS": return new EstrategiaMaximoProdutos();
            default: throw new IllegalArgumentException("Estrategia desconhecida.");
        }
    }
}
