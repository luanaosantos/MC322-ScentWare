/**
 * Representa a receita de esfoliante com tempo e qualidade próprios.
 *
 * @author Luana Oliveira
 * @author Roberta Santos
 */
public class ProdutoEsfoliante extends Produto {
    /**
     * Cria uma unidade de esfoliante pra passar pela linha.
     * @param id identificação do produto
     * @param nome nome do cosmético
     * @param quantidadeMateriaPrimaPorUnidade consumo positivo por unidade
     * @throws IllegalArgumentException se os dados forem inválidos, conforme a classe Produto
     */
    public ProdutoEsfoliante(String id, String nome, double quantidadeMateriaPrimaPorUnidade) {
        super(id, nome, quantidadeMateriaPrimaPorUnidade, 0.9);
    }

    /** Marca a unidade como em processamento. */
    @Override
    public void processar() {
        setStatus("Em processamento");
    }

    /**
     * Consulta o tempo fixo desta receita.
     * @return 9.0 unidades de tempo
     */
    @Override
    public double calcularTempoProducao() {
        return 9.0;
    }

    /**
     * Identifica a receita de esfoliante.
     * @return tipo correspondente ao cosmético
     */
    @Override
    public TipoProduto getTipo() {
        return TipoProduto.ESFOLIANTE;
    }
}
