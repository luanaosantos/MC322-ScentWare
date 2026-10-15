import java.util.List;

/**
 * Strategy que escolhe a maior quantidade entre as demandas que cabem no budget.
 *
 * @author Luana Oliveira
 * @author Roberta Santos
 */
public class EstrategiaMaximoProdutos implements EstrategiaProducao {
    /**
     * Compara as demandas pendentes com itens e custo estimado dentro do budget.
     * Em empate de quantidade, mantém a primeira da lista. A disponibilidade de
     * matéria-prima continua sendo conferida pelo gerenciador depois da seleção.
     *
     * @param demandas fila de demandas, na ordem de registro
     * @param orcamentoDisponivel budget máximo aceito pra demanda
     * @return maior candidata viável financeiramente, ou {@code null} se não houver
     * @throws IllegalArgumentException se a consulta de viabilidade receber budget
     *         negativo ou não finito
     */
    @Override
    public Demanda selecionarDemanda(List<Demanda> demandas, double orcamentoDisponivel) {
        Demanda demandaMaximoProdutos = null;

        // Entre as demandas que cabem no budget atual, escolhemos a que produz mais unidades
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
     * Consulta o nome da regra que considera o budget.
     *
     * @return nome exibido no menu
     */
    @Override
    public String getNomeEstrategia() {
        return "Máximo de produtos dentro do budget";
    }
}
