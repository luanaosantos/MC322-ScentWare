/**
 * Representa uma demanda ausente, incompatível ou num estado que não permite a operação.
 *
 * @author Luana Oliveira e Roberta Santos
 */
public class DemandaInvalidaException extends PlantaException {
    private static final long serialVersionUID = 1L;

    /**
     * Guarda o motivo da demanda não poder ser usada.
     *
     * @param mensagem explicação da situação inválida
     */
    public DemandaInvalidaException(String mensagem) {
        super(mensagem);
    }
}
