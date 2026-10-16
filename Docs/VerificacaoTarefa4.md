# Verificação da Tarefa 4

Registro das verificações realizadas em 15/10/2026, com o ambiente e os resultados descritos abaixo.

## Ambiente e referência

- Windows, com Eclipse Temurin JDK 17.0.20.1 em instalação portátil temporária.
- Gradle 8.14.3, executado pelo Wrapper do projeto. O primeiro uso baixou a distribuição e conferiu seu SHA-256.
- Referência da Tarefa 3: commit `e84700cb3d2b214d02c77c712d8b165430e27237`.
- Branch de trabalho local: `Tarefa4`.
- JDK/Gradle usados na preparação ficaram fora do repositório; não foi alterado o `JAVA_HOME` permanente do computador. Pra executar depois, configure um JDK 17 no `JAVA_HOME` ou no `PATH`, conforme o README.
- Não foram adicionados JUnit, mocks, dependências de teste ou uma suíte permanente. A tarefa `test` do Gradle aparece como `NO-SOURCE`; isso não significa que existam testes automatizados versionados.

## Resultados executados

| Verificação | Resultado observado |
| --- | --- |
| Referência anterior | Fontes do commit da Tarefa 3 compilados em pasta temporária; criação de demanda, produção e saída executadas sem erro |
| `gradlew.bat clean build javadoc --console=plain` | `BUILD SUCCESSFUL`; compilação, distribuição e documentação concluídas, sem erros ou avisos de Javadoc |
| V01: uso normal via `gradlew.bat run` | Criação e atualização de demandas, produção com lote 1, consultas de estoque/armazém, compra, troca entre estratégias, auditorias e saída disponíveis |
| V02: recuperação via `gradlew.bat run` | Texto no cenário, opção inexistente, compra negativa, demanda ausente e `NaN` tratados; compra válida depois dos erros funcionou; fim da entrada encerrou normalmente |
| Outro cenário | Apocalíptico inicializou com budget 100 e permitiu saída normal |
| V03: recursos e consistência | Falta de estoque e budget cancelou a demanda sem consumir estoque, debitar orçamento, preencher armazém ou incrementar produtos; compra recusada preservou os saldos |
| Retomada depois dos erros | Produção válida seguinte iniciou no lote 1 e consumiu apenas seu próprio material/budget; demanda de zero unidades foi recusada sem cancelar |
| Arredondamento | Custos 0.3, 0.2 e 0.1 com budget 0.6 não deixaram saldo negativo; a estratégia financeira continuou utilizável |
| Cópia limpa | Arquivos de fonte/configuração copiados sem `bin/`, `build/` e `.gradle/`; Wrapper executou `clean build javadoc` com sucesso e gerou `build/docs/javadoc/index.html` |
| Mermaid | `mermaid.parse` aceitou 1 bloco do diagrama de classes e 2 blocos do diagrama de dados/fluxo |

V03 usou um programa de conferência temporário fora do projeto, sem dependências extras. A cópia limpa reaproveitou a distribuição do Wrapper já baixada no cache externo; não dependeu de classes ou cache de compilação do projeto. O download inicial do Wrapper também foi validado separadamente.

Os resultados aleatórios de aprovação/rejeição não foram fixados como expectativa do teste. O roteiro confere o funcionamento e a consistência, preservando os sorteios da simulação.

## Revisão dos pontos alterados

- As demandas validam referências, quantidades, custos e transições; concluídas/canceladas não são reabertas por atualização.
- Produção valida demanda, modelo, linha e recursos antes de iniciar. Os objetos do lote são preparados antes das mutações; o consumo do material é feito pelo total do lote, evitando descontos unitários com resíduos numéricos.
- Falta de recurso mantém o cancelamento intencional da versão anterior. A mensagem avisa essa mudança de estado; não afirma que a demanda ficou igual.
- As máquinas são configuradas antes de cadastrar demandas, preservando as estimativas usadas pela estratégia financeira.
- Não há `catch` vazio ou captura genérica de `Exception`. Erros esperados chegam ao menu; rejeição de produto continua sendo resultado normal.
- As máquinas validam o produto antes de ligar e usam `finally` pra desligar.
- As listas de demandas/produtos devolvidas pelos getters são cópias da estrutura, evitando remoção acidental da lista interna por fora.
- As classes antigas foram movidas pra `src/main/java/`; os `.class` antigos foram retirados do índice do Git. O JAR do Wrapper permanece versionado e `gradlew` tem modo executável `100755`, com LF preservado por `.gitattributes`.

## Limites e entrega externa

O sistema operacional executado foi Windows; não foi afirmada validação de execução em Linux/macOS. Os scripts Unix do Wrapper estão presentes e com permissão de execução no Git.

Os diagramas foram validados pelo parser Mermaid, sem renderização visual adicional. O HTML Javadoc foi gerado e sua estrutura foi conferida; não houve teste visual de navegador.

RAs encontrados nas tags e autores do histórico: Luana Oliveira dos Santos, **230559**, e Roberta Santos Mascarenhas Nunes, **176643**. Nome esperado da release: `tarefa4-230559-176643`.

A conta GitHub conectada, `mariaxmxavier`, possui `pull: true` e `push: false` em `luanaosantos/MC322-ScentWare`. A release não foi publicada. O link do Google Forms não consta no PDF nem no histórico consultado e foi solicitado no chat; não houve submissão de formulário.

As notas e os dados necessários estão em [EntregaTarefa4](EntregaTarefa4.md).
