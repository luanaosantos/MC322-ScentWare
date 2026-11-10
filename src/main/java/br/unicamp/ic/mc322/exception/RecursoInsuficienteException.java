// Erro de falta de estoque ou budget.
package br.unicamp.ic.mc322.exception;



/**
 * Avisa q falta estoque ou budget pra operacao.
 *
 * @author Luana Oliveira e Roberta Santos
 */
public class RecursoInsuficienteException extends PlantaException {
    private static final long serialVersionUID = 1L;

    /**
     * Guarda a mensagem de falta de recurso.
     *
     * @param mensagem motivo da falta de recurso
     */
    public RecursoInsuficienteException(String mensagem) {
        super(mensagem);
    }
}
