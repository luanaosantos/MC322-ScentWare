// Converte modelos e produtos aprovados para XML e de volta.
package br.unicamp.ic.mc322.persistence.conversores;

import br.unicamp.ic.mc322.model.*;
import java.util.ArrayList;
import org.w3c.dom.Element;
import static br.unicamp.ic.mc322.persistence.xml.ElementosXml.*;

/**
 * Converte modelos e produtos aprovados para XML e de volta.
 * @author Luana Oliveira e Roberta Santos
 */
public class ProdutoXml {
    /**
     * Escreve os dados comuns dos modelos e dos produtos aprovados.
     * @param pai secao de modelos ou armazem
     * @param produto produto que sera salvo
     */
    public void escrever(Element pai, Produto produto) {
        atributos(adicionar(pai, "produto"), "tipo", produto.getTipo().name(), "id", produto.getId(),
                "nome", produto.getNome(), "consumo", produto.getQuantidadeMateriaPrimaPorUnidade(),
                "status", produto.getStatus(), "risco", produto.getProbabilidadeFalhaAcumulada(), "lote", produto.getLote());
    }

    /**
     * Cria cada produto pela subclasse que define sua qualidade e tempo.
     * @param pai secao de modelos ou armazem
     * @return produtos reconstruidos na ordem do XML
     * @throws IllegalArgumentException se os dados do produto forem invalidos
     */
    public ArrayList<Produto> ler(Element pai) {
        ArrayList<Produto> produtos = new ArrayList<>();
        for (Element elemento : filhos(pai, "produto")) {
            conferirFilhos(elemento);
            TipoProduto tipo = TipoProduto.valueOf(valor(elemento, "tipo"));
            String id = valor(elemento, "id");
            String nome = valor(elemento, "nome");
            double consumo = numero(elemento, "consumo");
            Produto produto;
            switch (tipo) {
                case HIDRATANTE: produto = new ProdutoHidratante(id, nome, consumo); break;
                case CREME_DE_MAOS: produto = new ProdutoCremeDeMaos(id, nome, consumo); break;
                case ESFOLIANTE: produto = new ProdutoEsfoliante(id, nome, consumo); break;
                default: throw new IllegalArgumentException("Tipo de produto desconhecido.");
            }
            produto.restaurarEstado(valor(elemento, "status"), numero(elemento, "risco"), inteiro(elemento, "lote"));
            produtos.add(produto);
        }
        return produtos;
    }
}
