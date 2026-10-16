import java.util.List;

public class EstrategiaMaximoProdutos implements EstrategiaProducao {
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

    @Override
    public String getNomeEstrategia() {
        return "Máximo de produtos dentro do budget";
    }
}
