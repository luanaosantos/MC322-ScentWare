// Converte os dados do estoque de materia-prima.
package br.unicamp.ic.mc322.persistence.conversores;

import br.unicamp.ic.mc322.model.*;
import org.w3c.dom.Element;
import static br.unicamp.ic.mc322.persistence.xml.ElementosXml.*;

/**
 * Converte os dados do estoque de materia-prima.
 * @author Luana Oliveira e Roberta Santos
 */
public class MateriaPrimaXml {

    /**
     * Adiciona o estoque ao XML da planta.
     * @param pai elemento da planta
     * @param materia estoque que sera salvo
     */
    public void escrever(Element pai, MateriaPrima materia) {
        atributos(adicionar(pai, "materiaPrima"), "id", materia.getId(), "nome", materia.getNome(),
                "quantidade", materia.getQuantidade(), "unidade", materia.getUnidade(), "custo", materia.getCustoPorUnidade());
    }

    /**
     * Recupera o estoque e confere os valores pelo construtor do dominio.
     * @param elemento elemento materiaPrima
     * @return outro objeto com o estoque salvo
     */
    public MateriaPrima ler(Element elemento) {
        conferirFilhos(elemento);
        return new MateriaPrima(valor(elemento, "id"), valor(elemento, "nome"), numero(elemento, "quantidade"),
                valor(elemento, "unidade"), numero(elemento, "custo"));
    }

}
