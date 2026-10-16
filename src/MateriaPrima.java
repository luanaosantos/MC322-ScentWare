public class MateriaPrima {
    private String id;
    private String nome;
    private double quantidade;
    private String unidade;
    private double custoPorUnidade;

    public MateriaPrima(
            String id,
            String nome,
            double quantidade,
            String unidade,
            double custoPorUnidade
    ) {
        this.id = id;
        this.nome = nome;
        this.quantidade = quantidade;
        this.unidade = unidade;
        this.custoPorUnidade = custoPorUnidade;
    }
    
    public boolean consumir(double quantidade) {
        if (verificarDisponibilidade(quantidade)) {
            this.quantidade -= quantidade;
            return true;
        }
        return false;
    }

    public void adicionarEstoque(double quantidade) {
        if (quantidade > 0) {
            this.quantidade += quantidade;
        }
    }

    public boolean verificarDisponibilidade(double quantidade) {
        return quantidade > 0 && this.quantidade >= quantidade;
    }

    public String getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public double getQuantidade() {
        return quantidade;
    }

    public String getUnidade() {
        return unidade;
    }

    public double getCustoPorUnidade() {
        return custoPorUnidade;
    }
}
