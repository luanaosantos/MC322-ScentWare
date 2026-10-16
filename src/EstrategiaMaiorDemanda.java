import java.util.List;

public class EstrategiaMaiorDemanda implements EstrategiaProducao {
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

    @Override
    public String getNomeEstrategia() {
        return "Maior lote de cuidados";
    }
}
