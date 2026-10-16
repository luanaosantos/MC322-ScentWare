// Escolha da demanda com mais itens pendentes.

import java.util.List;

/**
 * Regra do Strategy q prioriza a maior demanda pendente.
 *
 * @author Luana Oliveira
 * @author Roberta Santos
 */
public class EstrategiaMaiorDemanda implements EstrategiaProducao {
    /**
     * Pega a maior demanda pendente com itens. Nao filtra budget e, se empatar, pega a primeira.
     *
     * @param demandas fila de demandas, na ordem de registro
     * @param orcamentoDisponivel budget atual, nao usado nesta regra
     * @return maior candidata elegivel, ou {@code null} se nao houver nenhuma
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
     * Retorna o nome da regra.
     *
     * @return nome exibido no menu
     */
    @Override
    public String getNomeEstrategia() {
        return "Maior lote de cuidados";
    }
}
