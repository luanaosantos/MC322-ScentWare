import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        exibirIntroducao();
        Scanner scanner = new Scanner(System.in);

        int cenario = 0; 
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
        


        // Matéria-prima principal usada pelos três produtos da fábrica.
        MateriaPrima oleoAmendoas = new MateriaPrima(
                "MP001",
                "Oleo de Amendoas",
                1000.0,
                "mL",
                0.10
        );

        // Estes objetos funcionam como modelos para criar os produtos de cada lote.
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
        // As estratégias ficam separadas para podermos trocar a regra de escolha durante a execução.
        EstrategiaProducao estrategiaOrdemChegada = new EstrategiaOrdemChegada();
        EstrategiaProducao estrategiaMaximoProdutos = new EstrategiaMaximoProdutos();
        EstrategiaProducao estrategiaMaiorDemanda = new EstrategiaMaiorDemanda();


        // O gerenciador concentra a lógica da fábrica e deixa o Main mais voltado para os menus.
        GerenciadorProducao gerenciador = new GerenciadorProducao(
                oleoAmendoas,
                cenarioAtual.getBudget(),
                new ArrayList<>(List.of(cremeDeMaos, hidratante, esfoliante)),
                estrategiaOrdemChegada,
                cenarioAtual.getRiscoInicialProduto()
        );

        // Começamos com uma demanda vazia de cada produto, que pode ser atualizada pelo menu.
        gerenciador.registrarDemanda(new Demanda(hidratante.getTipo(), 0, 0));
        gerenciador.registrarDemanda(new Demanda(cremeDeMaos.getTipo(), 0, 0));
        gerenciador.registrarDemanda(new Demanda(esfoliante.getTipo(), 0, 0));

        // As taxas de falha e desgaste mudam de acordo com o cenário escolhido.
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
        } while (opcao != 0);
        scanner.close();
    }

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
    private static void subMenuDemandas(){
        System.out.println("[ x ] DEMANDAS");
        System.out.println("     [ 1 ] - Atualizar demanda");
        System.out.println("     [ 2 ] - Listar demandas");
        System.out.println("     [ 0 ] - Voltar");
    }
    private static void subMenuAtualizarDemanda(){
        System.out.println("[ x ] ATUALIZAR DEMANDAS");
        System.out.println("     [ 1 ] - Atualizar demanda de creme de mãos");
        System.out.println("     [ 2 ] - Atualizar demanda de esfoliante");
        System.out.println("     [ 3 ] - Atualizar demanda de hidratante");
        System.out.println("     [ 0 ] - Voltar");
    }
    private static void subMenuFabricar(){
        System.out.println("[ x ] FABRICAR");
        System.out.println("     [ 1 ] - Processar próxima demanda");
        System.out.println("     [ 2 ] - Fabricar item específico");
        System.out.println("     [ 0 ] - Voltar");
    }
    private static void subMenuFabricarItemEspecifico(){
        System.out.println("[ x ] FABRICAR ITEM ESPECÍFICO");
        System.out.println("     [ 1 ] - Fabricar creme de mãos");
        System.out.println("     [ 2 ] - Fabricar esfoliante");
        System.out.println("     [ 3 ] - Fabricar hidratante");
        System.out.println("     [ 0 ] - Voltar");
    }
    private static void subMenuConsultar(){
        System.out.println("[ x ] CONSULTAR");
        System.out.println("     [ 1 ] - Ver armazém");
        System.out.println("     [ 2 ] - Ver estoque de matéria-prima");
        System.out.println("     [ 0 ] - Voltar");
    }
    private static void subMenugerenciarEstrategia(){
        System.out.println("[ x ] GERENCIAR ESTRATÉGIA");
        System.out.println("     [ 1 ] - Ordem de chegada");
        System.out.println("     [ 2 ] - Maior demanda");
        System.out.println("     [ 3 ] - Maximizar produção");
        System.out.println("     [ 0 ] - Voltar");
    }
    private static void subMenuAuditoria(){
        System.out.println("[ x ] AUDITORIA");
        System.out.println("     [ 1 ] - Relatório geral");
        System.out.println("     [ 2 ] - Detalhar máquinas");
        System.out.println("     [ 3 ] - Detalhar produtos");
        System.out.println("     [ 0 ] - Voltar");
    }
    private static void atualizarDemanda(
            Scanner scanner,
            GerenciadorProducao gerenciador,
            TipoProduto tipoProduto
    ) {
        System.out.print("\nInforme a quantidade de produtos: ");
        int quantidade = lerInteiro(scanner);

        if (quantidade < 0) {
            System.out.println("A quantidade da demanda não pode ser negativa.");
            return;
        }

        if (gerenciador.atualizarDemanda(tipoProduto, quantidade)) {
            System.out.println(
                    "Demanda de " + tipoProduto.getNome()
                    + " atualizada para "
                    + quantidade + " unidades."
            );
        } else {
            Demanda demanda = new Demanda(tipoProduto, quantidade, gerenciador.getCustoProducao(quantidade));
            gerenciador.registrarDemanda(demanda);
        }
    }

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
            System.out.println("Não existem demandas pendentes de " + produto.getNome());
        }
    } 

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

    private static int lerInteiro(Scanner scanner) {
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.print(
                        "Valor invalido. Digite um numero inteiro: "
                );
            }
        }
    }
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

    private static double lerDouble(Scanner scanner) {
        while (true) {
            try {
                double valor = Double.parseDouble(
                        scanner.nextLine().trim().replace(",", ".")
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
}
