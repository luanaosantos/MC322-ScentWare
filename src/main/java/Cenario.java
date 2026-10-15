/**
 * Reúne os parâmetros que mudam o funcionamento da mesma fábrica.
 * As chances ficam entre zero e um; o desgaste é medido em pontos de saúde.
 *
 * @author Luana Oliveira
 * @author Roberta Santos
 */
public enum Cenario {
    /** Operação com budget maior e riscos e desgastes menores. */
    IDEAL("Ideal", 1000.0, 0.02, 0.05, 0.03, 0.05, 3.0, 3.0, 3.0),
    /** Operação com budget menor e riscos e desgastes maiores. */
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
     * Guarda a configuração que o Main usa pra montar a fábrica.
     *
     * @param nome nome exibido no menu
     * @param budget orçamento inicial em reais
     * @param riscoInicialProduto risco aplicado a cada produto novo
     * @param probabilidadeFalhaHomogeneizador chance básica de falha do homogeneizador
     * @param probabilidadeFalhaEmpacotador chance básica de falha da empacotadora
     * @param probabilidadeFalhaInspecao chance básica de falha da inspeção
     * @param maximoDesgasteHomogeneizador desgaste máximo por uso do homogeneizador
     * @param maximoDesgasteEmpacotador desgaste máximo por uso da empacotadora
     * @param maximoDesgasteInspecao desgaste máximo por uso da inspeção
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
     * Consulta quanto a fábrica tem pra começar a operação.
     *
     * @return budget inicial em reais
     */
    public double getBudget() {
        return budget;
    }

    /**
     * Consulta o risco inicial de cada produto novo.
     *
     * @return risco inicial entre zero e um
     */
    public double getRiscoInicialProduto() {
        return riscoInicialProduto;
    }

    /**
     * Consulta a chance básica de falha do homogeneizador.
     *
     * @return probabilidade entre zero e um
     */
    public double getProbabilidadeFalhaHomogeneizador() {
        return probabilidadeFalhaHomogeneizador;
    }

    /**
     * Consulta a chance básica de falha da empacotadora.
     *
     * @return probabilidade entre zero e um
     */
    public double getProbabilidadeFalhaEmpacotador() {
        return probabilidadeFalhaEmpacotador;
    }

    /**
     * Consulta a chance básica de falha da inspeção.
     *
     * @return probabilidade entre zero e um
     */
    public double getProbabilidadeFalhaInspecao() {
        return probabilidadeFalhaInspecao;
    }

    /**
     * Consulta o limite do desgaste sorteado a cada uso do homogeneizador.
     *
     * @return desgaste máximo em pontos de saúde
     */
    public double getMaximoDesgasteHomogeneizador() {
        return maximoDesgasteHomogeneizador;
    }

    /**
     * Consulta o limite do desgaste sorteado a cada uso da empacotadora.
     *
     * @return desgaste máximo em pontos de saúde
     */
    public double getMaximoDesgasteEmpacotador() {
        return maximoDesgasteEmpacotador;
    }

    /**
     * Consulta o limite do desgaste sorteado a cada uso da inspeção.
     *
     * @return desgaste máximo em pontos de saúde
     */
    public double getMaximoDesgasteInspecao() {
        return maximoDesgasteInspecao;
    }

    /**
     * Mostra o nome do cenário no terminal pra acompanhar o menu.
     */
    public void exibirCenarioAtual() {
        System.out.println("Cenário atual: " + nome);
    }
}
