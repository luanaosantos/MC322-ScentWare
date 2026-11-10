// Converte a fila de demandas sem mudar a ordem dos pedidos.
package br.unicamp.ic.mc322.persistence.conversores;

import br.unicamp.ic.mc322.model.*;
import java.util.ArrayList;
import org.w3c.dom.Element;
import static br.unicamp.ic.mc322.persistence.xml.ElementosXml.*;
import java.util.List;

/**
 * Converte a fila de demandas sem mudar a ordem dos pedidos.
 * @author Luana Oliveira e Roberta Santos
 */
public class DemandaXml {

    /**
     * Adiciona a fila e os estados das demandas.
     * @param pai elemento da planta
     * @param fila demandas na ordem de registro
     */
    public void escrever(Element pai, List<Demanda> fila) {
        Element demandas = adicionar(pai, "demandas");
        for (Demanda demanda : fila) {
            atributos(adicionar(demandas, "demanda"), "tipo", demanda.getTipoProduto().name(),
                    "quantidade", demanda.getQuantidadeProdutos(), "custo", demanda.getCustoTotal(),
                    "status", demanda.getStatus().name());
        }
    }

    /**
     * Reconstroi a fila, validando cada estado pelo dominio.
     * @param elemento secao demandas
     * @return demandas recuperadas na mesma ordem
     */
    public ArrayList<Demanda> ler(Element elemento) {
        ArrayList<Demanda> demandas = new ArrayList<>();
        for (Element demanda : filhos(elemento, "demanda")) {
            conferirFilhos(demanda);
            demandas.add(Demanda.restaurar(TipoProduto.valueOf(valor(demanda, "tipo")), inteiro(demanda, "quantidade"),
                    numero(demanda, "custo"), StatusDemanda.valueOf(valor(demanda, "status"))));
        }
        return demandas;
    }

}
