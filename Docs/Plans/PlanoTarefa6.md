# Plano da Tarefa 6 - ScentWare

## Objetivo

Continuar a fábrica da Tarefa 5 com persistência XML, testes JUnit 5 e pacotes Java. A implementação fica em `tarefa-06`; as cópias anteriores ficam como referência.

## Perfil de código observado

A Tarefa 4 organizou Gradle, validações e exceções. A Tarefa 5 acrescentou a linha e as partes das máquinas sem trocar as regras da fábrica. O perfil adotado aqui segue essa evolução: Java direto, nomes em português, `ArrayList`, laços e métodos com responsabilidade definida. Comentários explicam decisões, com português simples e sem acentos no código. Não é preciso criar uma arquitetura diferente para salvar a mesma fábrica.

Os testes seguem os grupos usados antes: partes, composição, falhas e reparo, inspeção, validações e linha. A diferença é que agora são testes JUnit reconhecidos pelo Gradle. Os testes novos também conferem a recuperação do estado e os erros de arquivo.

## Requisitos e decisões

| Requisito do enunciado | Decisão |
| --- | --- |
| Estoque, máquinas/componentes e demandas em XML | Salvar valores e relações; recriar as partes pelos construtores das máquinas. |
| Dados opcionais | Salvar orçamento, modelos, armazém, estratégia, cenário e próximos números de lote/produto para continuar a operação. |
| Separar responsabilidades | `PersistenciaPlanta` cuida do XML; domínio valida seus valores; gerenciador confere as relações recuperadas. |
| Erro sem perder estado válido | Carregar uma fábrica nova; o menu troca sua referência somente após o sucesso. |
| Primeira execução | Sem arquivo, usar os dados do cenário escolhido. Arquivo inválido gera aviso e mantém esses dados iniciais. |
| Atualizar arquivo | Salvar novamente pela opção 7. Escrever em temporário antes de substituir o arquivo anterior. |
| Pacotes e JUnit 5 | Separar `model`, `service`, `strategy`, `persistence` e `exception`; testes em `src/test/java`. |
| Documentação | Atualizar README com `Escopo e Limitações`, plano, diagrama editável e registro das verificações. |

## Tarefas e dependências

| Etapa | Trabalho | Depende de | Aceite |
| --- | --- | --- | --- |
| T0 | Ler versões anteriores, README e enunciado; identificar o estilo | — | Requisitos e limites registrados neste plano. |
| T1 | Organizar pacotes e imports | T0 | Gradle encontra a classe principal e todos os fontes. |
| T2 | Acrescentar leitura de estado e restauração validada ao domínio | T1 | Partes continuam próprias de cada máquina; combinações inválidas são rejeitadas. |
| T3 | Implementar XML e validação das relações | T2 | Salvar, carregar e salvar novamente preservam valores, ordem e composição. |
| T4 | Integrar opções 7/8 e carregamento inicial | T3 | Carregamento substitui estado; erro preserva a fábrica anterior. |
| T5 | Implementar JUnit de domínio e persistência | T1–T3 | Casos independentes, arquivos temporários e asserções para erros esperados. |
| T6 | Atualizar README e Mermaid | T3–T5 | Dados persistidos, limites e grupos de testes descritos conforme o código. |
| T7 | Executar testes, build, Javadoc e fluxos do menu | T4–T6 | Resultados reais registrados, incluindo verificação de subagente. |

T2 e os testes de domínio foram feitos em paralelo com persistência e integração. Uma revisão independente confere restauração e executa os testes depois da integração.

## Conferência de escopo

- [x] Leitura e comparação das versões anteriores.
- [x] Pacotes, persistência XML e integração ao menu.
- [x] Testes JUnit 5 e configuração do Gradle.
- [x] README e diagrama da persistência.
- [x] Registrar a execução em [VerificacaoTarefa6](../VerificacaoTarefa6.md): 48 testes iniciais e 66 após reorganizar a persistência, com verificação por subagente, build e Javadoc. Sete fluxos do menu foram conferidos na implementação inicial.

Publicação de release e envio ao Forms não fazem parte desta execução. A implementação local não comprova entrega na disciplina.

## Reorganização da persistência

A pedido, `PersistenciaPlanta` foi dividida em arquivos menores. A entrada usada pelo menu continua com o mesmo construtor e os métodos `salvar` e `carregar`. As regras da fábrica e o formato XML não mudaram.

| Etapa | Trabalho | Aceite |
| --- | --- | --- |
| R0 | Conferir testes existentes e guardar um XML anterior fora do projeto | Comparar o comportamento antes e depois da divisão. |
| R1 | Extrair `DocumentoXml` e `ElementosXml` para `persistence/xml` | Manter escrita temporária, leitura configurada e validações estruturais. |
| R2 | Extrair conversores para `persistence/conversores` | Uma classe para planta, matéria-prima, demandas, produtos, máquinas e estratégia. |
| R3 | Criar um arquivo de teste por classe extraída | Espelhar subpastas e manter testes de integração da entrada principal. |
| R4 | Conferir XML anterior, suíte, build e Javadoc | Registrar os resultados em `VerificacaoTarefa6.md`. |

Os testes foram preparados em paralelo com a extração e executados por subagente depois da integração. A documentação foi atualizada para mostrar quem coordena cada parte.
