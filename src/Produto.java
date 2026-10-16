public abstract class Produto implements Auditavel {
    private String id;
    private String nome;
    private String status;
    private double quantidadeMateriaPrimaPorUnidade;
    private double qualidade;
    private double probabilidadeFalhaAcumulada;
    private int lote;
    private static int totalProdutosFabricados = 0;

    public Produto(
            String id,
            String nome,
            double quantidadeMateriaPrimaPorUnidade,
            double qualidade
    ) {
        this.id = id;
        this.nome = nome;
        // Cada produto novo começa um ciclo próprio, sem reaproveitar o status de outro lote.
        this.status = "Aguardando";
        this.quantidadeMateriaPrimaPorUnidade = quantidadeMateriaPrimaPorUnidade;
        this.qualidade = qualidade;
        this.probabilidadeFalhaAcumulada = 0.0;
        this.lote = 0;
    }

    public abstract void processar();
    public abstract double calcularTempoProducao();
    public abstract TipoProduto getTipo();

    @Override
    public String gerarRelatorioDiagnostico() {
        return String.format(
                "%s - %s | Lote: %d | Qualidade: %.2f | Risco acumulado: %.2f | Atenção necessária: %s",
                id,
                nome,
                lote,
                qualidade,
                probabilidadeFalhaAcumulada,
                precisaManutencao() ? "Sim" : "Não"
        );
    }

    @Override
    public boolean precisaManutencao() {
        return probabilidadeFalhaAcumulada >= 0.70 || "Rejeitado".equals(status);
    }

    public String getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getQuantidadeMateriaPrimaPorUnidade() {
        return quantidadeMateriaPrimaPorUnidade;
    }

    public double getQualidade() {
        return qualidade;
    }

    public double getProbabilidadeFalhaAcumulada() {
        return probabilidadeFalhaAcumulada;
    }

    public int getLote() {
        return lote;
    }

    public void setLote(int lote) {
        this.lote = lote;
    }

    // O risco é acumulado durante a linha, mas nunca passa de 100%.
    public void aumentarProbabilidadeFalha(double aumento) {
        probabilidadeFalhaAcumulada += aumento;
        if (probabilidadeFalhaAcumulada > 1.0) {
            probabilidadeFalhaAcumulada = 1.0;
        }
    }

    protected static void incrementarTotalProdutosFabricados() {
        totalProdutosFabricados++;
    }

    public static int getTotalProdutosFabricados() {
        return totalProdutosFabricados;
    }
}
