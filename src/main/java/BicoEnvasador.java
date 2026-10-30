// Bico usado no envase dos produtos.

/**
 * Abre e fecha o bico e guarda sua condicao.
 *
 * @author Luana Oliveira
 * @author Roberta Santos
 */
public class BicoEnvasador {
    private boolean aberto;
    private boolean funcionando;

    /** Cria o bico fechado e funcionando. */
    BicoEnvasador() {
        aberto = false;
        funcionando = true;
    }

    /**
     * Abre o bico pra envasar.
     *
     * @return true se o bico conseguiu abrir
     */
    boolean abrirBicoEnvasador() {
        aberto = funcionando;
        return aberto;
    }

    /** Fecha o bico depois do envase. */
    void fechar() {
        aberto = false;
    }

    /** Registra falha no bico. */
    void registrarFalha() {
        funcionando = false;
        aberto = false;
    }

    /**
     * Confere a condicao do bico.
     *
     * @return true se precisa de reparo
     */
    boolean precisaReparo() {
        return !funcionando;
    }

    /** Repara e fecha o bico. */
    void reparar() {
        funcionando = true;
        aberto = false;
    }

    /**
     * Mostra o estado do bico.
     *
     * @return texto pra auditoria
     */
    String diagnostico() {
        return "Bico envasador | " + (funcionando ? "Funcionando" : "Com falha")
                + " | " + (aberto ? "Aberto" : "Fechado");
    }
}
