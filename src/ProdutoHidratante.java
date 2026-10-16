public class ProdutoHidratante extends Produto {
    public ProdutoHidratante(String id, String nome, double quantidadeMateriaPrimaPorUnidade) {
        super(id, nome, quantidadeMateriaPrimaPorUnidade, 0.5);
    }

    @Override
    public void processar() {
        setStatus("Em processamento");
    }

    @Override
    public double calcularTempoProducao() {
        return 5.0;
    }

    @Override
    public TipoProduto getTipo() {
        return TipoProduto.HIDRATANTE;
    }
}
