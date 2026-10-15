/**
 * Controla o estoque e o custo unitário da matéria-prima da fábrica.
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
     * Cadastra uma matéria-prima com estoque inicial, inclusive zero.
     *
     * @param id identificação do material
     * @param nome nome pra consulta
     * @param quantidade qtd inicial no estoque
     * @param unidade unidade usada nas compras e no consumo
     * @param custoPorUnidade custo de cada unidade
     * @throws IllegalArgumentException se faltar identificação ou os valores forem inválidos
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
     * Retira material só depois de conferir toda a qtd pedida.
     *
     * @param quantidade qtd positiva pra consumir
     * @return true após a retirada
     * @throws IllegalArgumentException se a qtd não for finita e positiva
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
     * Soma uma entrada ao estoque sem deixar o total ultrapassar o limite.
     *
     * @param quantidade qtd finita e positiva da entrada
     * @throws IllegalArgumentException se a entrada ou o total forem inválidos
     */
    public void adicionarEstoque(double quantidade) {
        double novoEstoque = this.quantidade + quantidade;
        if (!Double.isFinite(quantidade) || quantidade <= 0 || !Double.isFinite(novoEstoque)) {
            throw new IllegalArgumentException("A entrada precisa ser positiva e manter o estoque finito.");
        }
        this.quantidade = novoEstoque;
    }

    /**
     * Consulta se o estoque cobre uma retirada sem alterar nada.
     *
     * @param quantidade qtd finita e positiva procurada
     * @return true se houver material suficiente
     * @throws IllegalArgumentException se a qtd for inválida
     */
    public boolean verificarDisponibilidade(double quantidade) {
        if (!Double.isFinite(quantidade) || quantidade <= 0) {
            throw new IllegalArgumentException("A quantidade precisa ser finita e positiva.");
        }
        return this.quantidade >= quantidade;
    }

    /**
     * Consulta a identificação do material.
     * @return id cadastrado
     */
    public String getId() {
        return id;
    }

    /**
     * Consulta o nome do material.
     * @return nome cadastrado
     */
    public String getNome() {
        return nome;
    }

    /**
     * Consulta o saldo disponível.
     * @return qtd em estoque
     */
    public double getQuantidade() {
        return quantidade;
    }

    /**
     * Consulta a unidade usada no estoque.
     * @return unidade cadastrada
     */
    public String getUnidade() {
        return unidade;
    }

    /**
     * Consulta o preço de compra de cada unidade.
     * @return custo por unidade
     */
    public double getCustoPorUnidade() {
        return custoPorUnidade;
    }
}
