# Verificação da Tarefa 6

## Ambiente e comandos

Verificação feita no Windows com Eclipse Temurin JDK 17.0.21 e Gradle Wrapper 8.14.3. O ambiente não tinha JDK no `PATH`; foi usado um JDK temporário, sem alterar os arquivos do Wrapper nem a configuração permanente do computador.

Na pasta `tarefa-06`, foram executados:

```powershell
.\gradlew.bat test build javadoc --console=plain
.\gradlew.bat test --rerun-tasks --console=plain
"1`n0" | .\gradlew.bat run --console=plain
```

O primeiro comando terminou com `BUILD SUCCESSFUL`: compilação, testes, JAR, distribuições e Javadoc gerados. O segundo foi executado por um subagente, recompilando e repetindo a suíte de forma independente. Também terminou com `BUILD SUCCESSFUL`. O terceiro abriu o cenário Ideal pelo Gradle e encerrou pelo menu com código de saída zero.

## Resultados JUnit da implementação inicial

| Classe | Testes | Falhas | Erros | Ignorados |
| --- | ---: | ---: | ---: | ---: |
| `ComponentesTest` | 5 | 0 | 0 | 0 |
| `DemandaTest` | 6 | 0 | 0 | 0 |
| `LinhaDeProducaoTest` | 6 | 0 | 0 | 0 |
| `EstrategiaTest` | 4 | 0 | 0 | 0 |
| `GerenciadorProducaoTest` | 7 | 0 | 0 | 0 |
| `PersistenciaPlantaTest` | 20 | 0 | 0 | 0 |
| Total | 48 | 0 | 0 | 0 |

A contagem foi conferida nos arquivos XML gerados pelo Gradle. Os vinte casos de persistência incluem dez entradas parametrizadas com dados inválidos. A suíte também recupera os quatro estados de demanda, componentes com falhas e uma máquina ligada; depois confere a continuidade dos contadores ao fabricar e salvar outra vez.

- [Relatório HTML do JUnit](../build/reports/tests/test/index.html)
- [Resultados XML](../build/test-results/test/)
- [Javadoc](../build/docs/javadoc/index.html)

Esses arquivos são gerados pelo Gradle e ficam em `build/`, pasta ignorada pelo Git. Podem ser recriados com os comandos acima.

## Fluxos do menu

Foram executados sete fluxos por processos Java separados, com entradas prontas e diretório temporário. Cada processo usou as classes compiladas pelo Gradle. Os testes não escreveram em `dados/planta.xml` do projeto.

| Fluxo | Resultado observado |
| --- | --- |
| Carregar sem arquivo | Aviso de persistência; consulta do estoque inicial continua disponível. |
| Salvar e sair | XML criado com demanda de três cremes de mãos; encerrar mantém o arquivo. |
| Reabrir em outra execução | Mesmo escolhendo Apocalíptico, recupera cenário Ideal e a demanda salva. |
| Alterar e carregar pelo menu | Quantidade nove em memória é substituída pela quantidade três do arquivo. |
| Salvar novamente | Compra atualiza o estoque salvo de 1000 para 1010 mL. |
| XML corrompido | Inicialização usa o cenário escolhido; falha ao carregar depois de uma compra preserva 1010 mL e budget de R$ 99. O XML corrompido não é sobrescrito ao sair. |
| Fim da entrada | Programa encerra sem travar nem mostrar uma pilha de exceção. |

As saídas completas ficaram em [build/verificacao/Menu.txt](../build/verificacao/Menu.txt). Essa conferência complementa os testes JUnit; não depende de uma execução manual anterior da fábrica.

## Revisão dos arquivos

A revisão independente conferiu domínio, XML, gerenciador e menu. Um ajuste encontrado foi rejeitar conteúdo CDATA inesperado; o caso entrou nos testes parametrizados e passou nas duas execuções.

O diagrama [PersistenciaTarefa6.mmd](Diagrams/PersistenciaTarefa6.mmd) foi renderizado com `pretty-mermaid`, tema `github-light`, em [SVG](Diagrams/PersistenciaTarefa6.svg). A prévia em PNG foi inspecionada: textos e setas legíveis, sem cortes. Comentários, mensagens novas, testes e documentação passaram pela revisão `humanizer`, mantendo o vocabulário da fábrica e o estilo das tarefas anteriores.

As verificações confirmam o funcionamento nesta máquina Windows. Linux e macOS não foram executados. A release e o formulário da disciplina ainda dependem de entrega pela dupla.

## Verificação após dividir a persistência

`PersistenciaPlanta` passou a coordenar as chamadas de conversão e arquivo. Continua sendo a entrada chamada pelo menu, com o mesmo construtor e os métodos `salvar` e `carregar`. A conversão foi distribuída em `persistence/conversores`; as operações de documento e validação estrutural ficaram em `persistence/xml`.

Os 48 testes anteriores continuam aprovados. Cada uma das oito classes extraídas tem um arquivo de teste correspondente, acrescentando 18 casos:

| Classe nova de teste | Casos |
| --- | ---: |
| `MateriaPrimaXmlTest` | 2 |
| `DemandaXmlTest` | 2 |
| `ProdutoXmlTest` | 2 |
| `MaquinaXmlTest` | 2 |
| `EstrategiaXmlTest` | 2 |
| `PlantaXmlTest` | 3 |
| `DocumentoXmlTest` | 2 |
| `ElementosXmlTest` | 3 |

Execução independente por subagente: `gradlew test --rerun-tasks --console=plain`, com `BUILD SUCCESSFUL`. Conferência final: `gradlew test build javadoc --console=plain`, também com `BUILD SUCCESSFUL`, sem avisos de Javadoc. Os resultados XML confirmam 66 testes em 14 classes, zero falhas, erros ou ignorados.

Antes da extração, foi guardado um XML produzido pelo menu com uma demanda de três cremes de mãos. Depois, outro processo Java carregou esse arquivo, recuperou cenário e demanda e salvou novamente. O XML resultante ficou idêntico ao anterior, byte a byte. A saída está em [CompatibilidadePersistencia.txt](../build/verificacao/CompatibilidadePersistencia.txt).

README, plano e Mermaid foram atualizados para descrever os arquivos extraídos. Os textos novos passaram por `humanizer`, mantendo os comentários curtos em português.
