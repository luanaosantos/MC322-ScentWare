// Controle da producao, dos recursos e das demandas.

import java.util.ArrayList;

/**
 * Controla a fabrica. Usa Strategy pra escolher a demanda e confere os recursos antes do lote.
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
     * Cria a fabrica com estoque, budget, modelos e regra inicial.
     *
     * @param materiaPrima estoque usado na producao
     * @param budget budget inicial, finito e nao negativo
     * @param produtosModelos modelos dos produtos disponiveis
     * @param estrategiaAtual regra inicial pra escolher demandas
     * @param riscoInicialProduto risco do cenario entre zero e um
     * @throws IllegalArgumentException se os dados iniciais forem invalidos
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
     * Troca a regra de escolha da demanda usando Strategy.
     *
     * @param novaEstrategia regra que passa a escolher as demandas
     * @throws IllegalArgumentException se a estrategia for nula
     */
    public void setEstrategia(EstrategiaProducao novaEstrategia) {
        if (novaEstrategia == null) {
            throw new IllegalArgumentException("Escolha uma estratégia válida.");
        }
        estrategiaAtual = novaEstrategia;
    }

    /**
     * Escolhe e fabrica a proxima demanda. Se nao houver nenhuma, avisa no menu.
     *
     * @throws DemandaInvalidaException se o modelo da demanda nao existir
     * @throws RecursoInsuficienteException se faltar recurso pra producao
     */
    public void executarProximaProducao() {
        // A regra atual escolhe a proxima demanda.
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
     * Coloca a demanda na fila e calcula seu custo.
     *
     * @param demanda demanda ainda nao registrada
     * @throws DemandaInvalidaException se a demanda nao puder entrar na fila
     * @throws IllegalArgumentException se o custo calculado nao for finito
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
     * Adiciona a maquina antes do cadastro das demandas.
     *
     * @param maquina maquina que vai participar dos proximos lotes
     * @throws IllegalArgumentException se a maquina for nula ou ja estiver na linha
     * @throws PlantaException se ja houver demandas com custo calculado pra essa linha
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
     * Busca a primeira demanda pendente desse tipo, mesmo com qtd zero.
     *
     * @param tipoProduto tipo procurado na fila
     * @return demanda encontrada, ou null se ainda nao houver uma pendente
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
     * Atualiza qtd e custo. Se nao houver demanda pendente, retorna false.
     *
     * @param tipoProduto tipo da demanda procurada
     * @param quantidade nova qtd, podendo ser zero
     * @return true se atualizou, ou false se nao ha uma demanda pendente desse tipo
     * @throws DemandaInvalidaException se a qtd for negativa
     * @throws IllegalArgumentException se o tipo ou o custo forem invalidos
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
     * Confere a qtd e o budget antes de comprar materia-prima.
     *
     * @param quantidade qtd positiva que vai entrar no estoque
     * @return true quando a compra foi concluida
     * @throws IllegalArgumentException se a qtd ou os totais nao forem validos
     * @throws RecursoInsuficienteException se o budget nao cobrir a compra
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

        // So desconta o custo depois de adicionar o material.
        materiaPrima.adicionarEstoque(quantidade);
        budget -= custo;
        return true;
    }

    /**
     * Confere tudo antes de iniciar o lote. Falta de recurso cancela sem consumir ou debitar. Se a inspecao rejeitar itens, so os faltantes voltam pra fila.
     *
     * @param demanda demanda pendente registrada na fabrica
     * @param produtoModelo modelo do mesmo tipo da demanda
     * @return true se atendeu tudo, ou false se restaram produtos rejeitados
     * @throws DemandaInvalidaException se demanda, tipo ou estado forem invalidos
     * @throws RecursoInsuficienteException se faltar estoque ou budget
     * @throws IllegalArgumentException se a linha ou os totais nao forem validos
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

        // Confere os recursos antes de comecar.
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

        // Cria os produtos antes de mexer nos recursos.
        ArrayList<Produto> produtosLote = new ArrayList<>();
        for (int i = 0; i < quantidadeSolicitada; i++) {
            Produto produto = criarProduto(produtoModelo, Produto.getTotalProdutosFabricados() + i + 1);
            produto.aumentarProbabilidadeFalha(riscoInicialProduto);
            produto.setLote(proximoLote);
            produtosLote.add(produto);
        }

        demanda.iniciarProducao();
        // Desconta o total de uma vez pra evitar arredondamento.
        materiaPrima.consumir(materiaPrimaNecessaria);

        // Cada tentativa recebe um lote novo, mesmo se houver rejeicao.
        int loteAtual = proximoLote++;
        int produtosAprovados = 0;

        System.out.println("\n[AVISO] - Iniciando produção de " + produtoModelo.getNome());
        System.out.println("Lote: " + loteAtual);

        for (Produto produto : produtosLote) {
            Produto.incrementarTotalProdutosFabricados();

            boolean aprovado = true;

            for (Maquina maquina : maquinas) {
                // Se quebrou, repara antes de continuar.
                if (maquina.quebrada()) {
                    System.out.println("\n[AVISO] - Produção interrompida!");
                    System.out.println("[AVISO] - A máquina " + maquina.getNome() + " está quebrada.");
                    maquina.reparar();
                    System.out.println("[AVISO] - Máquina reparada. Produção retomada.");
                }

                // Evita saldo negativo por arredondamento.
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

        // Volta pra fila so oq falta produzir.
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
     * Busca o modelo do produto pelo tipo.
     *
     * @param tipoProduto tipo procurado
     * @return modelo do mesmo tipo, ou null se nao estiver guardado
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
     * Soma o custo das maquinas pra qtd pedida.
     *
     * @param quantidadeProdutos qtd nao negativa de itens do lote
     * @return custo operacional total, sem compra de materia-prima
     * @throws IllegalArgumentException se a qtd ou o custo calculado forem invalidos
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
     * Retorna o custo de producao da qtd pedida.
     *
     * @param quantidadeProdutos qtd de itens a fabricar
     * @return custo operacional estimado do lote
     * @throws IllegalArgumentException se a qtd ou o custo calculado forem invalidos
     */
    public double getCustoProducao(int quantidadeProdutos) {
        return calcularCustoProducao(quantidadeProdutos);
    }

    /**
     * Cria um produto novo a partir do modelo, sem copiar o status de outro lote.
     *
     * @param produtoModelo modelo guardado do produto
     * @param numeroProduto numero sequencial do novo item
     * @return nova objeto do tipo do modelo
     * @throws DemandaInvalidaException se o modelo nao for de um tipo suportado
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

    /** Mostra a auditoria dos produtos e das maquinas usando Auditavel. */
    public void gerarAuditoriaGeral() {
        System.out.println("\n=== AUDITORIA GERAL ===");
        ArrayList<Auditavel> itensAuditaveis = new ArrayList<>();
        itensAuditaveis.addAll(maquinas);
        itensAuditaveis.addAll(produtosFabricados);

        if (itensAuditaveis.isEmpty()) {
            System.out.println("Não existem itens para auditar.");
            return;
        }

        // Auditavel permite usar o mesmo laco pra produtos e maquinas.
        for (Auditavel item : itensAuditaveis) {
            System.out.println(item.gerarRelatorioDiagnostico());
        }
    }

    /** Mostra os dados dos produtos aprovados. */
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

    /** Mostra saude, falhas e manutencao das maquinas. */
    public void detalharMaquinas() {
        System.out.println("\n[MÁQUINAS]");
        for (Maquina maquina : maquinas) {
            System.out.println(
                    maquina.gerarRelatorioDiagnostico()
                    + " | Falha base: " + String.format("%.0f%%", maquina.getProbabilidadeFalha() * 100)
            );
        }
    }

    /** Mostra os produtos guardados e os dados dos lotes. */
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

    /** Mostra qtd, status e custo das demandas. */
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

    /** Mostra o budget atual. */
    public void exibirBudget() {
        System.out.printf("Budget atual: R$ %.2f%n", budget);
    }

    /** Mostra a regra usada pra escolher a demanda. */
    public void exibirEstrategiaAtual() {
        System.out.println("Estratégia atual: " + estrategiaAtual.getNomeEstrategia());
    }

    /**
     * Retorna o budget atual.
     *
     * @return budget atual da fabrica
     */
    public double getBudget() {
        return budget;
    }

    /**
     * Retorna a materia-prima usada pela fabrica.
     *
     * @return materia-prima compartilhada com a producao
     */
    public MateriaPrima getMateriaPrima() {
        return materiaPrima;
    }

    /**
     * Retorna uma copia da lista de produtos aprovados.
     *
     * @return copia da lista de produtos aprovados
     */
    public ArrayList<Produto> getProdutosFabricados() {
        return new ArrayList<>(produtosFabricados);
    }

    /**
     * Retorna uma copia da lista de demandas.
     *
     * @return copia da lista de demandas registradas
     */
    public ArrayList<Demanda> getDemandas() {
        return new ArrayList<>(demandas);
    }
}
