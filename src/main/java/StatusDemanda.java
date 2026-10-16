// Estados q uma demanda pode ter.

/**
 * Estados usados pra acompanhar a demanda.
 *
 * @author Luana Oliveira
 * @author Roberta Santos
 */
public enum StatusDemanda {
    /** Espera na fila; precisa ter itens pra produzir. */
    PENDENTE("Pendente"),
    /** Esta passando pelas maquinas. */
    EM_PRODUCAO("Em produção"),
    /** Todos os itens foram aprovados. */
    CONCLUIDA("Concluída"),
    /** Foi cancelada e nao volta pra fila. */
    CANCELADA("Cancelada");

    private final String descricao;

    /**
     * Guarda o texto do status.
     *
     * @param descricao descricao legivel do estado
     */
    StatusDemanda(String descricao) {
        this.descricao = descricao;
    }

    /**
     * Retorna o texto do status.
     *
     * @return descricao usada nos menus e relatorios
     */
    public String getDescricao() {
        return descricao;
    }
}
