// Agrupa as maquinas da linha na ordem em q sao usadas.

import java.util.ArrayList;
import java.util.List;

/**
 * Guarda as maquinas e junta a auditoria de cada uma.
 * As maquinas podem existir fora da linha; suas partes pertencem a elas.
 *
 * @author Luana Oliveira
 * @author Roberta Santos
 */
public class LinhaDeProducao implements Auditavel {
    private String nome;
    private final ArrayList<Maquina> maquinas;

    /**
     * Cria uma linha sem maquinas.
     *
     * @param nome nome da linha
     * @throws IllegalArgumentException se o nome estiver vazio
     */
    public LinhaDeProducao(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Informe o nome da linha.");
        }
        this.nome = nome;
        this.maquinas = new ArrayList<>();
    }

    /**
     * Adiciona uma maquina ao fim da linha.
     *
     * @param maquina maquina ainda nao cadastrada na linha
     * @throws IllegalArgumentException se a maquina for nula ou repetida
     */
    public void adicionarMaquina(Maquina maquina) {
        if (maquina == null || maquinas.contains(maquina)) {
            throw new IllegalArgumentException("Informe uma máquina válida que ainda não esteja na linha.");
        }
        maquinas.add(maquina);
    }

    /**
     * Confere se a maquina ja esta na linha.
     *
     * @param maquina maquina procurada
     * @return true se estiver cadastrada
     */
    public boolean contemMaquina(Maquina maquina) {
        return maquinas.contains(maquina);
    }

    /**
     * Devolve uma copia q nao permite mudar a lista da linha.
     *
     * @return maquinas na ordem da producao
     */
    public List<Maquina> getMaquinas() {
        return List.copyOf(maquinas);
    }

    /**
     * Confere se ha maquinas e se a ultima e a inspecao.
     *
     * @return true se a linha pode receber um lote
     */
    public boolean prontaParaProducao() {
        return !maquinas.isEmpty() && maquinas.get(maquinas.size() - 1) instanceof MaquinaInspecao;
    }

    /**
     * Soma o custo de passar um produto por todas as maquinas.
     *
     * @return custo estimado por produto
     */
    public double getCustoPorProduto() {
        double custo = 0.0;
        for (Maquina maquina : maquinas) {
            custo += maquina.getCustoOperacao();
        }
        return custo;
    }

    /**
     * Passa o produto pelas maquinas na ordem e soma o custo das etapas usadas.
     *
     * @param produto produto q vai passar pela linha
     * @return custo das etapas q foram executadas
     * @throws IllegalArgumentException se o produto ou a linha forem invalidos
     */
    public double processarProduto(Produto produto) {
        if (produto == null || !prontaParaProducao()) {
            throw new IllegalArgumentException("Informe um produto e uma linha pronta para produção.");
        }
        double custo = 0.0;
        for (Maquina maquina : maquinas) {
            // Se quebrou, repara antes de continuar.
            if (maquina.quebrada()) {
                System.out.println("\n[AVISO] - Produção interrompida!");
                System.out.println("[AVISO] - A máquina " + maquina.getNome() + " está quebrada.");
                maquina.reparar();
                System.out.println("[AVISO] - Máquina reparada. Produção retomada.");
            }
            custo += maquina.getCustoOperacao();
            if (!maquina.processar(produto)) {
                produto.setStatus("Rejeitado");
                break;
            }
        }
        return custo;
    }

    /** Repara as maquinas e suas partes antes da proxima producao. */
    public void repararMaquinas() {
        for (Maquina maquina : maquinas) {
            maquina.reparar();
        }
    }

    /**
     * Junta os relatorios da linha, das maquinas e de suas partes.
     *
     * @return diagnostico da linha
     */
    @Override
    public String gerarRelatorioDiagnostico() {
        StringBuilder relatorio = new StringBuilder("Linha de produção: " + nome);
        if (maquinas.isEmpty()) {
            relatorio.append("\n  Nenhuma máquina cadastrada.");
        }
        for (Maquina maquina : maquinas) {
            relatorio.append("\n  ").append(maquina.gerarRelatorioDiagnostico().replace("\n", "\n  "));
        }
        return relatorio.toString();
    }

    /**
     * Confere se alguma maquina ou parte precisa de atencao.
     *
     * @return true se alguma maquina precisar de manutencao
     */
    @Override
    public boolean precisaManutencao() {
        for (Maquina maquina : maquinas) {
            if (maquina.precisaManutencao()) {
                return true;
            }
        }
        return false;
    }
}
