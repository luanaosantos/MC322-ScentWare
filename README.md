# ScentWare

Sistema desenvolvido para a disciplina MC322 - Programação Orientada a Objetos, da Universidade Estadual de Campinas (UNICAMP). Esta versão corresponde à Tarefa 5.

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

## Como usar

1. Escolha o cenário de operação.
2. No menu de demandas, informe a quantidade do produto que deseja fabricar.
3. Consulte o estoque e o orçamento; compre matéria-prima se precisar.
4. No menu de fabricação, produza um item específico ou use a estratégia escolhida.
5. Consulte as demandas, o armazém e a auditoria para acompanhar os resultados.

## Diagrama de classes

- [UML em PNG](Docs/Diagrams/DiagramaClassesTarefa5.png)
- [Arquivo editável em Mermaid](Docs/Diagrams/DiagramaClassesTarefa5.mmd)

## Autoria

- Luana Oliveira dos Santos — RA 230559
- Roberta Santos Mascarenhas Nunes — RA 176643