/**
 * Avisa quando falta estoque ou budget pra completar uma operação.
 *
 * @author Luana Oliveira e Roberta Santos
 */
public class RecursoInsuficienteException extends PlantaException {
    private static final long serialVersionUID = 1L;

    /**
     * Explica qual recurso faltou e se a demanda foi cancelada.
     *
     * @param mensagem motivo da falta de recurso
     */
    public RecursoInsuficienteException(String mensagem) {
        super(mensagem);
    }
}
