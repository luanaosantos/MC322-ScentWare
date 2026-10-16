import java.util.ArrayList;

public class GerenciadorProducao {
    private ArrayList<Demanda> demandas;
    private ArrayList<Produto> produtosFabricados;
    private ArrayList<Maquina> maquinas;
    private ArrayList<Produto> produtosModelos;
    private MateriaPrima materiaPrima;
    private double budget;
    private EstrategiaProducao estrategiaAtual;
    private double riscoInicialProduto;
    private int proximoLote;

    public GerenciadorProducao(
            MateriaPrima materiaPrima,
            double budget,
            ArrayList<Produto> produtosModelos,
            EstrategiaProducao estrategiaAtual,
            double riscoInicialProduto
    ) {
        this.materiaPrima = materiaPrima;
        this.budget = budget;
        this.demandas = new ArrayList<>();
        this.produtosFabricados = new ArrayList<>();
        this.maquinas = new ArrayList<>();
        this.produtosModelos = produtosModelos;
        this.estrategiaAtual = estrategiaAtual;
        this.riscoInicialProduto = riscoInicialProduto;
        this.proximoLote = 1;
    }

    public void setEstrategia(EstrategiaProducao novaEstrategia) {
        if (novaEstrategia != null) {
            estrategiaAtual = novaEstrategia;
        }
    }

    public void executarProximaProducao() {
        // A escolha da próxima demanda fica por conta da estratégia ativa no momento
        Demanda proximaProducao = estrategiaAtual.selecionarDemanda(demandas, budget);

        if (proximaProducao == null) {
            System.out.println("Não existem demandas elegíveis na estratégia atual.");
            return;
        }

        Produto produtoModelo = buscarProdutoModelo(proximaProducao.getTipoProduto());
        if (produtoModelo == null) {
            System.out.println("Não foi encontrado um produto correspondente à demanda.");
            return;
        }

        fabricarDemanda(proximaProducao, produtoModelo);
    }

    public void registrarDemanda(Demanda demanda) {
        demandas.add(demanda);
    }

    public void adicionarMaquina(Maquina maquina) {
        maquinas.add(maquina);
    }

    public Demanda buscarDemandaPendente(TipoProduto tipoProduto) {
        for (Demanda demanda : demandas) {
            if (demanda.getTipoProduto() == tipoProduto && demanda.getStatus() == StatusDemanda.PENDENTE) {
                return demanda;
            }
        }
        return null;
    }

    public boolean atualizarDemanda(TipoProduto tipoProduto, int quantidade) {
        Demanda demanda = buscarDemandaPendente(tipoProduto);
        if (demanda == null || quantidade < 0) {
            return false;
        }

        demanda.atualizarQuantidade(quantidade);
        demanda.setCustoTotal(calcularCustoProducao(quantidade));
        return true;
    }

    public boolean comprarMateriaPrima(double quantidade) {
        if (!Double.isFinite(quantidade) || quantidade <= 0) {
            return false;
        }

        double custo = quantidade * materiaPrima.getCustoPorUnidade();
        if (custo > budget) {
            return false;
        }

        budget -= custo;
        materiaPrima.adicionarEstoque(quantidade);
        return true;
    }

