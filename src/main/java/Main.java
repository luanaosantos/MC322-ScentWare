// Menu da fabrica e leitura das opcoes.

import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Cuida dos menus e das entradas. As regras da fabrica ficam no gerenciador.
 *
 * @author Luana Oliveira
 * @author Roberta Santos
 */
public class Main {
    /**
     * Abre o cenario e o menu. Encerra quando a pessoa sai ou acaba a entrada.
     *
     * @param args argumentos da execucao, nao usados pelos menus
     */
    public static void main(String[] args) {
        exibirIntroducao();
        Scanner scanner = new Scanner(System.in);

        try {
            int cenario;
            Cenario cenarioAtual = null;
            System.out.println("Cenários de operação disponíveis : ");
            System.out.println("1 - Ideal");
            System.out.println("2 - Apocalíptico");
            System.out.print("Escolha com qual cenário deseja executar : ");
            cenario = lerInteiroEntre(scanner, 1, 2);

            switch(cenario){
                case 1 :
                    cenarioAtual = Cenario.IDEAL;
                    break;
                case 2 :
                    cenarioAtual = Cenario.APOCALIPTICO;
                    break;
            }

            // Os tres produtos usam esse material.
            MateriaPrima oleoAmendoas = new MateriaPrima(
                    "MP001",
                    "Oleo de Amendoas",
                    1000.0,
                    "mL",
                    0.10
            );

            // Modelos pra criar os produtos de cada lote.
            Produto hidratante = new ProdutoHidratante(
                    "P001",
                    "Hidratante Corporal",
                    20.0
            );

            Produto cremeDeMaos = new ProdutoCremeDeMaos(
                    "P002",
                    "Creme de Maos",
                    30.0
            );

            Produto esfoliante = new ProdutoEsfoliante(
                    "P003",
                    "Esfoliante Corporal",
                    40.0
            );
            // Deixa as regras prontas pra trocar pelo menu.
            EstrategiaProducao estrategiaOrdemChegada = new EstrategiaOrdemChegada();
            EstrategiaProducao estrategiaMaximoProdutos = new EstrategiaMaximoProdutos();
            EstrategiaProducao estrategiaMaiorDemanda = new EstrategiaMaiorDemanda();


            // O gerenciador cuida das regras; o Main cuida do menu.
            GerenciadorProducao gerenciador = new GerenciadorProducao(
                    oleoAmendoas,
                    cenarioAtual.getBudget(),
                    new ArrayList<>(List.of(cremeDeMaos, hidratante, esfoliante)),
                    estrategiaOrdemChegada,
                    cenarioAtual.getRiscoInicialProduto()
            );

            // Falha e desgaste dependem do cenario.
            gerenciador.adicionarMaquina(
                    new MaquinaHomogeneizador(
                            "Homogeneizador",
                            1000.0,
                            cenarioAtual.getProbabilidadeFalhaHomogeneizador(),
                            2.0,
                            100.0,
                            cenarioAtual.getMaximoDesgasteHomogeneizador()
                    )
            );

            gerenciador.adicionarMaquina(
                    new MaquinaEmpacotadora(
                            "Empacotadora",
                            1000.0,
                            cenarioAtual.getProbabilidadeFalhaEmpacotador(),
                            1.0,
                            100.0,
                            cenarioAtual.getMaximoDesgasteEmpacotador()
                    )
            );

            gerenciador.adicionarMaquina(
                    new MaquinaInspecao(
                            "Inspecao",
                            1000.0,
                            cenarioAtual.getProbabilidadeFalhaInspecao(),
                            1.5,
                            100.0,
                            cenarioAtual.getMaximoDesgasteInspecao()
                    )
            );

            int opcao;
            int opcaoSecundaria;
            int opcao3;

            do {
                exibirMenuPrincipal(gerenciador, cenarioAtual);

                System.out.print("Escolha uma opcao: ");
                opcao = lerInteiroEntre(scanner, 0, 6);

                // Mostra o erro e deixa tentar outra operacao.
                try {
                    switch (opcao){
                        // Submenu de demandas.
                        case 1:
                                subMenuDemandas();
                                System.out.print("Escolha uma opcao: ");
                                opcaoSecundaria = lerInteiroEntre(scanner, 0 ,2);
                                switch(opcaoSecundaria){
                                    case 1:
                                        subMenuAtualizarDemanda();
                                        System.out.print("Escolha uma opcao: ");
                                        opcao3 = lerInteiroEntre(scanner, 0 ,3);
                                        switch(opcao3){
                                            case 1:
                                                atualizarDemanda(scanner,gerenciador,cremeDeMaos.getTipo());
                                                break;
                                            case 2:
                                                atualizarDemanda(scanner,gerenciador,esfoliante.getTipo());
                                                break;
                                            case 3:
                                                atualizarDemanda(scanner,gerenciador,hidratante.getTipo());
                                                break;
                                            case 0:
                                                break;
                                        }
                                        break;
                                    case 2:
                                        System.out.println("\n=== DEMANDAS ===");
                                        gerenciador.exibirDemandas();
                                        break;
                                    case 0:
                                        break;
                                }
                                break;
                        case 2:
                                subMenuFabricar();
                                System.out.print("Escolha uma opcao: ");
                                opcaoSecundaria = lerInteiroEntre(scanner, 0 , 2);
                                switch(opcaoSecundaria){
                                    case 1:
                                        gerenciador.executarProximaProducao();
                                        break;
                                    case 2:
                                        subMenuFabricarItemEspecifico();
                                        System.out.print("Escolha uma opcao: ");
                                        opcao3 = lerInteiroEntre(scanner, 0 , 3);
                                        switch(opcao3){
                                            case 1:
                                                fabricarProdutoEspecifico(gerenciador, cremeDeMaos);
                                            break;
                                            case 2:
                                                fabricarProdutoEspecifico(gerenciador, esfoliante);
                                            break;
                                            case 3:
                                                fabricarProdutoEspecifico(gerenciador, hidratante);
                                            break;
                                        }
                                        break;
                                    case 0:
                                        break;
                                }
                                break;

                        case 3:
                                subMenuConsultar();
                                System.out.print("Escolha uma opcao: ");
                                opcaoSecundaria = lerInteiroEntre(scanner, 0, 2);
                                switch(opcaoSecundaria){
                                    case 1:
                                        System.out.println("\n=== ARMAZEM ===");
                                        gerenciador.exibirArmazem();
                                        break;
                                    case 2:
                                        exibirEstoque(gerenciador);
                                        break;
                                    case 0:
                                        break;
                                }
                                break;
                        case 4:
                            comprarMateriaPrima(
                                    scanner,
                                    gerenciador
                            );
                            break;

                        case 5:
                                subMenugerenciarEstrategia();
                                System.out.print("Escolha uma opcao: ");
                                opcaoSecundaria = lerInteiroEntre(scanner, 0, 3);
                                switch(opcaoSecundaria){
                                    case 1:
                                        gerenciador.setEstrategia(estrategiaOrdemChegada);
                                        break;
                                    case 2:
                                        gerenciador.setEstrategia(estrategiaMaiorDemanda);
                                        break;
                                    case 3:
                                        gerenciador.setEstrategia(estrategiaMaximoProdutos);
                                        break;
                                    case 0:
                                        break;
                                }
                                break;
                        case 6:
                                subMenuAuditoria();
                                System.out.print("Escolha uma opcao: ");
                                opcaoSecundaria = lerInteiroEntre(scanner, 0, 3);
                                switch(opcaoSecundaria){
                                    case 1:
                                        gerenciador.gerarAuditoriaGeral();
                                        break;
                                    case 2:
                                        gerenciador.detalharMaquinas();
                                        break;
                                    case 3:
                                        gerenciador.detalharProdutos();
                                        break;
                                    case 0:
                                        break;
                                }
                                break;
                        case 0:
                            System.out.println("\nEncerrando a ScentWare...");
                            System.out.println("Ate a proxima! :)");
                            break;
                    }
                } catch (PlantaException | IllegalArgumentException e) {
                    System.out.println("\n[AVISO] - " + e.getMessage());
                    System.out.println("Confira os dados e consulte a demanda, o estoque ou o budget antes de tentar de novo.");
                }
            } while (opcao != 0);
        } catch (NoSuchElementException e) {
            System.out.println("\nEntrada encerrada. Encerrando a ScentWare...");
        } finally {
            scanner.close();
        }
    }

