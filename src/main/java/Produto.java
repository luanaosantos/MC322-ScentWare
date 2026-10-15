/**
 * Reúne os dados e o risco acumulado de uma unidade de cosmético.
 * As subclasses definem o tipo e o tempo de produção de cada receita.
 *
 * @author Luana Oliveira
 * @author Roberta Santos
 */
public abstract class Produto implements Auditavel {
    private String id;
    private String nome;
    private String status;
    private double quantidadeMateriaPrimaPorUnidade;
    private double qualidade;
    private double probabilidadeFalhaAcumulada;
    private int lote;
    private static int totalProdutosFabricados = 0;

    /**
     * Cria uma unidade aguardando processamento, sem risco acumulado.
     *
     * @param id identificação do produto
     * @param nome nome do cosmético
     * @param quantidadeMateriaPrimaPorUnidade consumo positivo de cada unidade
     * @param qualidade qualidade entre zero e um
     * @throws IllegalArgumentException se faltar identificação ou houver valor fora da faixa
     */
    public Produto(
            String id,
            String nome,
            double quantidadeMateriaPrimaPorUnidade,
            double qualidade
    ) {
        if (id == null || id.isBlank() || nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Informe id e nome do produto.");
        }
        if (!Double.isFinite(quantidadeMateriaPrimaPorUnidade) || quantidadeMateriaPrimaPorUnidade <= 0
                || !Double.isFinite(qualidade) || qualidade < 0 || qualidade > 1) {
            throw new IllegalArgumentException("O consumo precisa ser positivo e a qualidade deve ficar entre 0 e 1.");
        }
        this.id = id;
        this.nome = nome;
        // Cada produto novo começa um ciclo próprio, sem reaproveitar o status de outro lote.
        this.status = "Aguardando";
        this.quantidadeMateriaPrimaPorUnidade = quantidadeMateriaPrimaPorUnidade;
        this.qualidade = qualidade;
        this.probabilidadeFalhaAcumulada = 0.0;
        this.lote = 0;
    }

    /** Marca o início do processamento da unidade. */
    public abstract void processar();

    /**
     * Informa o tempo previsto pra receita.
     * @return tempo de produção da unidade
     */
    public abstract double calcularTempoProducao();

    /**
     * Identifica qual receita esta unidade representa.
     * @return tipo do cosmético
     */
    public abstract TipoProduto getTipo();

    /**
     * Mostra qualidade, lote e risco pra auditoria da unidade.
     * @return diagnóstico formatado
     */
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

    /**
     * Sinaliza risco alto ou rejeição na inspeção.
     * @return true quando a unidade precisa de atenção
     */
    @Override
    public boolean precisaManutencao() {
        return probabilidadeFalhaAcumulada >= 0.70 || "Rejeitado".equals(status);
    }

    /**
     * Consulta a identificação da unidade.
     * @return id do produto
     */
    public String getId() {
        return id;
    }

    /**
     * Consulta o nome do cosmético.
     * @return nome cadastrado
     */
    public String getNome() {
        return nome;
    }

    /**
     * Consulta a situação na linha de produção.
     * @return status atual
     */
    public String getStatus() {
        return status;
    }

    /**
     * Registra a etapa ou o resultado da inspeção.
     * @param status descrição não vazia do estado
     * @throws IllegalArgumentException se o status estiver ausente
     */
    public void setStatus(String status) {
        if (status == null || status.isBlank()) {
            throw new IllegalArgumentException("Informe o status do produto.");
        }
        this.status = status;
    }

    /**
     * Consulta o consumo da receita por unidade.
     * @return qtd de matéria-prima por produto
     */
    public double getQuantidadeMateriaPrimaPorUnidade() {
        return quantidadeMateriaPrimaPorUnidade;
    }

    /**
     * Consulta a qualidade usada na inspeção.
     * @return qualidade entre zero e um
     */
    public double getQualidade() {
        return qualidade;
    }

    /**
     * Consulta o risco trazido das etapas anteriores.
     * @return probabilidade acumulada entre zero e um
     */
    public double getProbabilidadeFalhaAcumulada() {
        return probabilidadeFalhaAcumulada;
    }

    /**
     * Consulta o lote da unidade.
     * @return número do lote, ou zero antes de atribuí-lo
     */
    public int getLote() {
        return lote;
    }

    /**
     * Associa a unidade a um lote da fábrica.
     * @param lote número não negativo do lote
     * @throws IllegalArgumentException se o lote for negativo
     */
    public void setLote(int lote) {
        if (lote < 0) {
            throw new IllegalArgumentException("O lote não pode ser negativo.");
        }
        this.lote = lote;
    }

    /**
     * Acumula o risco durante a linha, limitado a 100%.
     * @param aumento parcela finita e não negativa de risco
     * @throws IllegalArgumentException se o aumento for inválido
     */
    public void aumentarProbabilidadeFalha(double aumento) {
        if (!Double.isFinite(aumento) || aumento < 0) {
            throw new IllegalArgumentException("O aumento do risco precisa ser finito e não negativo.");
        }
        probabilidadeFalhaAcumulada = Math.min(1.0, probabilidadeFalhaAcumulada + aumento);
    }

    /** Registra mais uma unidade fabricada no contador geral. */
    protected static void incrementarTotalProdutosFabricados() {
        totalProdutosFabricados++;
    }

    /**
     * Consulta o contador compartilhado pelas receitas.
     * @return total de produtos fabricados
     */
    public static int getTotalProdutosFabricados() {
        return totalProdutosFabricados;
    }
}
