public class MaquinaHomogeneizador extends Maquina {
    private Motor motor;
    private Sensor sensor;
    private ValvulaHomogeneizadora valvulaHomogeneizadora;

    public MaquinaHomogeneizador(
            String nome,
            double capacidadeMaxima,
            double probabilidadeFalha,
            double custoOperacao,
            double health,
            double maximoDesgaste
    ) {
        super(nome, capacidadeMaxima, probabilidadeFalha, custoOperacao, health, maximoDesgaste);
        this.motor = new Motor();
        this.sensor = new Sensor();
    }

    @Override
    public boolean processar(Produto produto) {
        ligar();
        produto.processar();

        // Uma falha aqui não rejeita o produto na hora, mas aumenta o risco levado até a inspeção.
        if (verificarFalha()) {
            produto.aumentarProbabilidadeFalha(getProbabilidadeFalha());
        }

        desgasteAleatorio();
        desligar();
        return true;
    }

    @Override
    public String getTipo() {
        return "Homogeneizador";
    }
}
