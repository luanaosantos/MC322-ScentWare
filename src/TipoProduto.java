public enum TipoProduto {
    CREME_DE_MAOS("Creme de mãos"),
    ESFOLIANTE("Esfoliante"),
    HIDRATANTE("Hidratante");

    private final String nome;

    TipoProduto(String nome){
        this.nome = nome;
    }
    public String getNome(){
        return nome;
    }
}
