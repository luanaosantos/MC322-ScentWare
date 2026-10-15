import java.util.Random;

/**
 * Guarda custo, desgaste e falhas de uma etapa da linha de produção.
 * A simulação usa sorteio e o gerenciador cuida do reparo automático.
 *
 * @author Luana Oliveira
 * @author Roberta Santos
 */
public abstract class Maquina implements Auditavel {
    private String nome;
    private boolean ligada;
    private double capacidadeMaxima;
    private double probabilidadeFalha;
    private double custoOperacao;
    private double health;
    private double maximoDesgaste;
    private int falhas;
    private Random random = new Random();

    /**
     * Cria uma máquina desligada com os parâmetros do cenário.
     *
     * @param nome nome da máquina
     * @param capacidadeMaxima capacidade finita e positiva
     * @param probabilidadeFalha chance básica de falha entre zero e um
     * @param custoOperacao custo não negativo de cada uso
     * @param health saúde inicial entre zero e cem
     * @param maximoDesgaste desgaste máximo não negativo por uso
     * @throws IllegalArgumentException se o nome ou algum parâmetro forem inválidos
     */
    public Maquina(
            String nome,
            double capacidadeMaxima,
            double probabilidadeFalha,
            double custoOperacao,
            double health,
            double maximoDesgaste
    ) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Informe o nome da máquina.");
        }
        if (!Double.isFinite(capacidadeMaxima) || capacidadeMaxima <= 0
                || !Double.isFinite(probabilidadeFalha) || probabilidadeFalha < 0 || probabilidadeFalha > 1
                || !Double.isFinite(custoOperacao) || custoOperacao < 0
                || !Double.isFinite(health) || health < 0 || health > 100
                || !Double.isFinite(maximoDesgaste) || maximoDesgaste < 0) {
            throw new IllegalArgumentException("Confira capacidade positiva, chance entre 0 e 1, saúde entre 0 e 100 e custo/desgaste não negativos.");
        }
        this.nome = nome;
        this.ligada = false;
        this.capacidadeMaxima = capacidadeMaxima;
        this.probabilidadeFalha = probabilidadeFalha;
        this.custoOperacao = custoOperacao;
        this.health = health;
        this.maximoDesgaste = maximoDesgaste;
        this.falhas = 0;
    }

    /**
     * Executa a etapa e desliga a máquina ao terminar, inclusive após erro.
     * @param produto unidade não nula da produção
     * @return resultado da etapa; false na inspeção significa rejeição normal
     * @throws IllegalArgumentException se não houver produto
     */
    public abstract boolean processar(Produto produto);

    /**
     * Identifica a etapa da máquina na linha.
     * @return tipo da máquina
     */
    public abstract String getTipo();

    /**
     * Mostra saúde e falhas pra auditoria da fábrica.
     * @return diagnóstico formatado
     */
    @Override
    public String gerarRelatorioDiagnostico() {
        return String.format(
                "%s (%s) | Saúde: %.1f%% | Falhas: %d | Precisa de manutenção: %s",
                nome,
                getTipo(),
                health,
                falhas,
                precisaManutencao() ? "Sim" : "Não"
        );
    }

    /**
     * Indica manutenção preventiva com saúde abaixo de 30%.
     * @return true quando a máquina precisa de atenção
     */
    @Override
    public boolean precisaManutencao() {
        return health < 30.0;
    }

    /**
     * Confere se a saúde chegou a zero.
     * @return true quando há necessidade de reparo
     */
    public boolean quebrada() {
        return health <= 0.0;
    }

    /** Soma uma falha detectada durante o sorteio da etapa. */
    public void contabilizarFalha() {
        falhas++;
    }

    /** Restaura a saúde pra 100%, preservando o histórico de falhas. */
    public void reparar() {
        health = 100.0;
    }

    /** Liga a máquina pra executar uma etapa. */
    public void ligar() {
        ligada = true;
    }

    /** Desliga a máquina ao sair da etapa. */
    public void desligar() {
        ligada = false;
    }

    /**
     * Consulta se a máquina está ligada.
     * @return true enquanto estiver ligada
     */
    public boolean estaLigada() {
        return ligada;
    }

    /**
     * Consulta o nome cadastrado.
     * @return nome da máquina
     */
    public String getNome() {
        return nome;
    }

    /**
     * Consulta a capacidade da etapa.
     * @return capacidade máxima cadastrada
     */
    public double getCapacidadeMaxima() {
        return capacidadeMaxima;
    }

    /**
     * Consulta o custo de um uso da máquina.
     * @return custo de operação
     */
    public double getCustoOperacao() {
        return custoOperacao;
    }

    /**
     * Consulta o desgaste restante como porcentagem de saúde.
     * @return saúde entre zero e cem
     */
    public double getHealth() {
        return health;
    }

    /**
     * Consulta a chance básica configurada no cenário.
     * @return probabilidade entre zero e um
     */
    public double getProbabilidadeFalha() {
        return probabilidadeFalha;
    }

    /**
     * Sorteia uma falha usando a chance do cenário acrescida do desgaste.
     * A chance fica limitada a 100%, e cada falha entra no histórico.
     * @return true quando o sorteio indicar falha
     */
    protected boolean verificarFalha() {
        double fatorDesgaste = 1.0 + (100.0 - health) / 100.0;
        double chanceFalha = Math.min(1.0, probabilidadeFalha * fatorDesgaste);
        boolean falhou = random.nextDouble() < chanceFalha;

        if (falhou) {
            contabilizarFalha();
        }
        return falhou;
    }

    /**
     * Aplica desgaste aleatório por uso, sem deixar a saúde abaixo de zero.
     * No cenário Apocalíptico o limite de desgaste é mais agressivo.
     */
    protected void desgasteAleatorio() {
        health -= random.nextDouble() * maximoDesgaste;
        if (health < 0.0) {
            health = 0.0;
        }
    }
}
