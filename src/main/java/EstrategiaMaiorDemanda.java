import java.util.List;

/**
 * Strategy que dá prioridade à maior quantidade ainda pendente.
 *
 * @author Luana Oliveira
 * @author Roberta Santos
 */
public class EstrategiaMaiorDemanda implements EstrategiaProducao {
    /**
     * Escolhe a maior demanda pendente com quantidade positiva, sem filtrar budget.
     * Em empate, mantém a primeira da lista; o gerenciador valida os recursos.
     *
     * @param demandas fila de demandas, na ordem de registro
     * @param orcamentoDisponivel budget atual, não usado nesta regra
     * @return maior candidata elegível, ou {@code null} se não houver nenhuma
     */
    @Override
    public Demanda selecionarDemanda(List<Demanda> demandas, double orcamentoDisponivel) {
        Demanda maiorDemanda = null;

        for (Demanda demanda : demandas) {
            if (demanda.getStatus() == StatusDemanda.PENDENTE && demanda.getQuantidadeProdutos() > 0) {
                if (maiorDemanda == null || demanda.getQuantidadeProdutos() > maiorDemanda.getQuantidadeProdutos()) {
                    maiorDemanda = demanda;
                }
            }
        }
        return maiorDemanda;
    }

    /**
     * Consulta o nome da regra de maior demanda.
     *
     * @return nome exibido no menu
     */
    @Override
    public String getNomeEstrategia() {
        return "Maior lote de cuidados";
    }
}
