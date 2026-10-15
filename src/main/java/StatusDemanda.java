/**
 * Estados da demanda ao longo da fila e da produção.
 *
 * @author Luana Oliveira
 * @author Roberta Santos
 */
public enum StatusDemanda {
    /** A demanda está na fila; só entra na linha se houver itens pendentes. */
    PENDENTE("Pendente"),
    /** O lote da demanda já começou a passar pelas máquinas. */
    EM_PRODUCAO("Em produção"),
    /** Todos os itens pedidos foram aprovados e não há quantidade pendente. */
    CONCLUIDA("Concluída"),
    /** A demanda foi cancelada e não deve voltar pra fila. */
    CANCELADA("Cancelada");

    private final String descricao;

    /**
     * Guarda o texto do estado pra exibição.
     *
     * @param descricao descrição legível do estado
     */
    StatusDemanda(String descricao) {
        this.descricao = descricao;
    }

    /**
     * Consulta a descrição do estado atual.
     *
     * @return descrição usada nos menus e relatórios
     */
    public String getDescricao() {
        return descricao;
    }
}
