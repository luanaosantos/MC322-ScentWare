/**
 * Tipos de cosmético que a fábrica consegue produzir.
 *
 * @author Luana Oliveira
 * @author Roberta Santos
 */
public enum TipoProduto {
    /** Creme de mãos, com seu modelo de produção. */
    CREME_DE_MAOS("Creme de mãos"),
    /** Esfoliante corporal, com seu modelo de produção. */
    ESFOLIANTE("Esfoliante"),
    /** Hidratante corporal, com seu modelo de produção. */
    HIDRATANTE("Hidratante");

    private final String nome;

    /**
     * Guarda o nome que vai aparecer nos menus e relatórios.
     *
     * @param nome nome de exibição do tipo
     */
    TipoProduto(String nome){
        this.nome = nome;
    }
    /**
     * Consulta o nome legível do tipo de produto.
     *
     * @return nome de exibição do tipo
     */
    public String getNome(){
        return nome;
    }
}
