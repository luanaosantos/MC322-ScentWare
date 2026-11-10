// Valvula q controla a passagem da mistura.
package br.unicamp.ic.mc322.model;


/**
 * Guarda a pressao e a condicao da valvula do homogeneizador.
 *
 * @author Luana Oliveira
 * @author Roberta Santos
 */
public class ValvulaHomogeneizadora {
    private double pressaoAtual;
    private boolean funcionando;

    /** Cria a valvula sem pressao e funcionando. */
    ValvulaHomogeneizadora() {
        pressaoAtual = 0.0;
        funcionando = true;
    }

    /**
     * Abre a passagem durante a mistura.
     *
     * @return true se a valvula funcionou
     */
    boolean abrir() {
        pressaoAtual = funcionando ? 1.0 : 0.0;
        return funcionando;
    }

    /** Fecha a passagem depois da mistura. */
    void fechar() {
        pressaoAtual = 0.0;
    }

    /** Registra falha na valvula. */
    void registrarFalha() {
        funcionando = false;
        pressaoAtual = 0.0;
    }

    /**
     * Confere a condicao da valvula.
     *
     * @return true se precisa de reparo
     */
    boolean precisaReparo() {
        return !funcionando;
    }

    /** Repara a valvula. */
    void reparar() {
        funcionando = true;
        pressaoAtual = 0.0;
    }

    /**
     * Mostra a condicao e a pressao.
     *
     * @return texto pra auditoria
     */
    String diagnostico() {
        return "Válvula | " + (funcionando ? "Funcionando" : "Com falha")
                + " | Pressão: " + pressaoAtual;
    }

    /**
     * Retorna a pressao da valvula.
     * @return pressao atual, zero ou um
     */
    public double getPressaoAtual() {
        return pressaoAtual;
    }

    /**
     * Consulta a condicao da valvula.
     * @return true se esta funcionando
     */
    public boolean isFuncionando() {
        return funcionando;
    }

    /**
     * Recupera a pressao e a condicao da valvula.
     * @param pressaoAtual pressao salva, zero ou um
     * @param funcionando condicao salva
     * @throws IllegalArgumentException se a pressao nao corresponder a um estado da valvula
     */
    public void restaurarEstado(double pressaoAtual, boolean funcionando) {
        if ((pressaoAtual != 0.0 && pressaoAtual != 1.0) || (!funcionando && pressaoAtual != 0.0)) {
            throw new IllegalArgumentException("A pressão da válvula deve ser 0 ou 1 e ficar zerada quando há falha.");
        }
        this.pressaoAtual = pressaoAtual;
        this.funcionando = funcionando;
    }

}
