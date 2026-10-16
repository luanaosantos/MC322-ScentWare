// Contrato pra auditoria de produtos e maquinas.

/**
 * Define oq o item precisa mostrar na auditoria.
 *
 * @author Luana Oliveira
 * @author Roberta Santos
 */
public interface Auditavel {
    /**
     * Monta o relatorio do item pra auditoria.
     *
     * @return relatorio de diagnostico do item
     */
    String gerarRelatorioDiagnostico();

    /**
     * Confere se o item precisa de atencao.
     *
     * @return {@code true} se o item precisar de atencao
     */
    boolean precisaManutencao();
}
