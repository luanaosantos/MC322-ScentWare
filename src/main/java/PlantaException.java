// Erro base das regras da fabrica.

/**
 * Erro de regra da fabrica q o menu pode tratar.
 *
 * @author Luana Oliveira e Roberta Santos
 */
public class PlantaException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    /**
     * Guarda a mensagem do erro da fabrica.
     *
     * @param mensagem explicacao do erro pra quem esta usando o sistema
     */
    public PlantaException(String mensagem) {
        super(mensagem);
    }
}
