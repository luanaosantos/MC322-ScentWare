/**
 * Representa a receita de creme de mãos com tempo e qualidade próprios.
 *
 * @author Luana Oliveira
 * @author Roberta Santos
 */
public class ProdutoCremeDeMaos extends Produto {
    /**
     * Cria uma unidade de creme de mãos pra passar pela linha.
     * @param id identificação do produto
     * @param nome nome do cosmético
     * @param quantidadeMateriaPrimaPorUnidade consumo positivo por unidade
     * @throws IllegalArgumentException se os dados forem inválidos, conforme a classe Produto
     */
    public ProdutoCremeDeMaos(String id, String nome, double quantidadeMateriaPrimaPorUnidade) {
        super(id, nome, quantidadeMateriaPrimaPorUnidade, 0.7);
    }

    /** Marca a unidade como em processamento. */
    @Override
    public void processar() {
        setStatus("Em processamento");
    }

    /**
     * Consulta o tempo fixo desta receita.
     * @return 7.0 unidades de tempo
     */
    @Override
    public double calcularTempoProducao() {
        return 7.0;
    }

    /**
     * Identifica a receita de creme de mãos.
     * @return tipo correspondente ao cosmético
     */
    @Override
    public TipoProduto getTipo() {
        return TipoProduto.CREME_DE_MAOS;
    }
}
