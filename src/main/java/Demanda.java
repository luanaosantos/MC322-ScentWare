// Pedido da fabrica, com qtd, custo e status.

/**
 * Guarda o pedido e controla seu status.
 *
 * @author Luana Oliveira
 * @author Roberta Santos
 */
public class Demanda {
    private TipoProduto tipoProduto;
    private int quantidadeProdutos;
    private StatusDemanda status;
    private double custoTotal;

    /**
     * Cria a demanda pendente. Qtd zero fica esperando uma atualizacao.
     *
     * @param tipoProduto tipo do produto pedido
     * @param quantidadeProdutos qtd de itens ainda necessarios
     * @param custoTotal custo estimado dos itens pendentes
     * @throws DemandaInvalidaException se o tipo ou a qtd forem invalidos
     * @throws IllegalArgumentException se o custo nao for finito e nao negativo
     */
    public Demanda(
            TipoProduto tipoProduto,
            int quantidadeProdutos,
            double custoTotal
    ) {
        if (tipoProduto == null || quantidadeProdutos < 0) {
            throw new DemandaInvalidaException("Informe um tipo de produto e uma quantidade não negativa.");
        }
        if (!Double.isFinite(custoTotal) || custoTotal < 0) {
            throw new IllegalArgumentException("O custo precisa ser finito e não negativo.");
        }

        this.tipoProduto = tipoProduto;
        this.quantidadeProdutos = quantidadeProdutos;
        this.status = StatusDemanda.PENDENTE;
        this.custoTotal = custoTotal;
    }

    /**
     * Muda a qtd da demanda pendente. O gerenciador recalcula o custo.
     *
     * @param novaQuantidade nova qtd pendente, podendo ser zero
     * @return true depois da atualizacao
     * @throws DemandaInvalidaException se a qtd ou o estado forem invalidos
     */
    public boolean atualizarQuantidade(int novaQuantidade) {
        if (novaQuantidade < 0 || status != StatusDemanda.PENDENTE) {
            throw new DemandaInvalidaException("Só uma demanda pendente pode receber uma quantidade não negativa.");
        }

        quantidadeProdutos = novaQuantidade;
        status = StatusDemanda.PENDENTE;
        return true;
    }

    /**
     * Calcula quanto o pedido vai usar de materia-prima.
     *
     * @param produto modelo do mesmo tipo da demanda
     * @return qtd de materia-prima necessaria
     * @throws IllegalArgumentException se o modelo for incompativel ou o total nao for finito
     */
    public double calcularMateriaPrimaNecessaria(Produto produto) {
        if (produto == null || produto.getTipo() != tipoProduto) {
            throw new IllegalArgumentException("O produto precisa ter o mesmo tipo da demanda.");
        }
        double quantidadeNecessaria = quantidadeProdutos * produto.getQuantidadeMateriaPrimaPorUnidade();
        if (!Double.isFinite(quantidadeNecessaria)) {
            throw new IllegalArgumentException("A quantidade de matéria-prima ultrapassa o limite permitido.");
        }
        return quantidadeNecessaria;
    }

    /**
     * Confere se a demanda tem itens pendentes e cabe no budget.
     *
     * @param budget budget atual
     * @return true se a demanda puder ser paga com o budget
     * @throws IllegalArgumentException se o orcamento nao for finito e nao negativo
     */
    public boolean viabilidadeFinanceira(double budget) {
        if (!Double.isFinite(budget) || budget < 0) {
            throw new IllegalArgumentException("O orçamento precisa ser finito e não negativo.");
        }
        return status == StatusDemanda.PENDENTE && quantidadeProdutos > 0 && custoTotal <= budget;
    }

    /**
     * Inicia a demanda se ela estiver pendente e tiver itens.
     *
     * @return true depois de iniciar
     * @throws DemandaInvalidaException se nao houver itens ou o estado for incompativel
     */
    public boolean iniciarProducao() {
        if (status != StatusDemanda.PENDENTE || quantidadeProdutos <= 0) {
            throw new DemandaInvalidaException("Só uma demanda pendente com quantidade positiva pode iniciar a produção.");
        }
        status = StatusDemanda.EM_PRODUCAO;
        return true;
    }

    /**
     * Volta a demanda pra fila depois da tentativa de producao.
     *
     * @throws DemandaInvalidaException se a demanda nao estiver em producao
     */
    public void voltarParaPendente() {
        if (status != StatusDemanda.EM_PRODUCAO) {
            throw new DemandaInvalidaException("Só uma demanda em produção pode voltar para pendente.");
        }
        status = StatusDemanda.PENDENTE;
    }

    /**
     * Conclui a demanda e zera a qtd e o custo pendentes.
     *
     * @throws DemandaInvalidaException se a demanda nao estiver em producao
     */
    public void concluir() {
        if (status != StatusDemanda.EM_PRODUCAO) {
            throw new DemandaInvalidaException("Só uma demanda em produção pode ser concluída.");
        }
        quantidadeProdutos = 0;
        custoTotal = 0.0;
        status = StatusDemanda.CONCLUIDA;
    }

    /**
     * Cancela a demanda e mantem seus dados pra consulta.
     *
     * @throws DemandaInvalidaException se a demanda ja estiver encerrada
     */
    public void cancelar() {
        if (status != StatusDemanda.PENDENTE && status != StatusDemanda.EM_PRODUCAO) {
            throw new DemandaInvalidaException("Só uma demanda pendente ou em produção pode ser cancelada.");
        }
        status = StatusDemanda.CANCELADA;
    }

    /**
     * Retorna o tipo pedido na demanda.
     *
     * @return tipo do produto
     */
    public TipoProduto getTipoProduto() {
        return tipoProduto;
    }

    /**
     * Retorna a qtd q falta produzir.
     *
     * @return qtd pendente
     */
    public int getQuantidadeProdutos() {
        return quantidadeProdutos;
    }

    /**
     * Retorna o status atual.
     *
     * @return estado da demanda
     */
    public StatusDemanda getStatus() {
        return status;
    }

    /**
     * Retorna o custo dos itens pendentes.
     *
     * @return custo total atual
     */
    public double getCustoTotal() {
        return custoTotal;
    }

    /**
     * Atualiza o custo dos itens pendentes.
     *
     * @param custoTotal custo finito e nao negativo
     * @throws IllegalArgumentException se o custo estiver fora da faixa
     */
    public void setCustoTotal(double custoTotal) {
        if (!Double.isFinite(custoTotal) || custoTotal < 0) {
            throw new IllegalArgumentException("O custo precisa ser finito e não negativo.");
        }
        this.custoTotal = custoTotal;
    }
}
