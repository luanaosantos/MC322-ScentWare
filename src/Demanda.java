public class Demanda {
    private TipoProduto tipoProduto;
    private int quantidadeProdutos;
    private StatusDemanda status;
    private double custoTotal;

    public Demanda(
            TipoProduto tipoProduto,
            int quantidadeProdutos,
            double custoTotal
    ) {
        if (quantidadeProdutos < 0) {
            throw new IllegalArgumentException("A quantidade de produtos não pode ser negativa.");
        }

        this.tipoProduto = tipoProduto;
        this.quantidadeProdutos = quantidadeProdutos;
        this.status = StatusDemanda.PENDENTE;
        this.custoTotal = custoTotal;
    }

    public boolean atualizarQuantidade(int novaQuantidade) {
        if (novaQuantidade < 0 || status == StatusDemanda.CANCELADA) {
            return false;
        }

        quantidadeProdutos = novaQuantidade;
        status = StatusDemanda.PENDENTE;
        return true;
    }

    public double calcularMateriaPrimaNecessaria(Produto produto) {
        return quantidadeProdutos * produto.getQuantidadeMateriaPrimaPorUnidade();
    }

    public boolean viabilidadeFinanceira(double budget) {
        return status == StatusDemanda.PENDENTE && quantidadeProdutos > 0 && custoTotal <= budget;
    }

    // Só uma demanda pendente e com itens a produzir pode entrar na linha
    public boolean iniciarProducao() {
        if (status != StatusDemanda.PENDENTE || quantidadeProdutos <= 0) {
            return false;
        }
        status = StatusDemanda.EM_PRODUCAO;
        return true;
    }

    public void voltarParaPendente() {
        if (status == StatusDemanda.EM_PRODUCAO) {
            status = StatusDemanda.PENDENTE;
        }
    }

    // Ao concluir, zeramos a quantidade porque não ficou nenhum item pendente
    public void concluir() {
        if (status == StatusDemanda.EM_PRODUCAO) {
            quantidadeProdutos = 0;
            status = StatusDemanda.CONCLUIDA;
        }
    }

    public void cancelar() {
        if (status == StatusDemanda.PENDENTE || status == StatusDemanda.EM_PRODUCAO) {
            status = StatusDemanda.CANCELADA;
        }
    }

    public TipoProduto getTipoProduto() {
        return tipoProduto;
    }

    public int getQuantidadeProdutos() {
        return quantidadeProdutos;
    }

    public StatusDemanda getStatus() {
        return status;
    }

    public double getCustoTotal() {
        return custoTotal;
    }

    public void setCustoTotal(double custoTotal) {
        this.custoTotal = custoTotal;
    }
}
