/**
 * Junta o que máquinas e produtos precisam informar na auditoria da fábrica.
 *
 * @author Luana Oliveira
 * @author Roberta Santos
 */
public interface Auditavel {
    /**
     * Monta um resumo do estado atual do item pra consulta no menu.
     *
     * @return relatório de diagnóstico do item
     */
    String gerarRelatorioDiagnostico();

    /**
     * Confere se o item precisa de atenção pelas regras do seu tipo.
     *
     * @return {@code true} se houver necessidade de manutenção ou atenção
     */
    boolean precisaManutencao();
}