    /**
     * Mostra a apresentacao da fabrica.
     */
    private static void exibirIntroducao() {
        System.out.println("===============================================================");
        System.out.println("                         SCENTWARE");
        System.out.println("                 Cuidar de voce e Essencial");
        System.out.println("               Cuidar da sua pele e ScentWare");
        System.out.println("===============================================================");
        System.out.println("Bem-vindos! Somos uma fabrica de cosmeticos corporais.");
        System.out.println();
        System.out.println("Produtos:");
        System.out.println("[01] Hidratante Corporal");
        System.out.println("[02] Creme de Maos");
        System.out.println("[03] Esfoliante Corporal");
        System.out.println();
        System.out.println("Materia-prima principal: Oleo de Amendoas");
        System.out.println();
        System.out.println("Desenvolvido por: Luana Oliveira e Roberta Santos");
        System.out.println("===============================================================");
        System.out.println();
    }

    /**
     * Mostra as opcoes, o cenario e o budget.
     *
     * @param gerenciador fabrica que vai fornecer os dados do menu
     * @param cenarioAtual cenario escolhido no comeco da execucao
     */
    private static void exibirMenuPrincipal(GerenciadorProducao gerenciador, Cenario cenarioAtual) {
        System.out.println("\n================================");
        System.out.println("            SCENTWARE");
        System.out.println("================================");
        cenarioAtual.exibirCenarioAtual();
        gerenciador.exibirEstrategiaAtual();
        gerenciador.exibirBudget();
        System.out.println("[ 1 ] - DEMANDAS");
        System.out.println("[ 2 ] - FABRICAR");
        System.out.println("[ 3 ] - CONSULTAR");
        System.out.println("[ 4 ] - COMPRAR MATERIA-PRIMA");
        System.out.println("[ 5 ] - GERENCIAR ESTRATÉGIA");
        System.out.println("[ 6 ] - AUDITORIA");
        System.out.println("[ 0 ] - Sair");
        System.out.println("================================");
    }
    /**
     * Mostra as opcoes de demandas.
     */
    private static void subMenuDemandas(){
        System.out.println("[ x ] DEMANDAS");
        System.out.println("     [ 1 ] - Atualizar demanda");
        System.out.println("     [ 2 ] - Listar demandas");
        System.out.println("     [ 0 ] - Voltar");
    }
    /**
     * Mostra os produtos pra atualizar a demanda.
     */
    private static void subMenuAtualizarDemanda(){
        System.out.println("[ x ] ATUALIZAR DEMANDAS");
        System.out.println("     [ 1 ] - Atualizar demanda de creme de mãos");
        System.out.println("     [ 2 ] - Atualizar demanda de esfoliante");
        System.out.println("     [ 3 ] - Atualizar demanda de hidratante");
        System.out.println("     [ 0 ] - Voltar");
    }
    /**
     * Mostra as opcoes de fabricacao.
     */
    private static void subMenuFabricar(){
        System.out.println("[ x ] FABRICAR");
        System.out.println("     [ 1 ] - Processar próxima demanda");
        System.out.println("     [ 2 ] - Fabricar item específico");
        System.out.println("     [ 0 ] - Voltar");
    }
    /**
     * Mostra os produtos pra escolher oq fabricar.
     */
    private static void subMenuFabricarItemEspecifico(){
        System.out.println("[ x ] FABRICAR ITEM ESPECÍFICO");
        System.out.println("     [ 1 ] - Fabricar creme de mãos");
        System.out.println("     [ 2 ] - Fabricar esfoliante");
        System.out.println("     [ 3 ] - Fabricar hidratante");
        System.out.println("     [ 0 ] - Voltar");
    }
    /**
     * Mostra as opcoes de consulta.
     */
    private static void subMenuConsultar(){
        System.out.println("[ x ] CONSULTAR");
        System.out.println("     [ 1 ] - Ver armazém");
        System.out.println("     [ 2 ] - Ver estoque de matéria-prima");
        System.out.println("     [ 0 ] - Voltar");
    }
    /**
     * Mostra as regras pra escolher a proxima demanda.
     */
    private static void subMenugerenciarEstrategia(){
        System.out.println("[ x ] GERENCIAR ESTRATÉGIA");
        System.out.println("     [ 1 ] - Ordem de chegada");
        System.out.println("     [ 2 ] - Maior demanda");
        System.out.println("     [ 3 ] - Maximizar produção");
        System.out.println("     [ 0 ] - Voltar");
    }
    /**
     * Mostra as opcoes de auditoria.
     */
    private static void subMenuAuditoria(){
        System.out.println("[ x ] AUDITORIA");
        System.out.println("     [ 1 ] - Relatório geral");
        System.out.println("     [ 2 ] - Detalhar máquinas");
        System.out.println("     [ 3 ] - Detalhar produtos");
        System.out.println("     [ 0 ] - Voltar");
    }
    /**
     * Le a qtd e atualiza a demanda. Se nao houver uma pendente, cria outra.
     *
     * @param scanner entrada do terminal
     * @param gerenciador fabrica que guarda as demandas
     * @param tipoProduto tipo escolhido pra atualizar
     * @throws IllegalArgumentException se a qtd for negativa
     * @throws PlantaException se a demanda nao puder receber a atualizacao
     * @throws NoSuchElementException se a entrada acabar durante a leitura
     */
    private static void atualizarDemanda(
            Scanner scanner,
            GerenciadorProducao gerenciador,
            TipoProduto tipoProduto
    ) {
        System.out.print("\nInforme a quantidade de produtos: ");
        int quantidade = lerInteiro(scanner);

        if (quantidade < 0) {
            throw new IllegalArgumentException("A quantidade da demanda não pode ser negativa.");
        }

        if (gerenciador.atualizarDemanda(tipoProduto, quantidade)) {
            System.out.println(
                    "Demanda de " + tipoProduto.getNome()
                    + " atualizada para "
                    + quantidade + " unidades."
            );
        } else {
            // false indica q ainda nao tem demanda pendente.
            Demanda demanda = new Demanda(tipoProduto, quantidade, gerenciador.getCustoProducao(quantidade));
            gerenciador.registrarDemanda(demanda);
            System.out.println("Demanda de " + tipoProduto.getNome() + " registrada com " + quantidade + " unidades.");
        }
    }

