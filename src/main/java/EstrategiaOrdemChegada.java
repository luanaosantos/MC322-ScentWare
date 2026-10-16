// Escolha da demanda por ordem de chegada.

import java.util.List;

/**
 * Regra do Strategy q segue a ordem de chegada.
 *
 * @author Luana Oliveira
 * @author Roberta Santos
 */
public class EstrategiaOrdemChegada implements EstrategiaProducao {
    /**
     * Pega a primeira demanda pendente com itens. O budget e conferido depois.
     *
     * @param demandas fila de demandas, na ordem de registro
     * @param orcamentoDisponivel budget atual, nao usado nesta regra
     * @return primeira candidata elegivel, ou {@code null} se nao houver nenhuma
     */
    @Override
    public Demanda selecionarDemanda(List<Demanda> demandas, double orcamentoDisponivel) {
        for (Demanda demanda : demandas) {
            if (demanda.getStatus() == StatusDemanda.PENDENTE && demanda.getQuantidadeProdutos() > 0) {
                return demanda;
            }
        }
        return null;
    }

    /**
     * Retorna o nome da regra.
     *
     * @return nome exibido no menu
     */
    @Override
    public String getNomeEstrategia() {
        return "Ordem de chegada";
    }
}
