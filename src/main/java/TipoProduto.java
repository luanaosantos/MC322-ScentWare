// Tipos de produto da fabrica.

/**
 * Produtos q a fabrica pode fazer.
 *
 * @author Luana Oliveira
 * @author Roberta Santos
 */
public enum TipoProduto {
    /** Creme de maos. */
    CREME_DE_MAOS("Creme de mãos"),
    /** Esfoliante corporal. */
    ESFOLIANTE("Esfoliante"),
    /** Hidratante corporal. */
    HIDRATANTE("Hidratante");

    private final String nome;

    /**
     * Guarda o nome do produto pro menu.
     *
     * @param nome nome no menu do tipo
     */
    TipoProduto(String nome){
        this.nome = nome;
    }
    /**
     * Retorna o nome.
     *
     * @return nome no menu do tipo
     */
    public String getNome(){
        return nome;
    }
}
