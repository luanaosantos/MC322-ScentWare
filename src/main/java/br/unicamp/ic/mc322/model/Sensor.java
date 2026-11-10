// Sensor de presenca usado dentro das maquinas.
package br.unicamp.ic.mc322.model;


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

    /**
     * Consulta a leitura atual do sensor.
     * @return true se ha presenca detectada
     */
    public boolean isPresencaDetectada() {
        return presencaDetectada;
    }

    /**
     * Consulta a condicao do sensor.
     * @return true se esta funcionando
     */
    public boolean isFuncionando() {
        return funcionando;
    }

    /**
     * Recupera a leitura e a condicao do sensor.
     * @param presencaDetectada leitura salva
     * @param funcionando condicao salva
     * @throws IllegalArgumentException se um sensor com falha tiver detectado presenca
     */
    public void restaurarEstado(boolean presencaDetectada, boolean funcionando) {
        if (presencaDetectada && !funcionando) {
            throw new IllegalArgumentException("Sensor com falha não pode detectar presença.");
        }
        this.presencaDetectada = presencaDetectada;
        this.funcionando = funcionando;
    }

}
