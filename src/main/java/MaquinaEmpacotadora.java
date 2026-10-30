// Etapa de embalagem dos produtos.

/**
 * Embala o produto e pode aumentar o risco na inspecao.
 *
 * @author Luana Oliveira
 * @author Roberta Santos
 */
public class MaquinaEmpacotadora extends Maquina {
    private final BicoEnvasador bicoEnvasador;
    /**
     * Cria a empacotadora com os valores do cenario.
     *
     * @param nome nome da maquina
     * @param capacidadeMaxima capacidade finita e positiva
     * @param probabilidadeFalha chance basica entre zero e um
     * @param custoOperacao custo finito e nao negativo de cada uso
     * @param health saude entre zero e cem
     * @param maximoDesgaste desgaste maximo finito e nao negativo
     * @throws IllegalArgumentException se os dados forem invalidos, conforme a classe Maquina
     */
    public MaquinaEmpacotadora(
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
     * Cria a empacotadora com a qtd de sensores escolhida.
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
    public MaquinaEmpacotadora(String nome, double capacidadeMaxima, double probabilidadeFalha,
            double custoOperacao, double health, double maximoDesgaste, int quantidadeSensores) {
        super(nome, capacidadeMaxima, probabilidadeFalha, custoOperacao, health, maximoDesgaste, quantidadeSensores);
        bicoEnvasador = new BicoEnvasador();
    }

    /**
     * Embala o produto e soma risco se a maquina, o bico ou um sensor falhar.
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
            boolean detectou = detectarProduto();
            boolean abriu = bicoEnvasador.abrirBicoEnvasador();
            boolean falhou = verificarFalha();
            if (falhou) {
                bicoEnvasador.registrarFalha();
            }
            // Sensor ou bico com falha aumentam o risco na inspecao.
            if (falhou || !detectou || !abriu) {
                produto.aumentarProbabilidadeFalha(getProbabilidadeFalha());
            }
            desgasteAleatorio();
            return true;
        } finally {
            // Desliga mesmo se der erro.
            bicoEnvasador.fechar();
            desligar();
        }
    }

    /**
     * Retorna o tipo da maquina.
     *
     * @return tipo Empacotadora
     */
    @Override
    public String getTipo() {
        return "Empacotadora";
    }

    /**
     * Mostra o estado do bico.
     *
     * @return texto do bico
     */
    @Override
    protected String diagnosticoParte() {
        return bicoEnvasador.diagnostico();
    }

    /**
     * Confere se o bico precisa de reparo.
     *
     * @return true se o bico falhou
     */
    @Override
    protected boolean partePrecisaReparo() {
        return bicoEnvasador.precisaReparo();
    }

    /** Repara o bico da maquina. */
    @Override
    protected void repararParte() {
        bicoEnvasador.reparar();
    }
}
