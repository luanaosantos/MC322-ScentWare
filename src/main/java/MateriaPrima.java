// Estoque e custo da materia-prima.

/**
 * Guarda a qtd em estoque e o custo por unidade.
 *
 * @author Luana Oliveira
 * @author Roberta Santos
 */
public class MateriaPrima {
    private String id;
    private String nome;
    private double quantidade;
    private String unidade;
    private double custoPorUnidade;

    /**
     * Cadastra o material com estoque inicial, q pode ser zero.
     *
     * @param id id do material
     * @param nome nome pra consulta
     * @param quantidade qtd inicial no estoque
     * @param unidade unidade usada nas compras e no consumo
     * @param custoPorUnidade custo de cada unidade
     * @throws IllegalArgumentException se faltar id ou os valores forem invalidos
     */
    public MateriaPrima(
            String id,
            String nome,
            double quantidade,
            String unidade,
            double custoPorUnidade
    ) {
        if (id == null || id.isBlank() || nome == null || nome.isBlank()
                || unidade == null || unidade.isBlank()) {
            throw new IllegalArgumentException("Informe id, nome e unidade da matéria-prima.");
        }
        if (!Double.isFinite(quantidade) || quantidade < 0
                || !Double.isFinite(custoPorUnidade) || custoPorUnidade < 0) {
            throw new IllegalArgumentException("Estoque e custo precisam ser finitos e não negativos.");
        }
        this.id = id;
        this.nome = nome;
        this.quantidade = quantidade;
        this.unidade = unidade;
        this.custoPorUnidade = custoPorUnidade;
    }

    /**
     * Retira a qtd pedida se houver estoque.
     *
     * @param quantidade qtd positiva pra consumir
     * @return true apos a retirada
     * @throws IllegalArgumentException se a qtd nao for finita e positiva
     * @throws RecursoInsuficienteException se faltar material no estoque
     */
    public boolean consumir(double quantidade) {
        if (!verificarDisponibilidade(quantidade)) {
            throw new RecursoInsuficienteException("Estoque insuficiente de " + nome + ".");
        }
        this.quantidade -= quantidade;
        return true;
    }

    /**
     * Soma a entrada depois de conferir a qtd e o total.
     *
     * @param quantidade qtd finita e positiva da entrada
     * @throws IllegalArgumentException se a entrada ou o total forem invalidos
     */
    public void adicionarEstoque(double quantidade) {
        double novoEstoque = this.quantidade + quantidade;
        if (!Double.isFinite(quantidade) || quantidade <= 0 || !Double.isFinite(novoEstoque)) {
            throw new IllegalArgumentException("A entrada precisa ser positiva e manter o estoque finito.");
        }
        this.quantidade = novoEstoque;
    }

    /**
     * Confere se o estoque tem a qtd pedida.
     *
     * @param quantidade qtd finita e positiva procurada
     * @return true se houver material suficiente
     * @throws IllegalArgumentException se a qtd for invalida
     */
    public boolean verificarDisponibilidade(double quantidade) {
        if (!Double.isFinite(quantidade) || quantidade <= 0) {
            throw new IllegalArgumentException("A quantidade precisa ser finita e positiva.");
        }
        return this.quantidade >= quantidade;
    }

    /**
     * Retorna o id.
     *
     * @return id guardado
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
     * Retorna a qtd em estoque.
     *
     * @return qtd em estoque
     */
    public double getQuantidade() {
        return quantidade;
    }

    /**
     * Retorna a unidade usada no estoque.
     *
     * @return unidade guardada
     */
    public String getUnidade() {
        return unidade;
    }

    /**
     * Retorna o custo por unidade.
     *
     * @return custo por unidade
     */
    public double getCustoPorUnidade() {
        return custoPorUnidade;
    }
}
