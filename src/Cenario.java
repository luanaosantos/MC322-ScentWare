public enum Cenario {
    // Cada cenário reúne os parâmetros que mudam o comportamento da mesma fábrica.
    IDEAL("Ideal", 1000.0, 0.02, 0.05, 0.03, 0.05, 3.0, 3.0, 3.0),
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

    public double getBudget() {
        return budget;
    }

    public double getRiscoInicialProduto() {
        return riscoInicialProduto;
    }

    public double getProbabilidadeFalhaHomogeneizador() {
        return probabilidadeFalhaHomogeneizador;
    }

    public double getProbabilidadeFalhaEmpacotador() {
        return probabilidadeFalhaEmpacotador;
    }

    public double getProbabilidadeFalhaInspecao() {
        return probabilidadeFalhaInspecao;
    }

    public double getMaximoDesgasteHomogeneizador() {
        return maximoDesgasteHomogeneizador;
    }

    public double getMaximoDesgasteEmpacotador() {
        return maximoDesgasteEmpacotador;
    }

    public double getMaximoDesgasteInspecao() {
        return maximoDesgasteInspecao;
    }

    public void exibirCenarioAtual() {
        System.out.println("Cenário atual: " + nome);
    }
}