    /**
     * Busca a demanda do produto e pede a fabricacao. Itens rejeitados podem ficar pendentes.
     *
     * @param gerenciador fabrica responsavel pela producao
     * @param produto modelo do produto escolhido no submenu
     * @throws DemandaInvalidaException se nao existir demanda pendente
     * @throws PlantaException se nao for possivel iniciar a producao
     */
    private static void fabricarProdutoEspecifico(
            GerenciadorProducao gerenciador,
            Produto produto) {

        Demanda demanda = gerenciador.buscarDemandaPendente(produto.getTipo());
        if (demanda != null){
            System.out.println(
                    "\nIniciando producao de "
                    + produto.getNome() + "..."
            );

            gerenciador.fabricarDemanda(
                    demanda,
                    produto
            );
        }
        else {
            throw new DemandaInvalidaException("Não existem demandas pendentes de " + produto.getNome() + ". Registre uma demanda antes de fabricar.");
        }
    }

    /**
     * Mostra a qtd e o custo da materia-prima.
     *
     * @param gerenciador fabrica que mantem o estoque
     */
    private static void exibirEstoque(GerenciadorProducao gerenciador) {
        MateriaPrima materiaPrima = gerenciador.getMateriaPrima();

        System.out.println("\n=== ESTOQUE DE MATERIA-PRIMA ===");

        System.out.println(
                materiaPrima.getNome()
                + ": "
                + materiaPrima.getQuantidade()
                + " "
                + materiaPrima.getUnidade()
        );

        System.out.printf(
                "Custo por unidade: R$ %.2f%n",
                materiaPrima.getCustoPorUnidade()
        );
    }

