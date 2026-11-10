// Consulta e valida os elementos e atributos do XML.
package br.unicamp.ic.mc322.persistence.xml;

import java.util.ArrayList;
import java.util.List;
import org.w3c.dom.*;

/**
 * Consulta e valida os elementos e atributos do XML.
 * @author Luana Oliveira e Roberta Santos
 */
public class ElementosXml {
    /**
     * Adiciona um elemento no documento do pai.
     * @param pai elemento que recebera o filho
     * @param nome nome do novo elemento
     * @return elemento adicionado
     */
    public static Element adicionar(Element pai, String nome) {
        Element elemento = pai.getOwnerDocument().createElement(nome);
        pai.appendChild(elemento);
        return elemento;
    }

    /**
     * Grava pares de nome e valor como atributos.
     * @param elemento elemento que recebera os atributos
     * @param pares nomes e valores alternados
     */
    public static void atributos(Element elemento, Object... pares) {
        for (int i = 0; i < pares.length; i += 2) elemento.setAttribute(pares[i].toString(), pares[i + 1].toString());
    }

    /**
     * Exige um atributo preenchido, inclusive para valores numericos.
     * @param elemento elemento consultado
     * @param nome atributo obrigatorio
     * @return valor preenchido
     * @throws IllegalArgumentException se faltar o atributo
     */
    public static String valor(Element elemento, String nome) {
        String valor = elemento.getAttribute(nome);
        if (valor.isBlank()) throw new IllegalArgumentException("Falta o atributo " + nome + " em " + elemento.getTagName() + ".");
        return valor;
    }

    /**
     * Le um numero; a faixa permitida e conferida pelo dominio.
     * @param elemento elemento consultado
     * @param nome nome do atributo
     * @return valor numerico
     * @throws IllegalArgumentException se faltar o atributo ou nao for numero
     */
    public static double numero(Element elemento, String nome) { return Double.parseDouble(valor(elemento, nome)); }

    /**
     * Le um inteiro sem aceitar arredondamento.
     * @param elemento elemento consultado
     * @param nome nome do atributo
     * @return valor inteiro
     * @throws IllegalArgumentException se o valor nao for inteiro
     */
    public static int inteiro(Element elemento, String nome) { return Integer.parseInt(valor(elemento, nome)); }

    /**
     * Rejeita textos que nao sejam true ou false.
     * @param elemento elemento consultado
     * @param nome nome do atributo
     * @return valor logico
     * @throws IllegalArgumentException se o texto nao for true ou false
     */
    public static boolean logico(Element elemento, String nome) {
        String texto = valor(elemento, nome);
        if (!texto.equals("true") && !texto.equals("false")) throw new IllegalArgumentException("Valor logico invalido: " + nome);
        return Boolean.parseBoolean(texto);
    }

    /**
     * Devolve apenas filhos diretos e rejeita elementos desconhecidos.
     * @param pai secao consultada
     * @param nome nome permitido para os filhos
     * @return filhos na ordem do documento
     * @throws IllegalArgumentException se houver conteudo inesperado
     */
    public static List<Element> filhos(Element pai, String nome) {
        conferirFilhos(pai, nome);
        ArrayList<Element> lista = new ArrayList<>();
        for (Node no = pai.getFirstChild(); no != null; no = no.getNextSibling()) {
            if (no instanceof Element) lista.add((Element) no);
        }
        return lista;
    }

    /**
     * Exige exatamente uma ocorrencia de uma secao obrigatoria.
     * @param pai elemento que contem a secao
     * @param nome nome da secao obrigatoria
     * @return secao encontrada
     * @throws IllegalArgumentException se a secao faltar ou estiver repetida
     */
    public static Element unico(Element pai, String nome) {
        Element resultado = null;
        for (Node no = pai.getFirstChild(); no != null; no = no.getNextSibling()) {
            if (no instanceof Element && ((Element) no).getTagName().equals(nome)) {
                if (resultado != null) throw new IllegalArgumentException("Secao repetida: " + nome);
                resultado = (Element) no;
            }
        }
        if (resultado == null) throw new IllegalArgumentException("Falta a secao " + nome + ".");
        return resultado;
    }

    /**
     * Recusa estrutura inesperada em vez de ignorar parte dos dados.
     * @param pai elemento que sera conferido
     * @param permitidos nomes dos filhos aceitos
     * @throws IllegalArgumentException se houver elementos ou texto inesperados
     */
    public static void conferirFilhos(Element pai, String... permitidos) {
        for (Node no = pai.getFirstChild(); no != null; no = no.getNextSibling()) {
            if (no instanceof Element && !List.of(permitidos).contains(((Element) no).getTagName())) {
                throw new IllegalArgumentException("Elemento desconhecido em " + pai.getTagName() + ".");
            }
            if ((no.getNodeType() == Node.TEXT_NODE || no.getNodeType() == Node.CDATA_SECTION_NODE) && !no.getTextContent().isBlank()) {
                throw new IllegalArgumentException("Texto inesperado em " + pai.getTagName() + ".");
            }
        }
    }
}
