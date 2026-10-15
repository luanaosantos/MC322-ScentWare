import java.util.Random;

/**
 * Decide a aprovação por sorteio, conforme qualidade e riscos acumulados.
 *
 * @author Luana Oliveira
 * @author Roberta Santos
 */
public class MaquinaInspecao extends Maquina {
    private Random random = new Random();

    /**
     * Configura a etapa com os valores do cenário.
     * @param nome nome da máquina
     * @param capacidadeMaxima capacidade finita e positiva
     * @param probabilidadeFalha chance básica entre zero e um
     * @param custoOperacao custo finito e não negativo de cada uso
     * @param health saúde entre zero e cem
     * @param maximoDesgaste desgaste máximo finito e não negativo
     * @throws IllegalArgumentException se os dados forem inválidos, conforme a classe Maquina
     */
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

    /**
     * Inspeciona a unidade; rejeição faz parte da simulação e não é exceção.
     * @param produto unidade não nula a processar
     * @return true se a unidade foi aprovada, ou false se foi rejeitada
     * @throws IllegalArgumentException se o produto estiver ausente
     */
    @Override
    public boolean processar(Produto produto) {
        if (produto == null) {
            throw new IllegalArgumentException("Informe um produto pra processar.");
        }
        ligar();
        try {
            // Quanto maior a qualidade, menor a chance básica de rejeição.
            // Os problemas acumulados nas etapas anteriores aumentam essa chance.
            double chanceRejeicao = (1.0 - produto.getQualidade())
                    + produto.getProbabilidadeFalhaAcumulada() * 0.5;
            chanceRejeicao = Math.min(1.0, chanceRejeicao);

            boolean rejeitado = random.nextDouble() < chanceRejeicao;

            // Se a própria inspeção falhar, ela pode dar o resultado contrário.
            if (verificarFalha()) {
                rejeitado = !rejeitado;
            }
            if (rejeitado) {
                produto.setStatus("Rejeitado");
            } else {
                produto.setStatus("Aprovado");
            }

            desgasteAleatorio();
            return !rejeitado;
        } finally {
            // Mesmo se a etapa der erro, não deixamos a máquina ligada.
            desligar();
        }
    }

    /**
     * Identifica esta etapa da linha.
     * @return tipo Inspecao
     */
    @Override
    public String getTipo() {
        return "Inspecao";
    }
}
