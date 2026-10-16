// Contrato das regras de escolha de demanda.

import java.util.List;

/**
 * Contrato do Strategy. O gerenciador pode trocar a regra sem mudar a producao.
 *
 * @author Luana Oliveira
 * @author Roberta Santos
 */
public interface EstrategiaProducao {
    /**
     * Escolhe sem mudar a demanda. A ordem desempata; so a regra de maximo de produtos filtra budget.
     *
     * @param demandas demandas na ordem em que foram registradas
     * @param orcamentoDisponivel budget atual, usado apenas se a regra filtrar custo
     * @return demanda escolhida, ou {@code null} se nao houver candidata elegivel
     * @throws IllegalArgumentException se a regra financeira consultar um budget negativo ou nao finito
     */
    Demanda selecionarDemanda(List<Demanda> demandas, double orcamentoDisponivel);

    /**
     * Retorna o nome da regra.
     *
     * @return nome legivel da estrategia
     */
    String getNomeEstrategia();
}
