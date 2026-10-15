/**
 * Erro de regra da fábrica que pode ser explicado no menu pra continuar usando o sistema.
 *
 * @author Luana Oliveira e Roberta Santos
 */
public class PlantaException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    /**
     * Guarda o motivo da operação não ter dado certo.
     *
     * @param mensagem explicação do erro pra quem está usando o sistema
     */
    public PlantaException(String mensagem) {
        super(mensagem);
    }
}
