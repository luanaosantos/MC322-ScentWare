# Plano da Tarefa 4 - ScentWare

## Objetivo e situação deste plano

Evoluir o projeto existente da Tarefa 3 até uma entrega completa da Tarefa 4: Gradle e Wrapper funcionando, código documentado com Javadoc, exceções de domínio, recuperação após erros e preservação das funcionalidades anteriores. Usar este mesmo repositório como base e manter seu histórico.

Este arquivo começou como planejamento e agora acompanha a implementação. A Tarefa 4 está implementada e verificada localmente; publicação no GitHub e envio do formulário continuam pendentes. As evidências ficam em [VerificacaoTarefa4](../VerificacaoTarefa4.md), e as notas da release em [EntregaTarefa4](../EntregaTarefa4.md).

**Base examinada em 15/10/2026:** as seis páginas do [enunciado](./Tarefa4GradleJavadocETratamentoDeErros.pdf), todos os 20 arquivos Java de `src/`, `README.md`, `justificativa.txt`, `.gitignore` e os arquivos rastreados pelo Git. Não foram encontrados arquivos `AGENTS.md` aplicáveis no projeto ou nos diretórios ancestrais consultados.

**Prazo informado pelo PDF, seção 5:** 08/10/2026 às 23h59; atraso com penalidade de 25% da nota obtida; entregas não aceitas após 15/10/2026. A data desta análise é o último dia indicado para recebimento de atrasadas. O PDF não informa um horário específico para esse limite final. Confirmar os avisos da disciplina e priorizar uma entrega completa e compilável.

## Requisitos e critérios de avaliação

| Área | Exigência do enunciado | Peso | Tarefas |
| --- | --- | --- | --- |
| Gradle | Estrutura coerente, `build.gradle`, `settings.gradle`, aplicação Java, Wrapper e construção em ambiente limpo | 2,5 | T0, T1, T7 |
| Javadoc | Classes, métodos, interfaces e enums documentados no Java; tags adequadas; geração sem erros | 2,5 | T6, T7 |
| Erros | Entrada validada, exceções próprias, mensagens úteis, continuidade e estado consistente | 4,0 | T2, T3, T4, T5, T7 |
| Organização | Responsabilidades separadas, legibilidade e funcionalidades anteriores preservadas | 1,0 | Todas |
| Entrega | Release `tarefa4-RA1-RA2` no GitHub e formulário com nomes, RAs e link | Obrigatória | T8 |

Não compilar implica penalidade de 5,0 pontos. `build`, `run` e `javadoc` são verificações separadas: não presumir que um `build` bem-sucedido comprova o menu ou a documentação. Referências: seções 3.1 a 3.5, 4 e 5 do PDF.

## O que já existe e precisa ser preservado

| Parte da base | Situação observada | Consequência para a Tarefa 4 |
| --- | --- | --- |
| Construção | Fontes diretamente em `src/`; README usa `javac`; sem arquivos Gradle | Migrar para `src/main/java/` e usar Wrapper como caminho oficial |
| Git | Há `.class` em `bin/` rastreados, apesar de `.gitignore` ignorar essa pasta | Remover os compilados do versionamento durante a migração; comprovar independência deles |
| Documentação | Comentários comuns, sem blocos Javadoc nos fontes examinados | Documentar as 20 unidades existentes e os novos arquivos |
| Entrada | `Main` já trata números inválidos, intervalos do menu e doubles não finitos | Reaproveitar; completar recuperação, fim de entrada e validação de operações |
| Demandas | `Main.atualizarDemanda` atualiza uma pendente ou cria outra quando não há pendente | Preservar criação e atualização; não trocar esse fluxo por uma exceção indiscriminada |
| Estados | `Demanda` retorna booleanos ou ignora algumas transições inválidas; atualizar quantidade pode reabrir uma concluída por chamada direta | Tornar os contratos explícitos e impedir mudanças incompatíveis |
| Produção | `fabricarDemanda` chama `iniciarProducao` antes de verificar estoque e budget; falta de recursos cancela a demanda | Validar antes de iniciar e definir o cancelamento como regra explícita, sem consumo parcial |
| Compra | Quantidade inválida e budget insuficiente retornam o mesmo `false` | Distinguir as causas com validações e exceções pertinentes |
| Domínio | Construtores e operações têm validação incompleta; consumo de estoque retorna um booleano ignorado na produção | Validar também fora do menu e impedir continuação após falha de consumo |
| Simulação | Rejeição aleatória, desgaste, risco acumulado, inspeção e reparo automático | Preservar como comportamento normal; não transformar cada rejeição em exceção |