    public boolean fabricarDemanda(Demanda demanda, Produto produtoModelo) {
        if (!demanda.iniciarProducao()) {
            System.out.println("[AVISO] - Essa demanda não pode ser iniciada.");
            return false;
        }

        int quantidadeSolicitada = demanda.getQuantidadeProdutos();
        double materiaPrimaNecessaria = demanda.calcularMateriaPrimaNecessaria(produtoModelo);
        double custoProducao = calcularCustoProducao(quantidadeSolicitada);

        // Antes de começar o lote, verificamos se existem recursos suficientes para atender a demanda
        if (!materiaPrima.verificarDisponibilidade(materiaPrimaNecessaria)) {
            demanda.cancelar();
            System.out.println(
                    "Matéria-prima insuficiente. Necessário: "
                    + materiaPrimaNecessaria + " "
                    + materiaPrima.getUnidade()
            );

            System.out.println(
                    "[AVISO] - Demanda cancelada por falta de matéria-prima."
            );

            return false;
        }

        if (custoProducao > budget) {
            demanda.cancelar();
            System.out.printf(
                    "Budget insuficiente. Custo da produção: R$ %.2f | Budget atual: R$ %.2f%n",
                    custoProducao,
                    budget
            );
            System.out.println("[AVISO] - Demanda cancelada por falta de orçamento.");
            return false;
        }

        // Cada tentativa de produção recebe um lote próprio, mesmo que parte dos produtos seja rejeitada
        int loteAtual = proximoLote++;
        int produtosAprovados = 0;

        System.out.println("\n[AVISO] - Iniciando produção de " + produtoModelo.getNome());
        System.out.println("Lote: " + loteAtual);

        for (int i = 0; i < quantidadeSolicitada; i++) {
            materiaPrima.consumir(produtoModelo.getQuantidadeMateriaPrimaPorUnidade());

            int numeroProduto = Produto.getTotalProdutosFabricados() + 1;
            Produto produto = criarProduto(produtoModelo, numeroProduto);
            produto.aumentarProbabilidadeFalha(riscoInicialProduto);
            produto.setLote(loteAtual);
            Produto.incrementarTotalProdutosFabricados();

            boolean aprovado = true;

            for (Maquina maquina : maquinas) {
                // Se a máquina chegou a zero de saúde, ela precisa ser reparada antes de continuar o lote.
                if (maquina.quebrada()) {
                    System.out.println("\n[AVISO] - Produção interrompida!");
                    System.out.println("[AVISO] - A máquina " + maquina.getNome() + " está quebrada.");
                    maquina.reparar();
                    System.out.println("[AVISO] - Máquina reparada. Produção retomada.");
                }

                budget -= maquina.getCustoOperacao();
                if (!maquina.processar(produto)) {
                    aprovado = false;
                    break;
                }
            }

            if (aprovado) {
                produtosFabricados.add(produto);
                produtosAprovados++;
            }
        }

        int produtosRejeitados = quantidadeSolicitada - produtosAprovados;

        System.out.println("\n=== RESULTADO DA PRODUÇÃO ===");
        System.out.println("Produtos processados: " + quantidadeSolicitada);
        System.out.println("Produtos aprovados: " + produtosAprovados);
        System.out.println("Produtos rejeitados: " + produtosRejeitados);

        if (produtosRejeitados == 0) {
            demanda.concluir();
            System.out.println("Demanda atendida completamente.");
            return true;
        }

        // A demanda volta para a fila somente com o que ainda ficou faltando produzir.
        demanda.voltarParaPendente();
        demanda.atualizarQuantidade(produtosRejeitados);
        demanda.setCustoTotal(calcularCustoProducao(produtosRejeitados));

        System.out.println(
                "Ainda faltam "
                + produtosRejeitados
                + " produto(s) para atender a demanda."
        );
        return false;
    }

    private Produto buscarProdutoModelo(TipoProduto tipoProduto) {
        for (Produto produto : produtosModelos) {
            if (produto.getTipo() == tipoProduto) {
                return produto;
            }
        }
        return null;
    }

    private double calcularCustoProducao(int quantidadeProdutos) {
        double custoPorProduto = 0;
        for (Maquina maquina : maquinas) {
            custoPorProduto += maquina.getCustoOperacao();
        }
        return custoPorProduto * quantidadeProdutos;
    }

    public double getCustoProducao(int quantidadeProdutos) {
        return calcularCustoProducao(quantidadeProdutos);
    }

    private Produto criarProduto(Produto produtoModelo, int numeroProduto) {
        String id = produtoModelo.getId() + "-" + numeroProduto;

        if (produtoModelo instanceof ProdutoHidratante) {
            return new ProdutoHidratante(id, produtoModelo.getNome(), produtoModelo.getQuantidadeMateriaPrimaPorUnidade());
        }
        if (produtoModelo instanceof ProdutoCremeDeMaos) {
            return new ProdutoCremeDeMaos(id, produtoModelo.getNome(), produtoModelo.getQuantidadeMateriaPrimaPorUnidade());
        }
        return new ProdutoEsfoliante(id, produtoModelo.getNome(), produtoModelo.getQuantidadeMateriaPrimaPorUnidade());
    }

