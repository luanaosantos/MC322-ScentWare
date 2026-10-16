// Inspecao final dos produtos.

import java.util.Random;

/**
 * Sorteia se o produto passa ou nao pela inspecao.
 *
 * @author Luana Oliveira
 * @author Roberta Santos
 */
public class MaquinaInspecao extends Maquina {
    private Random random = new Random();

    /**
     * Cria a inspecao com os valores do cenario.
     *
     * @param nome nome da maquina
     * @param capacidadeMaxima capacidade finita e positiva
     * @param probabilidadeFalha chance basica entre zero e um
     * @param custoOperacao custo finito e nao negativo de cada uso
     * @param health saude entre zero e cem
     * @param maximoDesgaste desgaste maximo finito e nao negativo
     * @throws IllegalArgumentException se os dados forem invalidos, conforme a classe Maquina
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
     * Sorteia a aprovacao do produto. Rejeicao faz parte da simulacao.
     *
     * @param produto unidade nao nula a processar
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
            // Qualidade maior reduz a chance de rejeicao.
            // O risco das outras etapas aumenta essa chance.
            double chanceRejeicao = (1.0 - produto.getQualidade())
                    + produto.getProbabilidadeFalhaAcumulada() * 0.5;
            chanceRejeicao = Math.min(1.0, chanceRejeicao);

            boolean rejeitado = random.nextDouble() < chanceRejeicao;

            // Se a inspecao falhar, inverte o resultado.
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
            // Desliga mesmo se der erro.
            desligar();
        }
    }

    /**
     * Retorna o tipo da maquina.
     *
     * @return tipo Inspecao
     */
    @Override
    public String getTipo() {
        return "Inspecao";
    }
}
