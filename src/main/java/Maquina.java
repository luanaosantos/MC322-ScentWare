// Base das maquinas da linha de producao.

import java.util.ArrayList;
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
    private final Motor motor;
    private final ArrayList<Sensor> sensores;

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
        this(nome, capacidadeMaxima, probabilidadeFalha, custoOperacao, health, maximoDesgaste, 2);
    }

    /**
     * Cria a maquina com a qtd de sensores escolhida.
     *
     * @param nome nome da maquina
     * @param capacidadeMaxima capacidade positiva
     * @param probabilidadeFalha chance entre 0 e 1
     * @param custoOperacao custo de cada uso
     * @param health saude inicial entre 0 e 100
     * @param maximoDesgaste desgaste maximo por uso
     * @param quantidadeSensores qtd de sensores, pelo menos um
     * @throws IllegalArgumentException se algum valor for invalido
     */
    protected Maquina(
            String nome,
            double capacidadeMaxima,
            double probabilidadeFalha,
            double custoOperacao,
            double health,
            double maximoDesgaste,
            int quantidadeSensores
    ) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Informe o nome da máquina.");
        }
        if (!Double.isFinite(capacidadeMaxima) || capacidadeMaxima <= 0
                || !Double.isFinite(probabilidadeFalha) || probabilidadeFalha < 0 || probabilidadeFalha > 1
                || !Double.isFinite(custoOperacao) || custoOperacao < 0
                || !Double.isFinite(health) || health < 0 || health > 100
                || !Double.isFinite(maximoDesgaste) || maximoDesgaste < 0 || quantidadeSensores < 1) {
            throw new IllegalArgumentException("Confira capacidade positiva, chance entre 0 e 1, saúde entre 0 e 100, custo/desgaste não negativos e pelo menos um sensor.");
        }
        this.nome = nome;
        this.ligada = false;
        this.capacidadeMaxima = capacidadeMaxima;
        this.probabilidadeFalha = probabilidadeFalha;
        this.custoOperacao = custoOperacao;
        this.health = health;
        this.maximoDesgaste = maximoDesgaste;
        this.falhas = 0;
        // Cada maquina cria suas partes pra nao compartilhar estado com outra.
        this.motor = new Motor(health);
        this.sensores = new ArrayList<>();
        for (int i = 0; i < quantidadeSensores; i++) {
            sensores.add(new Sensor());
        }
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
     * Mostra a saude, a chance de falha e o estado das partes.
     *
     * @return texto do relatorio
     */
    @Override
    public String gerarRelatorioDiagnostico() {
        StringBuilder relatorio = new StringBuilder(String.format(
                "%s (%s) | Saúde: %.1f%% | Falhas: %d | Falha base: %.0f%% | Precisa de manutenção: %s",
                nome,
                getTipo(),
                health,
                falhas,
                probabilidadeFalha * 100,
                precisaManutencao() ? "Sim" : "Não"
        ));
        relatorio.append("\n  - ").append(motor.diagnostico());
        for (int i = 0; i < sensores.size(); i++) {
            relatorio.append("\n  - ").append(i + 1).append(". ").append(sensores.get(i).diagnostico());
        }
        relatorio.append("\n  - ").append(diagnosticoParte());
        return relatorio.toString();
    }

    /**
     * Confere a saude da maquina e se alguma parte precisa de reparo.
     *
     * @return true quando a maquina precisa de atencao
     */
    @Override
    public boolean precisaManutencao() {
        if (health < 30.0 || motor.quebrado() || partePrecisaReparo()) {
            return true;
        }
        for (Sensor sensor : sensores) {
            if (sensor.precisaReparo()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Confere se a saude chegou a zero.
     *
     * @return true quando ha necessidade de reparo
     */
    public boolean quebrada() {
        return health <= 0.0 || motor.quebrado();
    }

    /** Soma uma falha no contador. */
    public void contabilizarFalha() {
        falhas++;
    }

    /** Repara a maquina e suas partes sem apagar o total de falhas. */
    public void reparar() {
        health = 100.0;
        motor.reparar();
        for (Sensor sensor : sensores) {
            sensor.reparar();
        }
        repararParte();
    }

    /**
     * Liga a maquina pelo motor.
     *
     * @throws PlantaException se o motor precisar de reparo
     */
    public void ligar() {
        motor.ligar();
        ligada = true;
    }

    /** Desliga a maquina. */
    public void desligar() {
        ligada = false;
        motor.desligar();
        for (Sensor sensor : sensores) {
            sensor.limparPresenca();
        }
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
     * Sorteia a falha considerando o desgaste. Se falhar, conta e avisa um sensor.
     *
     * @return true quando o sorteio indicar falha
     */
    protected boolean verificarFalha() {
        double fatorDesgaste = 1.0 + (100.0 - health) / 100.0;
        double chanceFalha = Math.min(1.0, probabilidadeFalha * fatorDesgaste);
        boolean falhou = random.nextDouble() < chanceFalha;

        if (falhou) {
            contabilizarFalha();
            sensores.get(random.nextInt(sensores.size())).registrarFalha();
        }
        return falhou;
    }

    /**
     * Sorteia o desgaste da maquina e do motor, sem deixar a saude abaixo de zero.
     */
    protected void desgasteAleatorio() {
        double desgaste = random.nextDouble() * maximoDesgaste;
        health -= desgaste;
        motor.desgastar(desgaste);
        if (health < 0.0) {
            health = 0.0;
        }
    }

    /**
     * Pede a leitura a todos os sensores da maquina.
     *
     * @return true se todos detectaram o produto
     */
    protected boolean detectarProduto() {
        boolean detectado = true;
        for (Sensor sensor : sensores) {
            if (!sensor.detectarPresenca()) {
                detectado = false;
            }
        }
        return detectado;
    }

    /**
     * Pede o diagnostico da parte especifica da etapa.
     *
     * @return texto da parte
     */
    protected abstract String diagnosticoParte();

    /**
     * Confere a condicao da parte especifica.
     *
     * @return true se precisa de reparo
     */
    protected abstract boolean partePrecisaReparo();

    /** Repara a parte especifica da etapa. */
    protected abstract void repararParte();
}
