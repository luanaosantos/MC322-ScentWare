// Escolha da maior demanda q cabe no budget.

import java.util.List;

/**
 * Regra do Strategy q escolhe a maior demanda dentro do budget.
 *
 * @author Luana Oliveira
 * @author Roberta Santos
 */
public class EstrategiaMaximoProdutos implements EstrategiaProducao {
    /**
     * Pega a maior demanda q cabe no budget. Se empatar, pega a primeira. O estoque e conferido depois.
     *
     * @param demandas fila de demandas, na ordem de registro
     * @param orcamentoDisponivel budget maximo aceito pra demanda
     * @return maior demanda q cabe no budget, ou {@code null} se nao houver
     * @throws IllegalArgumentException se o budget usado na comparacao for negativo, NaN ou infinito
     */
    @Override
    public Demanda selecionarDemanda(List<Demanda> demandas, double orcamentoDisponivel) {
        Demanda demandaMaximoProdutos = null;

        // Pega a maior qtd entre as demandas q cabem no budget.
        for (Demanda demanda : demandas) {
            if (demanda.viabilidadeFinanceira(orcamentoDisponivel)) {
                if (demandaMaximoProdutos == null || demanda.getQuantidadeProdutos() > demandaMaximoProdutos.getQuantidadeProdutos()) {
                    demandaMaximoProdutos = demanda;
                }
            }
        }
        return demandaMaximoProdutos;
    }

    /**
     * Retorna o nome da regra.
     *
     * @return nome exibido no menu
     */
    @Override
    public String getNomeEstrategia() {
        return "Máximo de produtos dentro do budget";
    }
}
