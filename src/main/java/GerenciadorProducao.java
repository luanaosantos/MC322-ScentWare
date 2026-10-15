import java.util.ArrayList;

/**
 * Organiza demandas, recursos e lotes da ScentWare.
 * Usa Strategy pra escolher a próxima demanda sem prender a fábrica a uma regra só.
 * Erros de recurso são conferidos antes do lote; o menu cuida de avisar quem usa.
 *
 * @author Luana Oliveira e Roberta Santos
 */
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

    /**
     * Monta a fábrica com seus modelos e a estratégia inicial.
     *
     * @param materiaPrima estoque usado na produção
     * @param budget orçamento inicial, finito e não negativo
     * @param produtosModelos modelos dos cosméticos disponíveis
     * @param estrategiaAtual regra inicial pra escolher demandas
     * @param riscoInicialProduto risco do cenário entre zero e um
     * @throws IllegalArgumentException se os dados iniciais forem inválidos
     */
    public GerenciadorProducao(
            MateriaPrima materiaPrima,
            double budget,
            ArrayList<Produto> produtosModelos,
            EstrategiaProducao estrategiaAtual,
            double riscoInicialProduto
    ) {
        if (materiaPrima == null || produtosModelos == null || produtosModelos.isEmpty()
                || estrategiaAtual == null) {
            throw new IllegalArgumentException("A fábrica precisa de estoque, produtos e estratégia.");
        }
        if (!Double.isFinite(budget) || budget < 0 || !Double.isFinite(riscoInicialProduto)
                || riscoInicialProduto < 0 || riscoInicialProduto > 1) {
            throw new IllegalArgumentException("Budget e risco inicial precisam estar nas faixas válidas.");
        }
        for (Produto produto : produtosModelos) {
            if (produto == null) {
                throw new IllegalArgumentException("A lista de modelos não pode conter produto nulo.");
            }
        }
        this.materiaPrima = materiaPrima;
        this.budget = budget;
        this.demandas = new ArrayList<>();
        this.produtosFabricados = new ArrayList<>();
        this.maquinas = new ArrayList<>();
        this.produtosModelos = new ArrayList<>(produtosModelos);
        this.estrategiaAtual = estrategiaAtual;
        this.riscoInicialProduto = riscoInicialProduto;
        this.proximoLote = 1;
    }

    /**
     * Troca a regra de seleção durante a execução, mantendo o padrão Strategy.
     *
     * @param novaEstrategia regra que passa a escolher as demandas
     * @throws IllegalArgumentException se a estratégia for nula
     */
    public void setEstrategia(EstrategiaProducao novaEstrategia) {
        if (novaEstrategia == null) {
            throw new IllegalArgumentException("Escolha uma estratégia válida.");
        }
        estrategiaAtual = novaEstrategia;
    }

    /**
     * Delega a escolha à estratégia e fabrica a demanda selecionada.
     * Se não houver uma elegível, só informa que não há trabalho na fila.
     *
     * @throws DemandaInvalidaException se o modelo da demanda não existir
     * @throws RecursoInsuficienteException se faltar recurso pra produção
     */
    public void executarProximaProducao() {
        // A escolha da próxima demanda fica por conta da estratégia ativa no momento
        Demanda proximaProducao = estrategiaAtual.selecionarDemanda(demandas, budget);

        if (proximaProducao == null) {
            System.out.println("Não existem demandas elegíveis na estratégia atual.");
            return;
        }

        Produto produtoModelo = buscarProdutoModelo(proximaProducao.getTipoProduto());
        if (produtoModelo == null) {
            throw new DemandaInvalidaException("Não existe um modelo de produto pra essa demanda.");
        }

        fabricarDemanda(proximaProducao, produtoModelo);
    }

    /**
     * Coloca uma demanda pendente na fila e calcula seu custo na linha atual.
     *
     * @param demanda demanda ainda não registrada
     * @throws DemandaInvalidaException se a demanda não puder entrar na fila
     * @throws IllegalArgumentException se o custo calculado não for finito
     */
    public void registrarDemanda(Demanda demanda) {
        if (demanda == null || demandas.contains(demanda)
                || demanda.getStatus() != StatusDemanda.PENDENTE
                || buscarProdutoModelo(demanda.getTipoProduto()) == null) {
            throw new DemandaInvalidaException("Informe uma demanda pendente válida e ainda não registrada.");
        }
        demanda.setCustoTotal(calcularCustoProducao(demanda.getQuantidadeProdutos()));
        demandas.add(demanda);
    }

    /**
     * Adiciona uma máquina à linha da fábrica.
     *
     * @param maquina máquina que vai participar dos próximos lotes
     * @throws IllegalArgumentException se a máquina for nula ou já estiver na linha
     * @throws PlantaException se já houver demandas com custo calculado pra essa linha
     */
    public void adicionarMaquina(Maquina maquina) {
        if (maquina == null || maquinas.contains(maquina)) {
            throw new IllegalArgumentException("Informe uma máquina válida que ainda não esteja na linha.");
        }
        if (!demandas.isEmpty()) {
            throw new PlantaException("Configure as máquinas antes de registrar as demandas.");
        }
        maquinas.add(maquina);
    }

    /**
     * Procura a primeira demanda pendente do tipo, incluindo qtd zero.
     *
     * @param tipoProduto tipo procurado na fila
     * @return demanda encontrada, ou null se ainda não houver uma pendente
     * @throws IllegalArgumentException se o tipo for nulo
     */
    public Demanda buscarDemandaPendente(TipoProduto tipoProduto) {
        if (tipoProduto == null) {
            throw new IllegalArgumentException("Escolha um tipo de produto válido.");
        }
        for (Demanda demanda : demandas) {
            if (demanda.getTipoProduto() == tipoProduto && demanda.getStatus() == StatusDemanda.PENDENTE) {
                return demanda;
            }
        }
        return null;
    }

    /**
     * Atualiza qtd e custo de uma demanda pendente sem criar outra escondido.
     *
     * @param tipoProduto tipo da demanda procurada
     * @param quantidade nova qtd, podendo ser zero
     * @return true se atualizou, ou false se não há uma demanda pendente desse tipo
     * @throws DemandaInvalidaException se a qtd for negativa
     * @throws IllegalArgumentException se o tipo ou o custo forem inválidos
     */
    public boolean atualizarDemanda(TipoProduto tipoProduto, int quantidade) {
        if (quantidade < 0) {
            throw new DemandaInvalidaException("A quantidade da demanda não pode ser negativa.");
        }
        Demanda demanda = buscarDemandaPendente(tipoProduto);
        if (demanda == null) {
            return false;
        }

        double custo = calcularCustoProducao(quantidade);
        demanda.atualizarQuantidade(quantidade);
        demanda.setCustoTotal(custo);
        return true;
    }

    /**
     * Compra matéria-prima depois de conferir qtd, custo, estoque final e budget.
     *
     * @param quantidade qtd positiva que vai entrar no estoque
     * @return true quando a compra foi concluída
     * @throws IllegalArgumentException se a qtd ou os totais não forem válidos
     * @throws RecursoInsuficienteException se o budget não cobrir a compra
     */
    public boolean comprarMateriaPrima(double quantidade) {
        if (!Double.isFinite(quantidade) || quantidade <= 0) {
            throw new IllegalArgumentException("A quantidade da compra precisa ser positiva e finita.");
        }

        double custo = quantidade * materiaPrima.getCustoPorUnidade();
        double novoEstoque = materiaPrima.getQuantidade() + quantidade;
        if (!Double.isFinite(custo) || !Double.isFinite(novoEstoque)) {
            throw new IllegalArgumentException("A quantidade informada deixa o custo ou estoque fora do limite.");
        }
        if (custo > budget) {
            throw new RecursoInsuficienteException(String.format(
                    "Budget insuficiente pra compra. Custo: R$ %.2f | Budget: R$ %.2f. "
                    + "Estoque e orçamento não foram alterados.", custo, budget));
        }

        // Adicionar já foi validado; só debitamos depois que o estoque recebeu a qtd.
        materiaPrima.adicionarEstoque(quantidade);
        budget -= custo;
        return true;
    }

    /**
     * Fabrica um lote depois de validar todos os recursos e o estado da demanda.
     * Falta de recurso cancela a demanda, como na Tarefa 3, sem iniciar um lote.
     * Rejeição na inspeção é normal: o recurso é usado e só os faltantes voltam à fila.
     *
     * @param demanda demanda pendente registrada na fábrica
     * @param produtoModelo modelo do mesmo tipo da demanda
     * @return true se atendeu tudo, ou false se restaram produtos rejeitados
     * @throws DemandaInvalidaException se demanda, tipo ou estado forem inválidos
     * @throws RecursoInsuficienteException se faltar estoque ou budget
     * @throws IllegalArgumentException se a linha ou os totais não forem válidos
     */
    public boolean fabricarDemanda(Demanda demanda, Produto produtoModelo) {
        if (demanda == null || !demandas.contains(demanda)) {
            throw new DemandaInvalidaException("Essa demanda não está registrada na fábrica.");
        }
        if (produtoModelo == null || produtoModelo.getTipo() != demanda.getTipoProduto()
                || buscarProdutoModelo(demanda.getTipoProduto()) != produtoModelo) {
            throw new DemandaInvalidaException("O modelo informado não corresponde ao produto da demanda.");
        }
        if (demanda.getStatus() != StatusDemanda.PENDENTE || demanda.getQuantidadeProdutos() <= 0) {
            throw new DemandaInvalidaException("Só uma demanda pendente com qtd positiva pode entrar na produção.");
        }
        if (maquinas.isEmpty() || !(maquinas.get(maquinas.size() - 1) instanceof MaquinaInspecao)) {
            throw new IllegalArgumentException("A linha precisa de máquinas e terminar na inspeção.");
        }

        int quantidadeSolicitada = demanda.getQuantidadeProdutos();
        double materiaPrimaNecessaria = demanda.calcularMateriaPrimaNecessaria(produtoModelo);
        double custoProducao = calcularCustoProducao(quantidadeSolicitada);
        if (!Double.isFinite(materiaPrimaNecessaria)) {
            throw new IllegalArgumentException("O consumo calculado passou do limite permitido.");
        }
        if (proximoLote == Integer.MAX_VALUE
                || quantidadeSolicitada > Integer.MAX_VALUE - Produto.getTotalProdutosFabricados()) {
            throw new IllegalArgumentException("O limite de lotes ou produtos dessa execução foi atingido.");
        }

        // Antes de começar o lote, verificamos se existem recursos suficientes para atender a demanda
        if (!materiaPrima.verificarDisponibilidade(materiaPrimaNecessaria)) {
            demanda.cancelar();
            throw new RecursoInsuficienteException(
                    "Matéria-prima insuficiente. Necessário: " + materiaPrimaNecessaria + " "
                    + materiaPrima.getUnidade() + ". Demanda cancelada. Estoque e budget não foram alterados.");
        }

        if (custoProducao > budget) {
            demanda.cancelar();
            throw new RecursoInsuficienteException(String.format(
                    "Budget insuficiente. Custo da produção: R$ %.2f | Budget: R$ %.2f. "
                    + "Demanda cancelada. Estoque e orçamento não foram alterados.", custoProducao, budget));
        }

        // Os objetos do lote tb são preparados antes de mudar demanda ou recurso.
        ArrayList<Produto> produtosLote = new ArrayList<>();
        for (int i = 0; i < quantidadeSolicitada; i++) {
            Produto produto = criarProduto(produtoModelo, Produto.getTotalProdutosFabricados() + i + 1);
            produto.aumentarProbabilidadeFalha(riscoInicialProduto);
            produto.setLote(proximoLote);
            produtosLote.add(produto);
        }

        demanda.iniciarProducao();
        // Consumir o total evita erro de arredondamento em vários descontos pequenos.
        materiaPrima.consumir(materiaPrimaNecessaria);

        // Cada tentativa de produção recebe um lote próprio, mesmo que parte dos produtos seja rejeitada
        int loteAtual = proximoLote++;
        int produtosAprovados = 0;

        System.out.println("\n[AVISO] - Iniciando produção de " + produtoModelo.getNome());
        System.out.println("Lote: " + loteAtual);

        for (Produto produto : produtosLote) {
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

                // O lote já cabe no budget; um resíduo de double não pode deixar saldo negativo.
                budget = Math.max(0.0, budget - maquina.getCustoOperacao());
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

    /**
     * Busca o modelo cadastrado pra um tipo de cosmético.
     *
     * @param tipoProduto tipo procurado
     * @return modelo correspondente, ou null se não estiver cadastrado
     */
    private Produto buscarProdutoModelo(TipoProduto tipoProduto) {
        for (Produto produto : produtosModelos) {
            if (produto.getTipo() == tipoProduto) {
                return produto;
            }
        }
        return null;
    }

    /**
     * Estima o custo de passar a qtd solicitada por todas as máquinas.
     *
     * @param quantidadeProdutos qtd não negativa de itens do lote
     * @return custo operacional total, sem compra de matéria-prima
     * @throws IllegalArgumentException se a qtd ou o custo calculado forem inválidos
     */
    private double calcularCustoProducao(int quantidadeProdutos) {
        if (quantidadeProdutos < 0) {
            throw new IllegalArgumentException("A quantidade de produtos não pode ser negativa.");
        }
        double custoPorProduto = 0;
        for (Maquina maquina : maquinas) {
            custoPorProduto += maquina.getCustoOperacao();
        }
        double custo = custoPorProduto * quantidadeProdutos;
        if (!Double.isFinite(custoPorProduto) || !Double.isFinite(custo)) {
            throw new IllegalArgumentException("O custo calculado passou do limite permitido.");
        }
        return custo;
    }

    /**
     * Disponibiliza a estimativa usada pelo menu ao cadastrar uma demanda.
     *
     * @param quantidadeProdutos qtd de itens a fabricar
     * @return custo operacional estimado do lote
     * @throws IllegalArgumentException se a qtd ou o custo calculado forem inválidos
     */
    public double getCustoProducao(int quantidadeProdutos) {
        return calcularCustoProducao(quantidadeProdutos);
    }

    /**
     * Cria um item independente do modelo, sem reaproveitar estado de outro lote.
     *
     * @param produtoModelo modelo cadastrado do cosmético
     * @param numeroProduto número sequencial do novo item
     * @return nova instância do tipo do modelo
     * @throws DemandaInvalidaException se o modelo não for de um tipo suportado
     */
    private Produto criarProduto(Produto produtoModelo, int numeroProduto) {
        String id = produtoModelo.getId() + "-" + numeroProduto;

        if (produtoModelo instanceof ProdutoHidratante) {
            return new ProdutoHidratante(id, produtoModelo.getNome(), produtoModelo.getQuantidadeMateriaPrimaPorUnidade());
        }
        if (produtoModelo instanceof ProdutoCremeDeMaos) {
            return new ProdutoCremeDeMaos(id, produtoModelo.getNome(), produtoModelo.getQuantidadeMateriaPrimaPorUnidade());
        }
        if (produtoModelo instanceof ProdutoEsfoliante) {
            return new ProdutoEsfoliante(id, produtoModelo.getNome(), produtoModelo.getQuantidadeMateriaPrimaPorUnidade());
        }
        throw new DemandaInvalidaException("O tipo desse modelo de produto não é suportado pela fábrica.");
    }

    /** Exibe uma auditoria comum pra produtos e máquinas que implementam Auditavel. */
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

    /** Mostra o diagnóstico individual dos produtos aprovados no armazém. */
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

    /** Mostra saúde, manutenção, falhas e chance base de cada máquina. */
    public void detalharMaquinas() {
        System.out.println("\n[MÁQUINAS]");
        for (Maquina maquina : maquinas) {
            System.out.println(
                    maquina.gerarRelatorioDiagnostico()
                    + " | Falha base: " + String.format("%.0f%%", maquina.getProbabilidadeFalha() * 100)
            );
        }
    }

    /** Exibe totais por tipo e detalhes dos lotes aprovados no armazém. */
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

    /** Lista demandas com a qtd que falta, o estado e o custo estimado. */
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

    /** Exibe o budget disponível pras próximas operações. */
    public void exibirBudget() {
        System.out.printf("Budget atual: R$ %.2f%n", budget);
    }

    /** Exibe qual regra Strategy está escolhendo as próximas demandas. */
    public void exibirEstrategiaAtual() {
        System.out.println("Estratégia atual: " + estrategiaAtual.getNomeEstrategia());
    }

    /**
     * Consulta o orçamento disponível.
     *
     * @return budget atual da fábrica
     */
    public double getBudget() {
        return budget;
    }

    /**
     * Consulta o estoque usado pela fábrica.
     *
     * @return matéria-prima compartilhada com a produção
     */
    public MateriaPrima getMateriaPrima() {
        return materiaPrima;
    }

    /**
     * Consulta os aprovados sem permitir apagar itens da lista interna por fora.
     *
     * @return cópia da lista de produtos aprovados
     */
    public ArrayList<Produto> getProdutosFabricados() {
        return new ArrayList<>(produtosFabricados);
    }

    /**
     * Consulta a fila sem expor a estrutura da lista interna.
     *
     * @return cópia da lista de demandas registradas
     */
    public ArrayList<Demanda> getDemandas() {
        return new ArrayList<>(demandas);
    }
}