Manter os três cosméticos, óleo de amêndoas, três máquinas, estratégias por chegada/maior demanda/máximo de produtos, cenários Ideal e Apocalíptico, orçamento, estoque, armazém, lotes, auditoria e todos os submenus. Não adicionar interface gráfica, persistência, novos produtos ou outra arquitetura sem necessidade da tarefa.

## Preferências de implementação

- **Testes mínimos:** usar os comandos exigidos e o roteiro curto de T7. Não adicionar JUnit, mocks, dependências de teste ou uma suíte automatizada por padrão. Só ampliar uma verificação se aparecer uma falha concreta ou um risco ainda sem resposta.
- **Estilo da base:** ler o arquivo e as classes parecidas antes de editar. Manter nomes em português, indentação, posição das chaves, organização dos métodos e construções simples já usadas ali, para o código parecer escrito pelas mesmas pessoas. Evitar reformatação geral e abstrações desnecessárias.
- **Comentários durante a implementação:** explicar decisões e partes menos óbvias enquanto escreve, em português informal, com abreviações claras como "pra" e "qtd" quando fizer sentido. Não comentar toda atribuição nem encurtar mensagens a ponto de atrapalhar o entendimento.
- **Nomes alinhados com a pessoa:** usar PascalCase em pastas, documentos e classes criados pelo grupo, como `Docs/Plans/`, `PlanoTarefa4.md` e `DemandaInvalidaException.java`. Métodos e variáveis mantêm camelCase e o jeito da base, como `fabricarDemanda` e `quantidadeProdutos`; preservar também o padrão existente das constantes de enum. Manter os nomes e caminhos necessários às ferramentas, como `build.gradle`, `settings.gradle`, `gradlew`, `gradlew.bat`, `gradle/wrapper/` e `src/main/java/`, sem renomeações gerais dos arquivos anteriores.
- **Documentação alinhada com a pessoa:** usar Javadoc imediatamente antes da declaração de cada função/método e construtor, explicando o que faz, parâmetros, retorno, erros e efeitos importantes quando aplicáveis. A referência a Google docstrings significa esse bloco explicativo por função, não mudar o projeto para convenções de Python. Manter a formatação Java da base e escrever as explicações em português informal, com abreviações claras.
- **Padrões de projeto:** explicar nos comentários/Javadoc os padrões realmente usados, principalmente Strategy: interface comum, implementações e escolha pelo gerenciador. Preservar o desenho existente; não adicionar padrões novos só para preencher documentação.
- **Perguntas:** fazer todas aqui no chat pelo recurso de perguntas disponível, uma decisão por vez, de forma iterativa, como solicitado com `@askquestions`. Enquanto uma resposta não chega, avançar apenas nas partes que não dependem dela.

## Divisão de trabalho humano e IA

**Redistribuição autorizada no chat:** a pessoa pediu implementação de tudo em paralelo e autopilot para as tarefas humanas. Por isso, H-A/H-B abaixo registram a divisão originalmente proposta; a IA assumiu também essas implementações e verificações. Decisões de rotina foram resolvidas preservando a base, sem simular revisão humana. Publicação depende de acesso de escrita ao repositório, e o formulário depende do link real.

Esta divisão é uma proposta de colaboração e aprendizado, não uma regra de uso de IA imposta pelo PDF. O enunciado fornecido não especifica uma política de IA; as pessoas devem conferir eventuais regras adicionais da disciplina.

