public class MaquinaEmpacotadora extends Maquina {
    private Motor motor;
    private Sensor sensor;
    private BicoEnvasador bicoEnvasador;
    public MaquinaEmpacotadora(
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
        this.bicoEnvasador = new BicoEnvasador();
    }

    @Override
    public boolean processar(Produto produto) {
        ligar();

        // Assim como no homogeneizador, uma falha aqui aumenta o risco que será avaliado na inspeção.
        if (verificarFalha()) {
            produto.aumentarProbabilidadeFalha(getProbabilidadeFalha());
        }
        desgasteAleatorio();
        desligar();

        return true;
    }

    @Override
    public String getTipo() {
        return "Empacotadora";
    }
}
