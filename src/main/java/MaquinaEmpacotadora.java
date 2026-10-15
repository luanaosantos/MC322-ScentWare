/**
 * Embala a unidade e acumula riscos pra inspeção final.
 *
 * @author Luana Oliveira
 * @author Roberta Santos
 */
public class MaquinaEmpacotadora extends Maquina {
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
    public MaquinaEmpacotadora(
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
     * Executa a etapa e leva eventuais riscos até a inspeção.
     * @param produto unidade não nula a processar
     * @return true depois da etapa
     * @throws IllegalArgumentException se o produto estiver ausente
     */
    @Override
    public boolean processar(Produto produto) {
        if (produto == null) {
            throw new IllegalArgumentException("Informe um produto pra processar.");
        }
        ligar();
        try {
            // Assim como no homogeneizador, uma falha aqui aumenta o risco avaliado na inspeção.
            if (verificarFalha()) {
                produto.aumentarProbabilidadeFalha(getProbabilidadeFalha());
            }
            desgasteAleatorio();
            return true;
        } finally {
            // Mesmo se a etapa der erro, não deixamos a máquina ligada.
            desligar();
        }
    }

    /**
     * Identifica esta etapa da linha.
     * @return tipo Empacotadora
     */
    @Override
    public String getTipo() {
        return "Empacotadora";
    }
}
