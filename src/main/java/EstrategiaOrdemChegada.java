import java.util.List;

/**
 * Strategy que respeita a ordem de chegada das demandas elegíveis.
 *
 * @author Luana Oliveira
 * @author Roberta Santos
 */
public class EstrategiaOrdemChegada implements EstrategiaProducao {
    /**
     * Pega a primeira demanda pendente com itens pra produzir.
     * O budget não filtra a escolha; o gerenciador confere os recursos depois.
     *
     * @param demandas fila de demandas, na ordem de registro
     * @param orcamentoDisponivel budget atual, não usado nesta regra
     * @return primeira candidata elegível, ou {@code null} se não houver nenhuma
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
     * Consulta o nome da regra de chegada.
     *
     * @return nome exibido no menu
     */
    @Override
    public String getNomeEstrategia() {
        return "Ordem de chegada";
    }
}
