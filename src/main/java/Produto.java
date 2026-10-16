// Dados comuns dos produtos da fabrica.

/**
 * Guarda os dados do produto. Cada subclasse define seu tipo e tempo de producao.
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
     * Cria o produto aguardando processamento e sem risco acumulado.
     *
     * @param id id do produto
     * @param nome nome do produto
     * @param quantidadeMateriaPrimaPorUnidade consumo positivo de cada unidade
     * @param qualidade qualidade entre zero e um
     * @throws IllegalArgumentException se faltar id ou houver valor fora da faixa
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
        // Produto novo comeca aguardando, sem copiar o status de outro lote.
        this.status = "Aguardando";
        this.quantidadeMateriaPrimaPorUnidade = quantidadeMateriaPrimaPorUnidade;
        this.qualidade = qualidade;
        this.probabilidadeFalhaAcumulada = 0.0;
        this.lote = 0;
    }

    /** Define como iniciar o processamento do produto. */
    public abstract void processar();

    /**
     * Retorna o tempo de producao desse produto.
     *
     * @return tempo de producao da unidade
     */
    public abstract double calcularTempoProducao();

    /**
     * Retorna o tipo do produto.
     *
     * @return tipo do produto
     */
    public abstract TipoProduto getTipo();

    /**
     * Monta o relatorio com qualidade, lote e risco do produto.
     *
     * @return texto do relatorio
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
     * Confere se o risco esta alto ou se o produto foi rejeitado.
     *
     * @return true quando a unidade precisa de atencao
     */
    @Override
    public boolean precisaManutencao() {
        return probabilidadeFalhaAcumulada >= 0.70 || "Rejeitado".equals(status);
    }

    /**
     * Retorna o id.
     *
     * @return id do produto
     */
    public String getId() {
        return id;
    }

    /**
     * Retorna o nome.
     *
     * @return nome guardado
     */
    public String getNome() {
        return nome;
    }

    /**
     * Retorna o status atual.
     *
     * @return status atual
     */
    public String getStatus() {
        return status;
    }

    /**
     * Atualiza o status do produto.
     *
     * @param status texto do status, sem ficar vazio
     * @throws IllegalArgumentException se o status estiver ausente
     */
    public void setStatus(String status) {
        if (status == null || status.isBlank()) {
            throw new IllegalArgumentException("Informe o status do produto.");
        }
        this.status = status;
    }

    /**
     * Retorna quanto cada produto usa de materia-prima.
     *
     * @return qtd de materia-prima por produto
     */
    public double getQuantidadeMateriaPrimaPorUnidade() {
        return quantidadeMateriaPrimaPorUnidade;
    }

    /**
     * Retorna a qualidade do produto.
     *
     * @return qualidade entre zero e um
     */
    public double getQualidade() {
        return qualidade;
    }

    /**
     * Retorna o risco acumulado do produto.
     *
     * @return probabilidade acumulada entre zero e um
     */
    public double getProbabilidadeFalhaAcumulada() {
        return probabilidadeFalhaAcumulada;
    }

    /**
     * Retorna o lote do produto.
     *
     * @return numero do lote, ou zero antes de atribui-lo
     */
    public int getLote() {
        return lote;
    }

    /**
     * Guarda o numero do lote.
     *
     * @param lote numero nao negativo do lote
     * @throws IllegalArgumentException se o lote for negativo
     */
    public void setLote(int lote) {
        if (lote < 0) {
            throw new IllegalArgumentException("O lote não pode ser negativo.");
        }
        this.lote = lote;
    }

    /**
     * Soma o risco sem passar de 100%.
     *
     * @param aumento parcela finita e nao negativa de risco
     * @throws IllegalArgumentException se o aumento for invalido
     */
    public void aumentarProbabilidadeFalha(double aumento) {
        if (!Double.isFinite(aumento) || aumento < 0) {
            throw new IllegalArgumentException("O aumento do risco precisa ser finito e não negativo.");
        }
        probabilidadeFalhaAcumulada = Math.min(1.0, probabilidadeFalhaAcumulada + aumento);
    }

    /** Soma um produto no contador geral. */
    protected static void incrementarTotalProdutosFabricados() {
        totalProdutosFabricados++;
    }

    /**
     * Retorna o total de produtos fabricados.
     *
     * @return total de produtos fabricados
     */
    public static int getTotalProdutosFabricados() {
        return totalProdutosFabricados;
    }
}
