# ScentWare
Sistema desenvolvido para a disciplina MC322 - Programação Orientada a Objetos, da Universidade Estadual de Campinas (UNICAMP). Esta versão corresponde à Tarefa 4.
A ScentWare é uma fábrica de cosméticos corporais que simula o processo de produção desde o controle das demandas e da matéria-prima até a inspeção e o armazenamento dos produtos acabados.

## Produtos

- Hidratante corporal
- Creme de mãos
- Esfoliante corporal

## Matéria-prima

- Óleo de amêndoas

## Estrutura atual do sistema

A linha de produção possui:

- estoque de matéria-prima;
- homogeneizador;
- empacotadora;
- máquina de inspeção;
- armazém de produtos acabados;
- gerenciamento de demandas e orçamento;
- estratégias de seleção de demandas;
- auditoria de produtos e máquinas;
- cenários Ideal e Apocalíptico.

## Tarefa 3

Nesta versão foram adicionados o padrão Strategy, com estratégias por ordem de chegada, maior demanda e máximo de produtos, o enum `StatusDemanda`, a interface `Auditavel`, o desgaste progressivo das máquinas, os cenários Ideal e Apocalíptico e a consulta detalhada do armazém de produtos acabados.
O menu também foi reorganizado em submenus para separar as funcionalidades de demandas, fabricação, consultas, estratégias e auditoria.

## Tarefa 4

A construção agora usa Gradle e o Wrapper do projeto. O código ganhou Javadoc antes dos métodos e construtores, validações no domínio e exceções próprias pra demandas inválidas e falta de recursos. O menu informa os erros recuperáveis e permite continuar usando a fábrica.

As funcionalidades anteriores foram mantidas: três cosméticos, óleo de amêndoas, linha com três máquinas, orçamento, estoque, armazém, lotes, estratégias, auditoria e os dois cenários.

## Execução

É preciso ter um JDK 17 instalado e acessível pelo `JAVA_HOME` ou pelo `PATH`. O Wrapper usa Gradle 8.14.3 com checksum da distribuição, então não precisa instalar Gradle separado. Na primeira execução, precisa de internet pra baixar essa distribuição. O projeto não usa dependências externas nem framework de testes.

No Windows, a partir da pasta do projeto:

```powershell
.\gradlew.bat clean build
.\gradlew.bat run
.\gradlew.bat javadoc
```

No Linux ou macOS:

```bash
./gradlew clean build
./gradlew run
./gradlew javadoc
```

`build` compila e empacota o projeto; `run` abre o menu no terminal; `javadoc` gera a documentação em `build/docs/javadoc/index.html`. Esses comandos têm objetivos diferentes. Os resultados das verificações ficam em [VerificacaoTarefa4](Docs/VerificacaoTarefa4.md).

## Como usar

1. Escolha o cenário Ideal ou Apocalíptico.
2. No submenu de demandas, cadastre ou atualize a qtd de um dos cosméticos.
3. Consulte o estoque e o budget; compre matéria-prima se precisar.
4. Use o submenu de fabricação pra produzir diretamente ou pela estratégia atual.
5. Consulte demandas, armazém e auditoria pra acompanhar os resultados.

Se digitar uma opção inválida ou tentar uma operação sem os requisitos, o programa mostra uma mensagem e volta à interação. Qtd zero pode ficar pendente, mas não entra na produção. Não dá pra alterar uma demanda concluída ou cancelada; o cadastro cria uma nova quando não há pendente daquele tipo.

Falta de estoque ou budget na fabricação cancela a demanda sem consumir material, debitar orçamento ou abrir lote. Já a rejeição na inspeção é parte normal da simulação: os recursos da tentativa são usados e só os itens faltantes voltam pra fila. As máquinas quebradas continuam com reparo automático. Na API, monte a linha de máquinas antes de registrar demandas, pra manter as estimativas de custo coerentes.

## Organização

```text
src/main/java/       Código Java
gradle/wrapper/      Wrapper e configuração da distribuição
Docs/Plans/         Plano e enunciado da Tarefa 4
Docs/Diagrams/      Diagramas em Mermaid
Docs/               Verificação e preparação da entrega
build/              Saídas geradas pelo Gradle (não versionadas)
```

Os nomes exigidos pelo Gradle seguem o padrão da ferramenta. As classes, pastas e documentos novos do grupo usam PascalCase; métodos e variáveis mantêm o estilo da base.

- [Plano e divisão de tarefas](Docs/Plans/PlanoTarefa4.md)
- [Diagrama de classes](Docs/Diagrams/DiagramaClasses.md)
- [Diagrama de dados](Docs/Diagrams/DiagramaDados.md)
- [Preparação da entrega](Docs/EntregaTarefa4.md)

## Autoria e colaboração

- Luana Oliveira dos Santos — RA 230559
- Roberta Santos Mascarenhas Nunes — RA 176643

A base e a identidade da ScentWare são das autoras. A implementação da Tarefa 4, sua documentação e suas verificações tiveram assistência de IA, incluindo atividades inicialmente divididas como humanas no plano, conforme a redistribuição autorizada. A revisão e a submissão acadêmica precisam refletir essa participação real.
