import java.util.List;

/**
 * Contrato do padrão Strategy pra escolher a próxima demanda.
 * O gerenciador usa essa interface e pode trocar a regra durante a execução,
 * sem repetir a lógica de fabricação em cada estratégia.
 *
 * @author Luana Oliveira
 * @author Roberta Santos
 */
public interface EstrategiaProducao {
    /**
     * Escolhe uma demanda pendente com quantidade positiva pela regra concreta.
     * A ordem da lista desempata as escolhas; só a estratégia de máximo de
     * produtos filtra pelo orçamento. A seleção não altera a demanda.
     *
     * @param demandas demandas na ordem em que foram registradas
     * @param orcamentoDisponivel budget atual, usado apenas se a regra filtrar custo
     * @return demanda escolhida, ou {@code null} se não houver candidata elegível
     * @throws IllegalArgumentException se a regra financeira consultar um budget
     *         negativo ou não finito
     */
    Demanda selecionarDemanda(List<Demanda> demandas, double orcamentoDisponivel);

    /**
     * Informa a regra ativa pra mostrar no menu.
     *
     * @return nome legível da estratégia
     */
    String getNomeEstrategia();
}
