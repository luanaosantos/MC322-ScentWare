// Erro pra demanda que nao pode ser usada.

/**
 * Avisa q a demanda nao existe ou nao pode ser usada nesse estado.
 *
 * @author Luana Oliveira e Roberta Santos
 */
public class DemandaInvalidaException extends PlantaException {
    private static final long serialVersionUID = 1L;

    /**
     * Guarda a mensagem do erro da demanda.
     *
     * @param mensagem explicacao da situacao invalida
     */
    public DemandaInvalidaException(String mensagem) {
        super(mensagem);
    }
}
