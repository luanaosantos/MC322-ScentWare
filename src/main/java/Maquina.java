// Base das maquinas da linha de producao.

import java.util.Random;

/**
 * Guarda custo, saude e falhas da maquina. O gerenciador faz o reparo automatico.
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
     * Cria a maquina desligada com os valores do cenario.
     *
     * @param nome nome da maquina
     * @param capacidadeMaxima capacidade finita e positiva
     * @param probabilidadeFalha chance basica de falha entre zero e um
     * @param custoOperacao custo nao negativo de cada uso
     * @param health saude inicial entre zero e cem
     * @param maximoDesgaste desgaste maximo nao negativo por uso
     * @throws IllegalArgumentException se o nome ou algum parametro forem invalidos
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
     * Define a etapa da maquina. As subclasses desligam ao terminar, mesmo com erro.
     *
     * @param produto unidade nao nula da producao
     * @return resultado da etapa; false na inspecao significa rejeicao normal
     * @throws IllegalArgumentException se nao houver produto
     */
    public abstract boolean processar(Produto produto);

    /**
     * Retorna o tipo da maquina.
     *
     * @return tipo da maquina
     */
    public abstract String getTipo();

    /**
     * Monta o relatorio de saude e falhas da maquina.
     *
     * @return texto do relatorio
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
     * Confere se a saude esta abaixo de 30%.
     *
     * @return true quando a maquina precisa de atencao
     */
    @Override
    public boolean precisaManutencao() {
        return health < 30.0;
    }

    /**
     * Confere se a saude chegou a zero.
     *
     * @return true quando ha necessidade de reparo
     */
    public boolean quebrada() {
        return health <= 0.0;
    }

    /** Soma uma falha no contador. */
    public void contabilizarFalha() {
        falhas++;
    }

    /** Volta a saude pra 100 sem apagar as falhas. */
    public void reparar() {
        health = 100.0;
    }

    /** Liga a maquina. */
    public void ligar() {
        ligada = true;
    }

    /** Desliga a maquina. */
    public void desligar() {
        ligada = false;
    }

    /**
     * Confere se a maquina esta ligada.
     *
     * @return true enquanto estiver ligada
     */
    public boolean estaLigada() {
        return ligada;
    }

    /**
     * Retorna o nome.
     *
     * @return nome da maquina
     */
    public String getNome() {
        return nome;
    }

    /**
     * Retorna a capacidade da maquina.
     *
     * @return capacidade maxima guardada
     */
    public double getCapacidadeMaxima() {
        return capacidadeMaxima;
    }

    /**
     * Retorna o custo de usar a maquina uma vez.
     *
     * @return custo de operacao
     */
    public double getCustoOperacao() {
        return custoOperacao;
    }

    /**
     * Retorna a saude da maquina.
     *
     * @return saude entre zero e cem
     */
    public double getHealth() {
        return health;
    }

    /**
     * Retorna a chance base de falha.
     *
     * @return probabilidade entre zero e um
     */
    public double getProbabilidadeFalha() {
        return probabilidadeFalha;
    }

    /**
     * Sorteia a falha considerando o desgaste e conta se falhar. A chance nao passa de 100%.
     *
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
     * Sorteia o desgaste por uso, sem deixar a saude abaixo de zero.
     */
    protected void desgasteAleatorio() {
        health -= random.nextDouble() * maximoDesgaste;
        if (health < 0.0) {
            health = 0.0;
        }
    }
}
