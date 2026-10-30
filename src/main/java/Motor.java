// Motor usado pelas maquinas da fabrica.

/**
 * Controla se o motor esta ligado e acompanha seu desgaste.
 *
 * @author Luana Oliveira
 * @author Roberta Santos
 */
public class Motor {
    private boolean ligado;
    private double saude;

    /**
     * Cria o motor desligado com a saude inicial da maquina.
     *
     * @param saude saude inicial entre 0 e 100
     */
    Motor(double saude) {
        this.ligado = false;
        this.saude = saude;
    }

    /**
     * Liga o motor se ele estiver funcionando.
     *
     * @throws PlantaException se o motor precisar de reparo
     */
    void ligar() {
        if (quebrado()) {
            throw new PlantaException("O motor precisa de reparo antes de ligar.");
        }
        ligado = true;
    }

    /** Desliga o motor. */
    void desligar() {
        ligado = false;
    }

    /**
     * Desgasta o motor junto com a maquina.
     *
     * @param desgaste qtd de saude perdida
     */
    void desgastar(double desgaste) {
        saude = Math.max(0.0, saude - desgaste);
    }

    /**
     * Confere se o motor parou de funcionar.
     *
     * @return true se a saude chegou a zero
     */
    boolean quebrado() {
        return saude <= 0.0;
    }

    /** Repara e desliga o motor. */
    void reparar() {
        saude = 100.0;
        ligado = false;
    }

    /**
     * Mostra o estado do motor pra auditoria.
     *
     * @return texto com saude e estado
     */
    String diagnostico() {
        return String.format("Motor | Saúde: %.1f%% | %s | %s", saude,
                quebrado() ? "Quebrado" : "Funcionando", ligado ? "Ligado" : "Desligado");
    }
}
