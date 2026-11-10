// Erro de leitura, escrita ou validacao do estado salvo.
package br.unicamp.ic.mc322.exception;


/**
 * Avisa o menu quando o XML nao pode ser salvo ou recuperado.
 * @author Luana Oliveira e Roberta Santos
 */
public class PersistenciaException extends PlantaException {
    private static final long serialVersionUID = 1L;

    /**
     * Guarda a explicacao e a causa da falha.
     * @param mensagem explicacao para quem usa o menu
     * @param causa erro que impediu a operacao
     */
    public PersistenciaException(String mensagem, Throwable causa) {
        super(mensagem);
        initCause(causa);
    }
}
