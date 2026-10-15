/**
 * Guarda a qtd ainda pendente e controla os estados de uma demanda.
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
     * Cria uma demanda pendente; qtd zero fica aguardando uma atualização.
     *
     * @param tipoProduto tipo do produto pedido
     * @param quantidadeProdutos qtd de itens ainda necessários
     * @param custoTotal estimativa dos itens pendentes
     * @throws DemandaInvalidaException se o tipo ou a qtd forem inválidos
     * @throws IllegalArgumentException se o custo não for finito e não negativo
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
     * Ajusta uma demanda pendente sem reabrir pedidos já encerrados.
     * O gerenciador recalcula o custo conforme o modelo do produto.
     *
     * @param novaQuantidade nova qtd pendente, podendo ser zero
     * @return true depois da atualização
     * @throws DemandaInvalidaException se a qtd ou o estado forem inválidos
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
     * Calcula o consumo total sem mexer no estoque.
     *
     * @param produto modelo do mesmo tipo da demanda
     * @return qtd de matéria-prima necessária
     * @throws IllegalArgumentException se o modelo for incompatível ou o total não for finito
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
     * Confere se há itens pendentes e dinheiro pra estimativa atual.
     *
     * @param budget orçamento disponível
     * @return true se a demanda puder ser financiada
     * @throws IllegalArgumentException se o orçamento não for finito e não negativo
     */
    public boolean viabilidadeFinanceira(double budget) {
        if (!Double.isFinite(budget) || budget < 0) {
            throw new IllegalArgumentException("O orçamento precisa ser finito e não negativo.");
        }
        return status == StatusDemanda.PENDENTE && quantidadeProdutos > 0 && custoTotal <= budget;
    }

    /**
     * Coloca uma demanda pendente com itens na linha de produção.
     *
     * @return true depois de iniciar
     * @throws DemandaInvalidaException se não houver itens ou o estado for incompatível
     */
    public boolean iniciarProducao() {
        if (status != StatusDemanda.PENDENTE || quantidadeProdutos <= 0) {
            throw new DemandaInvalidaException("Só uma demanda pendente com quantidade positiva pode iniciar a produção.");
        }
        status = StatusDemanda.EM_PRODUCAO;
        return true;
    }

    /**
     * Devolve a demanda à fila pra atualizar os itens que faltaram.
     *
     * @throws DemandaInvalidaException se a demanda não estiver em produção
     */
    public void voltarParaPendente() {
        if (status != StatusDemanda.EM_PRODUCAO) {
            throw new DemandaInvalidaException("Só uma demanda em produção pode voltar para pendente.");
        }
        status = StatusDemanda.PENDENTE;
    }

    /**
     * Encerra a produção e zera qtd e custo, pois não restaram pendências.
     *
     * @throws DemandaInvalidaException se a demanda não estiver em produção
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
     * Cancela um pedido ativo, mantendo a qtd e o custo pra consulta.
     *
     * @throws DemandaInvalidaException se a demanda já estiver encerrada
     */
    public void cancelar() {
        if (status != StatusDemanda.PENDENTE && status != StatusDemanda.EM_PRODUCAO) {
            throw new DemandaInvalidaException("Só uma demanda pendente ou em produção pode ser cancelada.");
        }
        status = StatusDemanda.CANCELADA;
    }

    /**
     * Consulta o tipo solicitado.
     * @return tipo do produto
     */
    public TipoProduto getTipoProduto() {
        return tipoProduto;
    }

    /**
     * Consulta os itens ainda pedidos.
     * @return qtd pendente
     */
    public int getQuantidadeProdutos() {
        return quantidadeProdutos;
    }

    /**
     * Consulta a etapa atual do pedido.
     * @return estado da demanda
     */
    public StatusDemanda getStatus() {
        return status;
    }

    /**
     * Consulta a estimativa dos itens pendentes.
     * @return custo total atual
     */
    public double getCustoTotal() {
        return custoTotal;
    }

    /**
     * Atualiza a estimativa após recalcular os itens pendentes.
     *
     * @param custoTotal custo finito e não negativo
     * @throws IllegalArgumentException se o custo estiver fora da faixa
     */
    public void setCustoTotal(double custoTotal) {
        if (!Double.isFinite(custoTotal) || custoTotal < 0) {
            throw new IllegalArgumentException("O custo precisa ser finito e não negativo.");
        }
        this.custoTotal = custoTotal;
    }
}
