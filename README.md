# ScentWare

Sistema desenvolvido para a disciplina MC322 - Programação Orientada a Objetos, da Universidade Estadual de Campinas (UNICAMP). Esta versão corresponde à Tarefa 6.

A ScentWare é uma fábrica de cosméticos corporais que simula o controle das demandas, o estoque de matéria-prima, a fabricação, a inspeção e o armazenamento dos produtos acabados.

## Produtos

- Hidratante corporal
- Creme de mãos
- Esfoliante corporal

## Matéria-prima

- Óleo de amêndoas

## Funcionamento

A fábrica possui um homogeneizador, uma empacotadora e uma máquina de inspeção. O sistema permite atualizar demandas, comprar matéria-prima, acompanhar o orçamento, escolher estratégias de produção e consultar o armazém. No início, é possível escolher entre os cenários Ideal e Apocalíptico.

Na Tarefa 5, as máquinas passaram a ser agrupadas pela classe `LinhaDeProducao`. Cada máquina cria seu motor, seus sensores e uma parte específica da sua etapa: válvula, bico envasador ou atuador de descarte. A auditoria mostra a linha, as máquinas e suas partes. O menu também permite reparar as máquinas da linha.

## Execução

É preciso ter um JDK 17 instalado. O projeto inclui o Gradle Wrapper, então não é necessário instalar o Gradle separadamente. Na primeira execução, é preciso ter internet para baixar a distribuição.

Na pasta do projeto, execute:

**Windows:**

```powershell
.\gradlew.bat build
.\gradlew.bat run
.\gradlew.bat javadoc
```

**Linux ou macOS:**

```bash
./gradlew build
./gradlew run
./gradlew javadoc
```

`build` compila e empacota o projeto, `run` abre o menu e `javadoc` gera a documentação em `build/docs/javadoc/index.html`.

Para executar somente os testes automatizados:

```powershell
.\gradlew.bat test
```

No Linux ou macOS, use `./gradlew test`. O comando `build` também executa os testes. Além da distribuição do Gradle, a primeira execução dos testes baixa o JUnit 5 do Maven Central. O relatório HTML fica em `build/reports/tests/test/index.html`; os resultados XML ficam em `build/test-results/test/`.

## Como usar

1. Escolha o cenário de operação.
2. No menu de demandas, informe a quantidade do produto que deseja fabricar.
3. Consulte o estoque e o orçamento; compre matéria-prima se precisar.
4. No menu de fabricação, produza um item específico ou use a estratégia escolhida.
5. Consulte as demandas, o armazém e a auditoria para acompanhar os resultados.
6. Use a opção 7 para salvar em `dados/planta.xml`. Salve novamente depois das alterações que deseja manter.
7. Use a opção 8 para recuperar esse arquivo. O estado carregado substitui o atual por inteiro, inclusive coleções e orçamento.

Ao iniciar, o programa pede o cenário e monta a fábrica padrão. Se `dados/planta.xml` existir, tenta recuperar o estado salvo. Nesse caso, o cenário e a configuração recuperados passam a valer. Sem arquivo, continua com o cenário escolhido. Se o arquivo estiver inválido, mostra um aviso e mantém a fábrica inicial. Sair não salva automaticamente nem apaga o arquivo anterior.

## Escopo e Limitações

### Dados persistidos e dados mantidos em memória

O XML guarda o estoque de óleo de amêndoas, com identificação, nome, unidade e custo. Também guarda a linha na ordem de produção, as configurações das máquinas, saúde, quantidade de falhas e estado ligado/desligado. Cada máquina inclui seu motor, todos os sensores e sua parte específica: válvula, bico envasador ou atuador. As falhas das partes e o contador de descartes continuam disponíveis depois da leitura. Os construtores criam componentes novos para cada máquina; o carregamento recupera seus valores sem compartilhar as partes.

As demandas mantêm ordem, tipo, quantidade, custo e `StatusDemanda`. Orçamento, modelos de produto, produtos aprovados no armazém, estratégia, cenário, risco inicial e próximos números de lote/produto também são salvos. Escolhemos guardar esses dados para continuar a produção sem repor o orçamento, perder os produtos já aprovados ou repetir identificadores. Qualidade e tempo são definidos pelas subclasses dos produtos, como nas tarefas anteriores.

O estado interno dos sorteadores não é salvo. A fábrica recupera o mesmo estado, mas os sorteios seguintes podem ter resultados diferentes. O contador estático de produtos fabricados registra apenas a execução atual; os identificadores usam um contador próprio da fábrica, salvo no XML. Entradas do terminal, mensagens e produtos rejeitados não são armazenados. O armazém já guardava somente os aprovados; a quantidade que falta continua na demanda.

O formato tem versão `1` e aceita os tipos de produto, máquina e estratégia desta implementação. Não há conversão de outros formatos nem retomada de uma etapa no meio do lote: a produção do menu é síncrona. Um estado `EM_PRODUCAO` pode ser recuperado para consulta, mas não é escolhido pelas estratégias de demandas pendentes.

