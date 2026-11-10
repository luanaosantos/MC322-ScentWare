// Atuador q direciona o produto depois da inspecao.
package br.unicamp.ic.mc322.model;


/**
 * Guarda a condicao do mecanismo q separa produtos rejeitados.
 *
 * @author Luana Oliveira
 * @author Roberta Santos
 */
public class AtuadorDescarte {
    private boolean funcionando;
    private int descartados;

    /** Cria o atuador funcionando e sem descartes. */
    AtuadorDescarte() {
        funcionando = true;
        descartados = 0;
    }

    /**
     * Separa o produto rejeitado quando o atuador esta funcionando.
     *
     * @param rejeitado resultado da inspecao
     * @return true se o atuador conseguiu fazer a separacao
     */
    boolean encaminhar(boolean rejeitado) {
        if (!funcionando) {
            return false;
        }
        if (rejeitado) {
            descartados++;
        }
        return true;
    }

    /** Registra uma falha no atuador. */
    void registrarFalha() {
        funcionando = false;
    }

    /**
     * Confere se o atuador precisa de reparo.
     *
     * @return true se houve falha
     */
    boolean precisaReparo() {
        return !funcionando;
    }

    /** Repara o atuador sem apagar o total de descartes. */
    void reparar() {
        funcionando = true;
    }

    /**
     * Mostra o estado e a qtd de descartes.
     *
     * @return texto pra auditoria
     */
    String diagnostico() {
        return "Atuador de descarte | " + (funcionando ? "Funcionando" : "Com falha")
                + " | Descartados: " + descartados;
    }

    /**
     * Consulta a condicao do atuador.
     * @return true se esta funcionando
     */
    public boolean isFuncionando() {
        return funcionando;
    }

    /**
     * Retorna o total de produtos descartados.
     * @return descartes acumulados
     */
    public int getDescartados() {
        return descartados;
    }

    /**
     * Recupera a condicao e o contador do atuador.
     * @param funcionando condicao salva
     * @param descartados total de descartes salvo
     * @throws IllegalArgumentException se o total de descartes for negativo
     */
    public void restaurarEstado(boolean funcionando, int descartados) {
        if (descartados < 0) {
            throw new IllegalArgumentException("O total de descartes não pode ser negativo.");
        }
        this.funcionando = funcionando;
        this.descartados = descartados;
    }

}