    public void gerarAuditoriaGeral() {
        System.out.println("\n=== AUDITORIA GERAL ===");
        ArrayList<Auditavel> itensAuditaveis = new ArrayList<>();
        itensAuditaveis.addAll(maquinas);
        itensAuditaveis.addAll(produtosFabricados);

        if (itensAuditaveis.isEmpty()) {
            System.out.println("Não existem itens para auditar.");
            return;
        }

        // Como máquinas e produtos são Auditavel, o mesmo laço serve para os dois tipos de objeto.
        for (Auditavel item : itensAuditaveis) {
            System.out.println(item.gerarRelatorioDiagnostico());
        }
    }

    public void detalharProdutos() {
        System.out.println("\n[PRODUTOS]");
        if (produtosFabricados.isEmpty()) {
            System.out.println("Ainda não existem produtos acabados.");
            return;
        }

        for (Produto produto : produtosFabricados) {
            System.out.println(produto.gerarRelatorioDiagnostico());
        }
    }

    public void detalharMaquinas() {
        System.out.println("\n[MÁQUINAS]");
        for (Maquina maquina : maquinas) {
            System.out.println(
                    maquina.gerarRelatorioDiagnostico()
                    + " | Falha base: " + String.format("%.0f%%", maquina.getProbabilidadeFalha() * 100)
            );
        }
    }

    public void exibirArmazem() {
        System.out.println("\n=== ARMAZÉM DE PRODUTOS ACABADOS ===");
        if (produtosFabricados.isEmpty()) {
            System.out.println("O armazém está vazio.");
            return;
        }

        for (TipoProduto tipo : TipoProduto.values()) {
            int quantidade = 0;
            double somaQualidade = 0.0;

            for (Produto produto : produtosFabricados) {
                if (produto.getTipo() == tipo) {
                    quantidade++;
                    somaQualidade += produto.getQualidade();
                }
            }

            if (quantidade > 0) {
                System.out.printf(
                        "%s | Quantidade: %d | Qualidade média: %.2f%n",
                        tipo.getNome(),
                        quantidade,
                        somaQualidade / quantidade
                );
            }
        }

        System.out.println("\nDetalhes dos lotes:");
        for (Produto produto : produtosFabricados) {
            System.out.println(
                    produto.getId() + " | " + produto.getNome()
                    + " | Lote: " + produto.getLote()
                    + " | Qualidade: " + produto.getQualidade()
                    + " | Risco: " + (produto.precisaManutencao() ? "Atenção" : "Normal")
            );
        }
    }

    public void exibirDemandas() {
        if (demandas.isEmpty()) {
            System.out.println("Não existem demandas.");
            return;
        }

        for (Demanda demanda : demandas) {
            System.out.println(
                    "Tipo: " + demanda.getTipoProduto().getNome()
                    + " | Quantidade: " + demanda.getQuantidadeProdutos()
                    + " | Status: " + demanda.getStatus().getDescricao()
                    + " | Custo estimado: R$ " + String.format("%.2f", demanda.getCustoTotal())
            );
        }
        System.out.println();
    }

    public void exibirBudget() {
        System.out.printf("Budget atual: R$ %.2f%n", budget);
    }

    public void exibirEstrategiaAtual() {
        System.out.println("Estratégia atual: " + estrategiaAtual.getNomeEstrategia());
    }

    public double getBudget() {
        return budget;
    }

    public MateriaPrima getMateriaPrima() {
        return materiaPrima;
    }

    public ArrayList<Produto> getProdutosFabricados() {
        return produtosFabricados;
    }

    public ArrayList<Demanda> getDemandas() {
        return demandas;
    }
}
