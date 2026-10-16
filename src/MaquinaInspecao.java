import java.util.Random;

public class MaquinaInspecao extends Maquina {
    private Random random = new Random();

    public MaquinaInspecao(
            String nome,
            double capacidadeMaxima,
            double probabilidadeFalha,
            double custoOperacao,
            double health,
            double maximoDesgaste
    ) {
        super(nome, capacidadeMaxima, probabilidadeFalha, custoOperacao, health, maximoDesgaste);
    }

    @Override
    public boolean processar(Produto produto) {
        ligar();

        // Quanto maior a qualidade, menor a chance básica de rejeição.
        // Os problemas acumulados nas etapas anteriores aumentam essa chance.
        double chanceRejeicao = (1.0 - produto.getQualidade())
                + produto.getProbabilidadeFalhaAcumulada() * 0.5;
        chanceRejeicao = Math.min(1.0, chanceRejeicao);

        boolean rejeitado = random.nextDouble() < chanceRejeicao;

        // Se a própria inspeção falhar, consideramos que ela pode dar o resultado contrário.
        if (verificarFalha()) {
            rejeitado = !rejeitado;
        }
        if (rejeitado) {
            produto.setStatus("Rejeitado");
        } else {
            produto.setStatus("Aprovado");
        }
        
        desgasteAleatorio();
        desligar();
        return !rejeitado;
    }

    @Override
    public String getTipo() {
        return "Inspecao";
    }
}
