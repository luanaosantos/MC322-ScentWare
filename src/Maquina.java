import java.util.Random;

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

    public Maquina(
            String nome,
            double capacidadeMaxima,
            double probabilidadeFalha,
            double custoOperacao,
            double health,
            double maximoDesgaste
    ) {
        this.nome = nome;
        this.ligada = false;
        this.capacidadeMaxima = capacidadeMaxima;
        this.probabilidadeFalha = probabilidadeFalha;
        this.custoOperacao = custoOperacao;
        this.health = health;
        this.maximoDesgaste = maximoDesgaste;
        this.falhas = 0;
    }

    public abstract boolean processar(Produto produto);
    public abstract String getTipo();

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

    @Override
    public boolean precisaManutencao() {
        return health < 30.0;
    }

    public boolean quebrada() {
        return health <= 0.0;
    }

    public void contabilizarFalha() {
        falhas++;
    }

    public void reparar() {
        health = 100.0;
    }

    public void ligar() {
        ligada = true;
    }

    public void desligar() {
        ligada = false;
    }

    public boolean estaLigada() {
        return ligada;
    }

    public String getNome() {
        return nome;
    }

    public double getCapacidadeMaxima() {
        return capacidadeMaxima;
    }

    public double getCustoOperacao() {
        return custoOperacao;
    }

    public double getHealth() {
        return health;
    }

    public double getProbabilidadeFalha() {
        return probabilidadeFalha;
    }

    // A chance começa no valor do cenário e cresce conforme a máquina vai se desgastando.
    protected boolean verificarFalha() {
        double fatorDesgaste = 1.0 + (100.0 - health) / 100.0;
        double chanceFalha = Math.min(1.0, probabilidadeFalha * fatorDesgaste);
        boolean falhou = random.nextDouble() < chanceFalha;

        if (falhou) {
            contabilizarFalha();
        }
        return falhou;
    }

    // O desgaste acontece a cada uso e é mais agressivo no cenário Apocalíptico.
    protected void desgasteAleatorio() {
        health -= random.nextDouble() * maximoDesgaste;
        if (health < 0.0) {
            health = 0.0;
        }
    }
}
