// Valores dos cenarios Ideal e Apocaliptico.

/**
 * Guarda os valores usados pra montar cada cenario.
 *
 * @author Luana Oliveira
 * @author Roberta Santos
 */
public enum Cenario {
    /** Mais budget, menos falhas e desgaste. */
    IDEAL("Ideal", 1000.0, 0.02, 0.05, 0.03, 0.05, 3.0, 3.0, 3.0),
    /** Menos budget, mais falhas e desgaste. */
    APOCALIPTICO("Apocalíptico", 100.0, 0.20, 0.45, 0.35, 0.45, 15.0, 15.0, 20.0);

    private final String nome;
    private final double budget;
    private final double riscoInicialProduto;
    private final double probabilidadeFalhaHomogeneizador;
    private final double probabilidadeFalhaEmpacotador;
    private final double probabilidadeFalhaInspecao;
    private final double maximoDesgasteHomogeneizador;
    private final double maximoDesgasteEmpacotador;
    private final double maximoDesgasteInspecao;

    /**
     * Guarda os valores usados pra montar o cenario.
     *
     * @param nome nome exibido no menu
     * @param budget budget inicial em reais
     * @param riscoInicialProduto risco aplicado a cada produto novo
     * @param probabilidadeFalhaHomogeneizador chance basica de falha do homogeneizador
     * @param probabilidadeFalhaEmpacotador chance basica de falha da empacotadora
     * @param probabilidadeFalhaInspecao chance basica de falha da inspecao
     * @param maximoDesgasteHomogeneizador desgaste maximo por uso do homogeneizador
     * @param maximoDesgasteEmpacotador desgaste maximo por uso da empacotadora
     * @param maximoDesgasteInspecao desgaste maximo por uso da inspecao
     */
    Cenario(
            String nome,
            double budget,
            double riscoInicialProduto,
            double probabilidadeFalhaHomogeneizador,
            double probabilidadeFalhaEmpacotador,
            double probabilidadeFalhaInspecao,
            double maximoDesgasteHomogeneizador,
            double maximoDesgasteEmpacotador,
            double maximoDesgasteInspecao
    ) {
        this.nome = nome;
        this.budget = budget;
        this.riscoInicialProduto = riscoInicialProduto;
        this.probabilidadeFalhaHomogeneizador = probabilidadeFalhaHomogeneizador;
        this.probabilidadeFalhaEmpacotador = probabilidadeFalhaEmpacotador;
        this.probabilidadeFalhaInspecao = probabilidadeFalhaInspecao;
        this.maximoDesgasteHomogeneizador = maximoDesgasteHomogeneizador;
        this.maximoDesgasteEmpacotador = maximoDesgasteEmpacotador;
        this.maximoDesgasteInspecao = maximoDesgasteInspecao;
    }

    /**
     * Retorna o budget inicial do cenario.
     *
     * @return budget inicial em reais
     */
    public double getBudget() {
        return budget;
    }

    /**
     * Retorna o risco inicial do produto.
     *
     * @return risco inicial entre zero e um
     */
    public double getRiscoInicialProduto() {
        return riscoInicialProduto;
    }

    /**
     * Retorna a chance de falha do homogeneizador.
     *
     * @return probabilidade entre zero e um
     */
    public double getProbabilidadeFalhaHomogeneizador() {
        return probabilidadeFalhaHomogeneizador;
    }

    /**
     * Retorna a chance de falha da empacotadora.
     *
     * @return probabilidade entre zero e um
     */
    public double getProbabilidadeFalhaEmpacotador() {
        return probabilidadeFalhaEmpacotador;
    }

    /**
     * Retorna a chance de falha da inspecao.
     *
     * @return probabilidade entre zero e um
     */
    public double getProbabilidadeFalhaInspecao() {
        return probabilidadeFalhaInspecao;
    }

    /**
     * Retorna o desgaste maximo do homogeneizador por uso.
     *
     * @return desgaste maximo em pontos de saude
     */
    public double getMaximoDesgasteHomogeneizador() {
        return maximoDesgasteHomogeneizador;
    }

    /**
     * Retorna o desgaste maximo da empacotadora por uso.
     *
     * @return desgaste maximo em pontos de saude
     */
    public double getMaximoDesgasteEmpacotador() {
        return maximoDesgasteEmpacotador;
    }

    /**
     * Retorna o desgaste maximo da inspecao por uso.
     *
     * @return desgaste maximo em pontos de saude
     */
    public double getMaximoDesgasteInspecao() {
        return maximoDesgasteInspecao;
    }

    /**
     * Mostra o cenario escolhido.
     */
    public void exibirCenarioAtual() {
        System.out.println("Cenário atual: " + nome);
    }
}