| Identificação | Responsabilidade principal | Entrega humana concreta |
| --- | --- | --- |
| H-A: pessoa responsável pelo domínio | Estados de demanda, decisões de negócio e documentação desse contrato | Implementar as transições de `Demanda`, escrever seus contratos e revisar os efeitos da produção |
| H-B: pessoa responsável pela interação | Menu, mensagens, recuperação e execução manual | Implementar a captura das exceções no `Main`, revisar as leituras e conduzir o roteiro mínimo de uso |
| IA | Migração Gradle, exceções acordadas, validações e integração no gerenciador, verificações mínimas e rascunhos | Produzir alterações verificáveis, explicar decisões e registrar resultados reais |
| Conjunto | Acordar contratos, integrar e verificar os requisitos | Revisão cruzada e demonstração dos cenários de erro |

H-A e H-B eram papéis sugeridos para Luana e Roberta. A redistribuição posterior para IA substitui a reserva de implementação humana, mantendo explícita a participação real.

**Responsabilidade das pessoas:** confirmar identidade e RAs, compreender e defender as decisões, validar o resultado em uso e realizar a submissão acadêmica. A IA pode preparar texto da release e checklist, mas não inventa autoria, RAs ou resultados.

**Como acompanhar:** marcar uma subtarefa somente após produzir a evidência prevista; no fechamento de cada tarefa, registrar responsável, revisão e resultado. Ter código escrito não substitui demonstrar o critério de aceite.

## Ordem de execução e pontos de integração

1. T0 estabelece o ambiente e registra a referência da Tarefa 3.
2. T1 migra a estrutura sem mudar regras de negócio.
3. T2 define o contrato das exceções e dos estados com as pessoas.
4. Após T2, H-A executa T3; IA executa T4 conforme o mesmo contrato. H-B prepara T5, integrando quando T3/T4 estiverem estáveis.
5. T6 acompanha os contratos estabilizados; T7 verifica o conjunto; T8 prepara e realiza a entrega.

Na execução autorizada em paralelo, um agente assumiu domínio/Javadoc, outro `Main`, outro estratégias/diagramas e a tarefa principal assumiu Gradle, exceções e gerenciador. Os arquivos foram divididos para evitar edições simultâneas, e os contratos foram combinados antes da integração. Houve revisão cruzada de consistência e correção dos achados antes da verificação final.

## T0 - Preparar ambiente e registrar a base

**Responsável:** conjunto. **Dependências:** nenhuma. **Referência:** preservação das funcionalidades, seção 3.4.

- [x] **T0.1 [IA]** Registrar o commit inicial da Tarefa 3 e o estado do Git; trabalhar em uma branch de Tarefa 4, preservando alterações locais e o histórico.
- [x] **T0.2 [Conjunto]** Localizar um JDK utilizável e verificar se o enunciado fixa versão. O PDF não fixa; foi adotado Java 17 com Gradle 8.14.3 e usados pacotes portáteis fora do projeto, sem alterar o ambiente permanente do computador.
- [x] **T0.3 [H-B, apoio IA]** Fazer uma execução curta da versão anterior: escolher cenário, criar uma demanda, produzir e sair. Usar a leitura do código para registrar as demais funcionalidades, sem repetir um roteiro completo antes da migração. Registrar falhas preexistentes separadamente.
- [x] **T0.4 [H-A]** Escrever a tabela das transições válidas de demanda e confirmar as políticas de quantidade zero e cancelamento por falta de recurso, usando T2 como proposta inicial.

**Aceite:** referência da base registrada, ambiente Java identificado e regras anteriores examinadas. A análise inicial foi estática; a execução posterior compilou a referência da Tarefa 3 e conferiu criação de demanda, produção e saída. Ver evidências da Tarefa 4 no registro de verificação.

## T1 - Migrar para Gradle e disponibilizar o Wrapper

**Responsável:** IA; revisão H-B. **Dependências:** T0. **Referência:** seção 3.1.

