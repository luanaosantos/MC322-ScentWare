// Etapa de mistura dos produtos.

/**
 * Mistura o produto e pode aumentar o risco na inspecao.
 *
 * @author Luana Oliveira
 * @author Roberta Santos
 */
public class MaquinaHomogeneizador extends Maquina {
    /**
     * Cria o homogeneizador com os valores do cenario.
     *
     * @param nome nome da maquina
     * @param capacidadeMaxima capacidade finita e positiva
     * @param probabilidadeFalha chance basica entre zero e um
     * @param custoOperacao custo finito e nao negativo de cada uso
     * @param health saude entre zero e cem
     * @param maximoDesgaste desgaste maximo finito e nao negativo
     * @throws IllegalArgumentException se os dados forem invalidos, conforme a classe Maquina
     */
    public MaquinaHomogeneizador(
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
     * Mistura o produto e soma risco se a maquina falhar.
     *
     * @param produto unidade nao nula a processar
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
            produto.processar();

            // Falha na mistura aumenta o risco na inspecao.
            if (verificarFalha()) {
                produto.aumentarProbabilidadeFalha(getProbabilidadeFalha());
            }

            desgasteAleatorio();
            return true;
        } finally {
            // Desliga mesmo se der erro.
            desligar();
        }
    }

    /**
     * Retorna o tipo da maquina.
     *
     * @return tipo Homogeneizador
     */
    @Override
    public String getTipo() {
        return "Homogeneizador";
    }
}