O carregamento rejeita arquivo inexistente, vazio, XML malformado, seções ausentes ou repetidas, tipos desconhecidos e valores incompatíveis com o domínio. Também confere referências aos modelos, custo das demandas, produtos aprovados e contadores. Uma falha mantém a fábrica em uso. Na escrita, um arquivo temporário é concluído antes de substituir o anterior; o programa tenta a troca atômica quando o sistema de arquivos permite. O menu mostra a exceção de persistência e permite continuar.

### Suíte de testes

Os testes seguem a separação entre demandas, produção, estoque, auditoria e partes descrita neste README. Cada teste monta os próprios objetos. Os arquivos de persistência ficam em diretórios `@TempDir`, separados de `dados/planta.xml`.

| Grupo JUnit | O que verifica |
| --- | --- |
| `DemandaTest` | Transições de estado, quantidade zero, consumo, custo e entradas inválidas. |
| `ComponentesTest` | Estado e reparo das partes, valores inválidos e preservação dos contadores. |
| `LinhaDeProducaoTest` | Ordem das etapas, composição própria, inspeção, auditoria e reparo. |
| `EstrategiaTest` | Ordem de chegada, maior demanda, limite de orçamento, empates e demandas encerradas. |
| `GerenciadorProducaoTest` | Compra, produção, rejeição, cancelamento por falta de recursos e proteção dos saldos. |
| `PersistenciaPlantaTest` | Salvar/carregar, atualizar XML, coleções e relações, componentes e falhas de arquivo ou domínio. |
| `MateriaPrimaXmlTest` | Recuperação de estoque zero e recusa de quantidade negativa. |
| `DemandaXmlTest` | Ordem da fila, estados e demanda concluída incompatível. |
| `ProdutoXmlTest` | Subclasses, estados de produto e tipos inválidos. |
| `MaquinaXmlTest` | Composição recuperada e componentes incompatíveis. |
| `EstrategiaXmlTest` | Nomes das estratégias e recusa de regra desconhecida. |
| `PlantaXmlTest` | Documento completo e relações entre dados da fábrica. |
| `DocumentoXmlTest` | Escrita, caracteres especiais, atualização do arquivo e rejeição de DTD/XML inválido. |
| `ElementosXmlTest` | Atributos obrigatórios, valores inválidos e estrutura das seções. |

Os casos que precisam de resultado exato usam probabilidades 0/1, desgaste zero ou uma inspeção controlada no teste do gerenciador. A inspeção real também é testada nos limites. Não é necessário digitar no terminal para executar a suíte.

## Organização

Os fontes ficam em `src/main/java/br/unicamp/ic/mc322/`:

- `Main`: menus e entrada do terminal.
- `model`: produtos, demandas, estoque, linha, máquinas e componentes.
- `service`: `GerenciadorProducao` e regras de operação da fábrica.
- `strategy`: interface e regras de escolha de demanda.
- `persistence`: entrada principal de salvamento e carregamento.
- `exception`: erros de negócio e persistência.

Os testes acompanham os pacotes em `src/test/java/br/unicamp/ic/mc322/`. O [plano da tarefa](Docs/Plans/PlanoTarefa6.md) registra as decisões; a [verificação](Docs/VerificacaoTarefa6.md) registra os comandos e resultados.

A persistência está dividida por responsabilidade:

```text
persistence/
├── PersistenciaPlanta.java
├── conversores/
│   ├── PlantaXml.java
│   ├── MateriaPrimaXml.java
│   ├── DemandaXml.java
│   ├── ProdutoXml.java
│   ├── MaquinaXml.java
│   └── EstrategiaXml.java
└── xml/
    ├── DocumentoXml.java
    └── ElementosXml.java
```

O menu chama somente `PersistenciaPlanta`. Ela usa `PlantaXml` para reunir os conversores e validar a fábrica, e `DocumentoXml` para ler ou gravar o arquivo. `ElementosXml` concentra consultas e validações comuns dos elementos. O formato XML continua na versão 1.

Cada classe de persistência tem seu próprio arquivo de teste, com as mesmas subpastas `conversores/` e `xml/`. `PersistenciaPlantaTest` verifica a integração pela entrada principal. A montagem da fábrica usada nesses testes fica em `persistence/apoio/FabricaTeste.java`, evitando repetir os mesmos dados em vários arquivos.

## Diagrama de classes

- [UML em PNG](Docs/Diagrams/DiagramaClassesTarefa5.png)
- [Arquivo editável em Mermaid](Docs/Diagrams/DiagramaClassesTarefa5.mmd)

O diagrama anterior mostra a composição introduzida na Tarefa 5. O fluxo de persistência da Tarefa 6 está disponível em [Mermaid editável](Docs/Diagrams/PersistenciaTarefa6.mmd) e [SVG](Docs/Diagrams/PersistenciaTarefa6.svg).

## Autoria

- Luana Oliveira dos Santos — RA 230559
- Roberta Santos Mascarenhas Nunes — RA 176643
