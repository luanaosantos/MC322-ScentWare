public class MaquinaHomogeneizador extends Maquina {
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
