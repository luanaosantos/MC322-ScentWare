/**
 * Representa a receita de hidratante com tempo e qualidade próprios.
 *
 * @author Luana Oliveira
 * @author Roberta Santos
 */
public class ProdutoHidratante extends Produto {
    /**
     * Cria uma unidade de hidratante pra passar pela linha.
     * @param id identificação do produto
     * @param nome nome do cosmético
     * @param quantidadeMateriaPrimaPorUnidade consumo positivo por unidade
     * @throws IllegalArgumentException se os dados forem inválidos, conforme a classe Produto
     */
    public ProdutoHidratante(String id, String nome, double quantidadeMateriaPrimaPorUnidade) {
        super(id, nome, quantidadeMateriaPrimaPorUnidade, 0.5);
    }

    /** Marca a unidade como em processamento. */
    @Override
    public void processar() {
        setStatus("Em processamento");
    }

    /**
     * Consulta o tempo fixo desta receita.
     * @return 5.0 unidades de tempo
     */
    @Override
    public double calcularTempoProducao() {
        return 5.0;
    }

    /**
     * Identifica a receita de hidratante.
     * @return tipo correspondente ao cosmético
     */
    @Override
    public TipoProduto getTipo() {
        return TipoProduto.HIDRATANTE;
    }
}
