// Sensor de presenca usado dentro das maquinas.

/**
 * Detecta o produto e guarda se o sensor esta funcionando.
 *
 * @author Luana Oliveira
 * @author Roberta Santos
 */
public class Sensor {
    private boolean presencaDetectada;
    private boolean funcionando;

    /** Cria o sensor sem produto e funcionando. */
    Sensor() {
        presencaDetectada = false;
        funcionando = true;
    }

    /**
     * Detecta o produto q chegou na maquina.
     *
     * @return true se o sensor conseguiu detectar
     */
    boolean detectarPresenca() {
        presencaDetectada = funcionando;
        return presencaDetectada;
    }

    /** Limpa a leitura depois q a etapa termina. */
    void limparPresenca() {
        presencaDetectada = false;
    }

    /** Registra uma falha na leitura. */
    void registrarFalha() {
        funcionando = false;
        presencaDetectada = false;
    }

    /**
     * Confere se o sensor precisa de reparo.
     *
     * @return true se o sensor falhou
     */
    boolean precisaReparo() {
        return !funcionando;
    }

    /** Repara o sensor e limpa a leitura. */
    void reparar() {
        funcionando = true;
        presencaDetectada = false;
    }

    /**
     * Mostra o estado e a leitura do sensor.
     *
     * @return texto pra auditoria
     */
    String diagnostico() {
        return "Sensor | " + (funcionando ? "Funcionando" : "Com falha")
                + " | Presença: " + (presencaDetectada ? "Sim" : "Não");
    }
}