    /**
     * Le a qtd e pede a compra. So mostra sucesso se ela der certo.
     *
     * @param scanner entrada do terminal
     * @param gerenciador fabrica que valida e realiza a compra
     * @throws IllegalArgumentException se a qtd nao for valida pra compra
     * @throws RecursoInsuficienteException se o budget nao cobrir o custo
     * @throws NoSuchElementException se a entrada acabar durante a leitura
     */
    private static void comprarMateriaPrima(Scanner scanner, GerenciadorProducao gerenciador) {
        System.out.print(
                "\nInforme a quantidade de materia-prima que deseja comprar: "
        );

        double quantidade = lerDouble(scanner);

        double custo = quantidade * gerenciador.getMateriaPrima().getCustoPorUnidade();

        if (gerenciador.comprarMateriaPrima(quantidade)) {
            System.out.printf(
                    "Materia-prima comprada com sucesso. Custo: R$ %.2f%n",
                    custo
            );
        } else {
            System.out.println(
                    "Nao foi possivel realizar a compra. Verifique a quantidade e o budget."
            );
        }
    }

    /**
     * Le um inteiro. Se a entrada estiver errada, pede de novo.
     *
     * @param scanner entrada do terminal
     * @return inteiro informado, dentro dos limites do tipo int
     * @throws NoSuchElementException se a entrada acabar durante a leitura
     */
    private static int lerInteiro(Scanner scanner) {
        while (true) {
            try {
                return Integer.parseInt(lerLinha(scanner).trim());
            } catch (NumberFormatException e) {
                System.out.print(
                        "Valor invalido. Digite um numero inteiro: "
                );
            }
        }
    }
    /**
     * Le uma opcao dentro do intervalo do menu.
     *
     * @param scanner entrada do terminal
     * @param a menor opcao permitida, inclusiva
     * @param b maior opcao permitida, inclusiva
     * @return opcao valida dentro do intervalo
     * @throws NoSuchElementException se a entrada acabar durante a leitura
     */
    private static int lerInteiroEntre(Scanner scanner, int a, int b){
        int valorInteiro;
        while(true){
            valorInteiro= lerInteiro(scanner);
            if (valorInteiro >= a && valorInteiro <= b){
                return valorInteiro;
            }
            else{
                System.out.print("\n[OPÇÃO INVÁLIDA] - Insira um número inteiro entre " + a + " e " + b + " : ");
            }
        }
    }

    /**
     * Le um numero com ponto ou virgula. Nao aceita NaN nem infinito.
     *
     * @param scanner entrada do terminal
     * @return numero finito informado
     * @throws NoSuchElementException se a entrada acabar durante a leitura
     */
    private static double lerDouble(Scanner scanner) {
        while (true) {
            try {
                double valor = Double.parseDouble(
                        lerLinha(scanner).trim().replace(",", ".")
                );

                if (Double.isFinite(valor)) {
                    return valor;
                }

                System.out.print(
                        "Valor invalido. Digite um numero: "
                );
            } catch (NumberFormatException e) {
                System.out.print(
                        "Valor invalido. Digite um numero: "
                );
            }
        }
    }

    /**
     * Le a proxima linha. Se a entrada acabou, avisa o main pra encerrar.
     *
     * @param scanner entrada do terminal
     * @return proxima linha disponivel
     * @throws NoSuchElementException se nao houver outra linha
     */
    private static String lerLinha(Scanner scanner) {
        // Se a entrada acabou, o main fecha o Scanner e sai.
        if (!scanner.hasNextLine()) {
            throw new NoSuchElementException("A entrada do terminal terminou.");
        }
        return scanner.nextLine();
    }
}
