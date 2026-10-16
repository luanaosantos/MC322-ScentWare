public class ProdutoEsfoliante extends Produto {
    public ProdutoEsfoliante(String id, String nome, double quantidadeMateriaPrimaPorUnidade) {
        super(id, nome, quantidadeMateriaPrimaPorUnidade, 0.9);
    }

    @Override
    public void processar() {
        setStatus("Em processamento");
    }

    @Override
    public double calcularTempoProducao() {
        return 9.0;
    }

    @Override
    public TipoProduto getTipo() {
        return TipoProduto.ESFOLIANTE;
    }
}
