// Produto hidratante corporal.

/**
 * Define a qualidade e o tempo do hidratante.
 *
 * @author Luana Oliveira
 * @author Roberta Santos
 */
public class ProdutoHidratante extends Produto {
    /**
     * Cria um hidratante novo.
     *
     * @param id id do produto
     * @param nome nome do produto
     * @param quantidadeMateriaPrimaPorUnidade consumo positivo por unidade
     * @throws IllegalArgumentException se os dados forem invalidos, conforme a classe Produto
     */
    public ProdutoHidratante(String id, String nome, double quantidadeMateriaPrimaPorUnidade) {
        super(id, nome, quantidadeMateriaPrimaPorUnidade, 0.5);
    }

    /** Marca o produto como em processamento. */
    @Override
    public void processar() {
        setStatus("Em processamento");
    }

    /**
     * Retorna o tempo de producao desse produto.
     *
     * @return 5.0 unidades de tempo
     */
    @Override
    public double calcularTempoProducao() {
        return 5.0;
    }

    /**
     * Retorna o tipo do produto.
     *
     * @return tipo do mesmo tipo ao produto
     */
    @Override
    public TipoProduto getTipo() {
        return TipoProduto.HIDRATANTE;
    }
}