- [x] **T1.1 [IA]** Mover os 20 fontes de `src/*.java` para `src/main/java/`, sem mudar sua lógica. Manter inicialmente o pacote padrão e `Main` como entrada; reorganização adicional de pacotes não é necessária para o enunciado.
- [x] **T1.2 [IA]** Criar `settings.gradle` com o nome do projeto e `build.gradle` com plugin `application`, classe principal `Main`, configuração Java e UTF-8 para compilação/Javadoc. Declarar dependências somente se usadas; não colocar regras de negócio no build. O plugin `application` fornece a execução da aplicação ([documentação oficial](https://docs.gradle.org/current/userguide/application_plugin.html)).
- [x] **T1.3 [IA]** Configurar `run` para encaminhar a entrada do terminal com `standardInput = System.in` e verificar a interação com `Scanner`. A entrada de `JavaExec` é vazia por padrão ([referência oficial](https://docs.gradle.org/current/dsl/org.gradle.api.tasks.JavaExec.html)).
- [x] **T1.4 [IA]** Gerar o Wrapper por meio da ferramenta oficial com versão fixa e compatível. Versionar `gradlew`, `gradlew.bat`, `gradle/wrapper/gradle-wrapper.jar` e `gradle-wrapper.properties`; preservar a permissão de execução de `gradlew` no Git ([Wrapper oficial](https://docs.gradle.org/current/userguide/gradle_wrapper.html)).
- [x] **T1.5 [IA]** Atualizar `.gitignore` para `.gradle/`, `build/`, `bin/` e `.class`. Retirar os compilados já rastreados do índice; não remover o JAR necessário do Wrapper. Preservar configurações pessoais não relacionadas.
- [x] **T1.6 [IA + H-B]** Confirmar o build pelo Wrapper após mover os fontes e fazer uma abertura curta do menu para conferir a entrada do terminal. Reservar a geração final de Javadoc para T6 e o roteiro integrado para T7; não repetir verificações sem alteração relevante. Confirmar independência de Gradle global e de `bin/`.

**Aceite:** build pelo Wrapper e menu interativo funcionando na nova estrutura. A migração não muda produtos, estratégias ou cenários. Escolher a combinação Java/Gradle pela [matriz oficial de compatibilidade](https://docs.gradle.org/current/userguide/compatibility.html), sem presumir que qualquer Gradle funciona com qualquer JDK.

## T2 - Definir erros, exceções e políticas de estado

**Responsável:** H-A + H-B; IA propõe e implementa as classes acordadas. **Dependências:** T0; arquivos integrados após T1. **Referência:** seções 3.3 a 3.3.3.

- [x] **T2.1 [H-A, apoio IA]** Separar erro de entrada, violação de regra e resultado normal da simulação. Uma busca sem resultado pode retornar `null`; a operação que exige uma demanda existente deve comunicar o erro de domínio.
- [x] **T2.2 [IA]** Criar uma hierarquia pequena: `PlantaException extends RuntimeException`, `RecursoInsuficienteException` e `DemandaInvalidaException`. Usar mensagens específicas para estoque e orçamento. Só adicionar `MaquinaIndisponivelException` se houver indisponibilidade sem recuperação no fluxo real; o reparo automático atual não exige uma exceção artificial.
- [x] **T2.3 [H-A]** Definir os contratos de argumentos inválidos: tipo/modelo ausente ou incompatível, demanda não registrada, quantidade negativa, estado incompatível, valores não finitos e parâmetros numéricos fora de sua faixa. Usar exceções Java adequadas para argumentos inválidos e próprias para falhas de negócio, com tratamento claro na interação.
- [x] **T2.4 [Conjunto]** Registrar a política abaixo antes de implementar. Se o grupo alterar uma regra anterior, justificar e atualizar testes/Javadoc; não mudar silenciosamente.

| Situação | Política inicial proposta a partir da base |
| --- | --- |
| Atualizar produto sem demanda pendente | Criar uma nova demanda, como o `Main` já faz |
| Quantidade zero | Manter como demanda pendente não elegível para produção; rejeitar tentativa de iniciá-la, sem cancelar por falta de estoque |
| Quantidade negativa | Rejeitar sem modificar demanda ou registrar outra por engano |
| Produzir demanda inexistente, concluída, cancelada ou já em produção | Lançar erro de demanda; manter o estado anterior |
| Estoque ou budget insuficiente | Preservar o cancelamento da Tarefa 3 como decisão explícita: validar primeiro, cancelar sem iniciar lote e lançar erro específico; não alterar estoque, budget, armazém ou contadores |
| Estratégia sem demanda elegível | Informar ausência de trabalho e voltar ao menu; não acessar referência nula nem cancelar outra demanda |
| Máquina quebrada reparável | Preservar reparo automático e retomada da simulação |
| Rejeição na inspeção | Resultado normal: contabilizar recursos realmente usados e deixar pendentes somente os itens faltantes |

**Aceite:** contratos, mensagens e classes de exceção definidos, com explicação de quais erros mudam estado de maneira intencional. A implementação e a revisão técnica foram assumidas pela IA em autopilot; não se presume compreensão pessoal pelas autoras. O cancelamento explícito acima não autoriza outras modificações parciais.

## T3 - Implementar os contratos de demanda

**Responsável:** H-A escreve; IA revisa. **Dependências:** T2. **Arquivos:** `Demanda.java`, `StatusDemanda.java`.

- [x] **T3.1 [H-A]** Validar construção e atualização: tipo válido, quantidade conforme T2, custo finito e não negativo. Não permitir reabrir concluída/cancelada por atualização direta.
- [x] **T3.2 [H-A]** Implementar as transições: pendente com quantidade positiva para em produção; em produção para concluída ou pendente com faltantes; cancelamento somente nos estados permitidos. Representar tentativa inválida por exceção adequada, sem retorno silencioso.
- [x] **T3.3 [H-A]** Garantir coerência de quantidade, status e custo estimado após conclusão, rejeição parcial e atualização; definir custo dos itens ainda pendentes e documentar o significado.
- [x] **T3.4 [H-A + IA]** Revisar as condições das transições e entregar ao responsável pelo gerenciador as assinaturas e exceções finais. Concentrar a execução dos casos essenciais no roteiro de T7, sem criar testes separados para cada método.

**Aceite:** erros de estado não alteram a demanda; conclusão zera a quantidade pendente; reprocessamento considera apenas faltantes. Contratos humanos documentados e conferidos pelo roteiro mínimo e pela revisão de T7.

## T4 - Integrar exceções e garantir consistência da fábrica

**Responsável:** IA escreve; H-A revisa. **Dependências:** T1 e T2; integração final com T3. **Arquivos:** `GerenciadorProducao.java`, `MateriaPrima.java`, validações pertinentes em `Produto` e `Maquina`.

- [x] **T4.1 [IA]** Validar invariantes na camada de domínio, não apenas no menu: referências necessárias, quantidade/custo/budget finitos e nas faixas corretas, probabilidades e qualidade entre 0 e 1, saúde entre 0 e 100, desgaste não negativo e consumo por unidade positivo. Validar resultados de operações numéricas para impedir overflow para infinito.
- [x] **T4.2 [IA]** Refatorar compra para distinguir quantidade inválida e orçamento insuficiente. Validar custo calculado e novo estoque antes de debitar/adicionar; uma compra rejeitada preserva ambos.
- [x] **T4.3 [IA]** Na produção, validar demanda registrada, tipo do modelo, quantidade, estado, configuração da linha, consumo total e custo total antes de `iniciarProducao`, consumo, débito ou incremento de lote. Aplicar separadamente a política de cancelamento de T2 quando faltar recurso.
- [x] **T4.4 [IA]** Adaptar `MateriaPrima.consumir` e seus chamadores para impedir consumo inválido e falha ignorada. Não permitir estoque negativo nem avançar a produção quando o consumo foi recusado.
- [x] **T4.5 [IA + H-A]** Verificar as operações após o início do lote. Antecipar todas as falhas recuperáveis previsíveis antes das mutações e eliminar lançamentos esperados tardios. Se restar uma operação que possa lançar após modificar estado, implementar restauração localizada ou calcular o resultado antes de efetivá-lo. Não basta mover um único `if` para declarar atomicidade.
- [x] **T4.6 [IA]** Preservar débito de operação das máquinas, criação de produtos independentes, lotes, risco, desgaste, inspeção, reparos e demanda parcial. Não tratar rejeição aleatória como desfazimento de um lote válido.
- [x] **T4.7 [IA]** Adaptar seleção automática e fabricação específica aos contratos. Preservar os critérios das três estratégias, incluindo o filtro de viabilidade financeira somente onde ele já existe. Atualizar todos os chamadores quando uma assinatura mudar.

**Aceite:** erros previsíveis não produzem consumo ou débitos parciais. Verificar orçamento, estoque, status/quantidade/custo da demanda, armazém, próximo lote, contador de produtos e estado das máquinas quando aplicável. Não capturar uma falha inesperada e voltar ao menu com o sistema parcialmente modificado; corrigir a causa ou restaurar o estado antes de permitir continuidade.

## T5 - Recuperar a interação após erros

**Responsável:** H-B escreve; IA revisa. **Dependências:** contratos de T2; integração com T3/T4. **Arquivo:** `Main.java`.

- [x] **T5.1 [H-B]** Reutilizar os leitores existentes de inteiros, intervalos e doubles finitos. Conferir no código a rejeição de vazio, negativos indevidos, tipos errados, `NaN`, infinito e inteiro fora da faixa. No roteiro mínimo, executar apenas exemplos representativos de entrada inválida. Definir comportamento limpo para fim da entrada, evitando repetição infinita.
- [x] **T5.2 [H-B]** Capturar `PlantaException` no limite da operação de menu, apresentar a causa e orientar como corrigir quando possível. Tratar exceções de argumento somente nos pontos em que representam entrada do usuário. Não envolver todo o programa em `catch (Exception)`.
- [x] **T5.3 [H-B]** Ajustar compra, produção específica/automática e criação/atualização aos novos contratos. Não interpretar uma exceção de atualização como autorização para criar uma demanda nova.
- [x] **T5.4 [H-B]** Garantir que um erro recuperável permite escolher outra operação sem reiniciar e sem imprimir sucesso indevido. Informar cancelamento quando essa for a política aplicada; não afirmar que nada mudou se o status foi cancelado.
- [x] **T5.5 [H-B + H-A]** Incluir no roteiro único de T7 a sequência: operação inválida, consulta do estado, correção e nova operação válida. H-A confere o domínio; H-B avalia mensagens e navegação. Não duplicar essa execução aqui.

**Aceite:** menu e submenus continuam disponíveis, erros recuperáveis têm mensagens específicas e não encerram a aplicação. Nenhum `catch` vazio; falhas de programação não são ocultadas como entradas inválidas.

## T6 - Documentar todo o código com Javadoc

**Responsáveis:** H-A, H-B e IA conforme tabela. **Dependências:** T1; documentar comportamento final de T2 a T5. **Referência:** seção 3.2.

| Responsável por escrever | Arquivos |
| --- | --- |
| H-A | `Demanda`, `StatusDemanda`, `EstrategiaProducao` e as três estratégias |
| H-B | `Main`, `Cenario`, `TipoProduto`, `Auditavel` |
| IA, com revisão H-A | `GerenciadorProducao`, `MateriaPrima`, `Produto` e três produtos concretos, `Maquina` e três máquinas concretas, novas exceções |

- [x] **T6.1 [Cada responsável]** Documentar responsabilidade de classes/interfaces/enums e constantes de enum. Colocar Javadoc imediatamente antes de cada método e construtor, incluindo os auxiliares privados, explicando o que faz em português informal e com abreviações claras. Atributos privados não exigem Javadoc individual se seu significado estiver claro no contrato.
- [x] **T6.2 [Cada responsável]** Usar `@param`, `@return` e `@throws` onde aplicáveis; incluir `@author` com identidade conferida pelas pessoas. Não criar `@return` em `void` ou declarar exceções que nunca são lançadas. Nas sobrescritas, herdar contrato somente quando adequado e explicar regras específicas.
- [x] **T6.3 [H-A]** Explicar transições, zero unidades, cancelamento, faltantes, custos e o significado de ausência de demanda nas estratégias. Documentar como Strategy funciona entre `EstrategiaProducao`, as três implementações e `GerenciadorProducao`, incluindo a troca em execução. Explicitar que algumas estratégias não filtram orçamento, sem introduzir novos padrões.
- [x] **T6.4 [IA + H-B]** Documentar probabilidade de falha, diferença entre rejeição normal e erro, orçamento, lotes, reparo automático e efeitos observáveis das operações.
- [x] **T6.5 [IA]** Executar `javadoc`, corrigir erros e avisos de documentação do projeto, sem desativar validações para esconder problemas. Conferir o HTML gerado em `build/docs/javadoc/index.html`.
- [x] **T6.6 [IA em autopilot]** Conferir o HTML gerado das 23 classes e comparar os contratos com os fontes e exemplos executados. A conferência foi estrutural; não foi feita revisão visual em navegador nem presumida revisão pessoal das autoras.

**Aceite:** todos os 20 fontes originais e os novos fontes documentados; tags coerentes; geração sem erros e cobertura revisada. Texto externo não substitui Javadoc nos `.java`.

## T7 - Fazer somente as verificações essenciais

**Responsável:** IA executa os comandos e revisa o domínio; H-A/H-B conduzem o uso. **Dependências:** T1 e implementação integrada de T3 a T6.

Usar o mínimo de testes solicitado: comandos obrigatórios, um roteiro curto pelo menu e revisão dos trechos alterados. Não criar `src/test/java/`, adicionar JUnit, mocks ou outra estrutura de testes por padrão. Não repetir verificações que já passaram na versão atual; ampliar somente se surgir uma falha concreta.

- [x] **T7.1 [IA]** Fazer `clean build` pelo Wrapper em uma cópia dos arquivos versionados, sem compilados antigos. Conferir o resultado de `javadoc` da mesma versão final, gerado em T6; executar novamente somente se os fontes tiverem mudado.
- [x] **T7.2 [H-B + H-A, apoio IA]** Executar o roteiro compacto abaixo durante `run`, aproveitando a mesma sessão quando possível. Registrar sucesso ou falha de cada grupo em uma linha.
- [x] **T7.3 [IA + H-A]** Revisar a ordem das validações e das alterações de estoque, budget, demanda e lote. Comparar antes/depois dos erros de recurso com uma conferência temporária, sem suíte versionada. Conferir as transições e estratégias no código; acrescentar só a checagem de arredondamento motivada por um achado concreto da revisão.
- [x] **T7.4 [Conjunto]** Registrar o ambiente efetivamente usado e os três resultados: `build`, `run` e `javadoc`. Validar no Windows disponível; não abrir uma matriz de sistemas operacionais nem exigir ferramentas adicionais só para testes.

| Grupo mínimo | Ação | Resultado esperado |
| --- | --- | --- |
| V01 - Uso normal | Criar/atualizar demanda, produzir, consultar estoque/armazém, trocar estratégia, auditar e sair; abrir o outro cenário para conferir sua inicialização | Funcionalidades anteriores acessíveis e dados coerentes; não exigir resultado aleatório exato da produção |
| V02 - Recuperação | Informar texto no menu, opção inexistente, compra com quantidade negativa e tentar produzir sem demanda; depois fazer uma operação válida | Mensagens específicas, sem encerramento ou sucesso falso; sessão continua |
| V03 - Recursos | Provocar falta de matéria-prima e falta de orçamento com os dados do cenário; consultar estado antes/depois | Cancelamento conforme T2, sem consumo ou débito parcial; compra recusada não muda estoque/budget |

**Comandos de aceite no Windows, a partir da raiz do projeto:**

```powershell
.\gradlew.bat clean build
.\gradlew.bat run
.\gradlew.bat javadoc
```

**Equivalentes em Linux/macOS:**

```bash
./gradlew clean build
./gradlew run
./gradlew javadoc
```

**Aceite:** três comandos obrigatórios bem-sucedidos, três grupos do roteiro conferidos e revisão dos pontos alterados concluída. Registrar qualquer limitação observada. Sem dependência de `.class` antigos nem captura genérica para esconder falhas.

## T8 - Atualizar documentação e realizar a entrega

**Responsável:** conjunto; submissão pelas pessoas. **Dependências:** T7 aprovado. **Referência:** seção 5.

- [x] **T8.1 [IA prepara, H-B revisa]** Atualizar `README.md` para Tarefa 4: pré-requisito JDK, versão do Wrapper, estrutura, comandos de build/run/javadoc em Windows e Unix, localização do HTML e instruções de uso/recuperação. Informar necessidade de rede no primeiro download do Wrapper e dependências.
- [x] **T8.2 [H-A + H-B escrevem, IA revisa]** Acrescentar a evolução da Tarefa 4 ao `justificativa.txt`, preservando o relato anterior. Explicar Gradle, exceções, consistência, políticas mantidas/alteradas e divisão real do trabalho. Não atribuir trabalho que não foi realizado.
- [x] **T8.3 [IA prepara, pessoas conferem]** Revisar o diff e os arquivos versionados: fontes, Gradle, scripts e JAR do Wrapper presentes; compilados/caches fora da entrega. Preparar notas da release com mudanças, comandos e resultados efetivamente obtidos.
- [ ] **T8.4 [H-A + H-B]** Publicar a release GitHub `tarefa4-230559-176643` no commit validado. Nomes e RAs já foram conferidos pelo histórico; a conta conectada tem apenas leitura no repositório, então a publicação está pendente de acesso de escrita.
- [ ] **T8.5 [Pessoa designada]** Preencher o Google Forms com nomes, RAs e link da release; conferir acessibilidade do link e guardar a confirmação. O PDF fornecido não contém a URL do formulário.

**Aceite:** release correta publicada e formulário submetido, com comprovantes conferidos pelas pessoas. Um commit local ou uma branch não substituem esses dois passos de entrega.

## Lista final de conclusão

- [x] Gradle e Wrapper completos, versionados e utilizáveis em ambiente limpo.
- [x] `build`, `run` interativo e `javadoc` executados com sucesso; ambientes testados identificados.
- [x] Código existente e novo com Javadoc coerente e autoria conferida.
- [x] Exceções próprias usadas em falhas de domínio; capturadas no ponto adequado.
- [x] Entrada inválida e erros recuperáveis permitem continuar a sessão.
- [x] Estoque, orçamento, demandas, lotes, máquinas e armazém ficam consistentes após erros.
- [x] Rejeições normais da simulação e funcionalidades da Tarefa 3 preservadas.
- [x] Comandos obrigatórios e roteiro mínimo registrados; nenhum resultado presumido ou suíte extra sem necessidade.
- [x] README e justificativa atualizados, com decisões e participação real explicadas pra revisão.
- [ ] Release `tarefa4-230559-176643` publicada e Google Forms enviado.

Registro ao encerrar cada tarefa: `Tarefa | implementado por | revisado por | evidência/comando | resultado | pendências`. Manter as caixas abertas enquanto a evidência não existir.

## Registro da execução

| Tarefa | Execução e revisão | Evidência | Estado |
| --- | --- | --- | --- |
| T0 | IA, preservando a referência e o histórico | Base `e84700c`, branch `Tarefa4`, compilação/execução anterior | Concluída |
| T1 | Tarefa principal + integração | Wrapper, checksum, `clean build`, cópia limpa | Concluída |
| T2 | IA em autopilot, preservando regras anteriores | Hierarquia de 3 exceções e contratos documentados | Concluída |
| T3 | Agente de domínio + revisão cruzada | Validações/transições e conferência de consistência | Concluída |
| T4 | Tarefa principal + agente revisor | Preflight, proteção de estimativas e correção de arredondamento | Concluída |
| T5 | Agente de menu + verificação integrada | Uso normal, entradas inválidas, retomada e EOF | Concluída |
| T6 | Agentes + tarefa principal | Javadoc gerado sem erros/avisos; 23 fontes documentados | Concluída |
| T7 | Tarefa principal + agente de diagramas | 3 grupos mínimos, cópia limpa e 3 blocos Mermaid válidos | Concluída |
| T8.1 a T8.3 | IA em autopilot | README, justificativa, notas e dados da entrega | Concluídas |
| T8.4 e T8.5 | Dependem de serviço/dados externos | GitHub sem escrita; Forms sem link informado | Pendentes |
