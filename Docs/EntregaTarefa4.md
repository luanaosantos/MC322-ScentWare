# Entrega da Tarefa 4

## Dados da entrega

| Campo | Valor |
| --- | --- |
| Projeto | ScentWare — MC322 / UNICAMP |
| Autora | Luana Oliveira dos Santos — RA 230559 |
| Autora | Roberta Santos Mascarenhas Nunes — RA 176643 |
| Repositório | [MC322-ScentWare](https://github.com/luanaosantos/MC322-ScentWare) |
| Nome da release e tag | `tarefa4-230559-176643` |
| Estado da release | Implementada e verificada localmente; publicação pendente |
| Formulário | Link ainda não recebido; envio pendente |

A implementação, documentação e verificação da Tarefa 4 tiveram assistência de IA, incluindo as atividades humanas do plano redistribuídas por autorização do grupo. A autoria e o tema da base foram preservados. A participação registrada deve corresponder ao trabalho realmente feito.

## Texto preparado pra release

**Título:** Tarefa 4 — Gradle, Javadoc e Tratamento de Erros

A ScentWare passou a usar Gradle 8.14.3 com Wrapper e Java 17. Os fontes estão em `src/main/java/`, com Javadoc nas classes e antes dos métodos e construtores. O Wrapper verifica o checksum da distribuição e não exige instalação global do Gradle. Não foram adicionadas dependências externas nem framework de testes.

As exceções `PlantaException`, `DemandaInvalidaException` e `RecursoInsuficienteException` distinguem falhas de negócio. O domínio valida entradas e transições, enquanto o menu comunica os erros recuperáveis e continua a interação. Falta de estoque ou budget cancela a demanda sem consumir material, debitar orçamento ou iniciar lote. Rejeições da inspeção continuam como resultado da simulação e deixam somente os faltantes pendentes.

Foram mantidos os três cosméticos, óleo de amêndoas, três máquinas, estratégias, cenários Ideal e Apocalíptico, orçamento, estoque, armazém, lotes e auditoria. A documentação inclui diagramas Mermaid de classes e dados em memória, plano, instruções de execução e registro das verificações.

**Autoras:** Luana Oliveira dos Santos (230559) e Roberta Santos Mascarenhas Nunes (176643). Implementação da Tarefa 4 assistida por IA, conforme descrito na documentação.

Os comandos de aceite são:

```powershell
.\gradlew.bat clean build
.\gradlew.bat run
.\gradlew.bat javadoc
```

No Linux/macOS:

```bash
./gradlew clean build
./gradlew run
./gradlew javadoc
```

Os resultados reais e eventuais limitações devem ser conferidos em [VerificacaoTarefa4](VerificacaoTarefa4.md) antes da publicação. A documentação gerada fica em `build/docs/javadoc/index.html`.

## Checklist final

- [x] Dados das autoras e nome da release preparados.
- [x] Texto da release preparado com mudanças e comandos.
- [x] README atualizado e evolução da Tarefa 4 acrescentada à justificativa, preservando o relato anterior.
- [x] Conferir os resultados de `clean build`, `run` e `javadoc` em [VerificacaoTarefa4](VerificacaoTarefa4.md).
- [x] Conferir os três grupos mínimos do roteiro: uso normal, recuperação de erros e falta de recursos.
- [x] Conferir o diff e os arquivos preparados para o commit: fontes, scripts e JAR do Wrapper presentes; compilados e caches fora do versionamento.
- [ ] Confirmar que a tag `tarefa4-230559-176643` aponta para o commit verificado.
- [ ] Publicar a release no GitHub e registrar seu link real.
- [ ] Obter o link do Google Forms da disciplina.
- [ ] Enviar nomes, RAs e link da release no formulário e guardar a confirmação.

O texto e os dados estão preparados. A publicação da release e a submissão no formulário só devem ser marcadas depois que forem realizadas.

## Referências do projeto

- [Plano e enunciado](Plans/PlanoTarefa4.md)
- [Diagrama de classes](Diagrams/DiagramaClasses.md)
- [Diagrama de dados](Diagrams/DiagramaDados.md)
- [README](../README.md)
- [Justificativa](../justificativa.txt)

A conta GitHub conectada possui apenas leitura no repositório (`push: false`); a publicação depende de acesso de escrita. Os RAs foram obtidos das tags e autores do histórico.
