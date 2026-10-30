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
    private final AtuadorDescarte atuador;

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
        this(nome, capacidadeMaxima, probabilidadeFalha, custoOperacao, health, maximoDesgaste, 2);
    }

    /**
     * Cria a inspecao com a qtd de sensores escolhida.
     *
     * @param nome nome da maquina
     * @param capacidadeMaxima capacidade positiva
     * @param probabilidadeFalha chance entre 0 e 1
     * @param custoOperacao custo de cada uso
     * @param health saude inicial
     * @param maximoDesgaste desgaste maximo por uso
     * @param quantidadeSensores qtd de sensores, pelo menos um
     * @throws IllegalArgumentException se algum valor for invalido
     */
    public MaquinaInspecao(String nome, double capacidadeMaxima, double probabilidadeFalha,
            double custoOperacao, double health, double maximoDesgaste, int quantidadeSensores) {
        super(nome, capacidadeMaxima, probabilidadeFalha, custoOperacao, health, maximoDesgaste, quantidadeSensores);
        atuador = new AtuadorDescarte();
    }

    /**
     * Sorteia a aprovacao. Se o atuador falhar, o produto e rejeitado.
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
            boolean detectou = detectarProduto();
            // Qualidade maior reduz a chance de rejeicao.
            // O risco das outras etapas aumenta essa chance.
            double chanceRejeicao = (1.0 - produto.getQualidade())
                    + produto.getProbabilidadeFalhaAcumulada() * 0.5;
            chanceRejeicao = Math.min(1.0, chanceRejeicao);

            boolean rejeitado = random.nextDouble() < chanceRejeicao;

            // Se a inspecao ou sensor falhar, o resultado pode sair errado.
            boolean falhou = verificarFalha();
            // Outra falha com sensor quebrado pode travar o atuador.
            if (falhou && !detectou) {
                atuador.registrarFalha();
            }
            if (falhou || !detectou) {
                rejeitado = !rejeitado;
            }
            // Sem separacao segura, o produto nao vai pro armazem.
            if (!atuador.encaminhar(rejeitado)) {
                rejeitado = true;
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

    /**
     * Mostra o estado do atuador de descarte.
     *
     * @return texto do atuador
     */
    @Override
    protected String diagnosticoParte() {
        return atuador.diagnostico();
    }

    /**
     * Confere se o atuador precisa de reparo.
     *
     * @return true se o atuador falhou
     */
    @Override
    protected boolean partePrecisaReparo() {
        return atuador.precisaReparo();
    }

    /** Repara o atuador de descarte. */
    @Override
    protected void repararParte() {
        atuador.reparar();
    }
}
