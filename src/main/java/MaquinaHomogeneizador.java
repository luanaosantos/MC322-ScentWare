// Etapa de mistura dos produtos.

/**
 * Mistura o produto e pode aumentar o risco na inspecao.
 *
 * @author Luana Oliveira
 * @author Roberta Santos
 */
public class MaquinaHomogeneizador extends Maquina {
    private final ValvulaHomogeneizadora valvula;
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
        this(nome, capacidadeMaxima, probabilidadeFalha, custoOperacao, health, maximoDesgaste, 2);
    }

    /**
     * Cria o homogeneizador com a qtd de sensores escolhida.
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
    public MaquinaHomogeneizador(String nome, double capacidadeMaxima, double probabilidadeFalha,
            double custoOperacao, double health, double maximoDesgaste, int quantidadeSensores) {
        super(nome, capacidadeMaxima, probabilidadeFalha, custoOperacao, health, maximoDesgaste, quantidadeSensores);
        valvula = new ValvulaHomogeneizadora();
    }

    /**
     * Mistura o produto e soma risco se a maquina, a valvula ou um sensor falhar.
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
            boolean detectou = detectarProduto();
            boolean abriu = valvula.abrir();
            boolean falhou = verificarFalha();
            if (falhou) {
                valvula.registrarFalha();
            }
            // Sensor ou valvula com falha aumentam o risco do produto.
            if (falhou || !detectou || !abriu) {
                produto.aumentarProbabilidadeFalha(getProbabilidadeFalha());
            }

            desgasteAleatorio();
            return true;
        } finally {
            // Desliga mesmo se der erro.
            valvula.fechar();
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

    /**
     * Mostra o estado da valvula.
     *
     * @return texto da valvula
     */
    @Override
    protected String diagnosticoParte() {
        return valvula.diagnostico();
    }

    /**
     * Confere se a valvula precisa de reparo.
     *
     * @return true se a valvula falhou
     */
    @Override
    protected boolean partePrecisaReparo() {
        return valvula.precisaReparo();
    }

    /** Repara a valvula da maquina. */
    @Override
    protected void repararParte() {
        valvula.reparar();
    }
}
