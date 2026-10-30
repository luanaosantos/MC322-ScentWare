// Valvula q controla a passagem da mistura.

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
}
