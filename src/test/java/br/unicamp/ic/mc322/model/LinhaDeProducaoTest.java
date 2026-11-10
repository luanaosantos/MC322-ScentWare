// Testes da composicao, das falhas e da ordem das etapas.
package br.unicamp.ic.mc322.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LinhaDeProducaoTest {
    @Test
    void falhaNaoPassaDeUmaMaquinaPraOutra() {
        MaquinaHomogeneizador primeira = new MaquinaHomogeneizador("A", 100, 1, 2, 100, 0, 3);
        MaquinaHomogeneizador segunda = new MaquinaHomogeneizador("B", 100, 0, 2, 100, 0, 1);
        primeira.processar(produtoComQualidade(1));
        assertTrue(primeira.precisaManutencao());
        assertFalse(segunda.precisaManutencao());
        assertTrue(primeira.gerarRelatorioDiagnostico().contains("3. Sensor"));
        assertFalse(segunda.gerarRelatorioDiagnostico().contains("2. Sensor"));
    }

    @Test
    void reparoDaLinhaConsertaPartesEMantemHistorico() {
        LinhaDeProducao linha = new LinhaDeProducao("Teste");
        MaquinaHomogeneizador mistura = new MaquinaHomogeneizador("Mistura", 100, 1, 2, 100, 0);
        MaquinaEmpacotadora envase = new MaquinaEmpacotadora("Envase", 100, 1, 3, 100, 0);
        linha.adicionarMaquina(mistura);
        linha.adicionarMaquina(envase);
        Produto produto = produtoComQualidade(1);
        mistura.processar(produto);
        envase.processar(produto);
        assertEquals(1, produto.getProbabilidadeFalhaAcumulada());
        assertTrue(linha.precisaManutencao());
        assertFalse(mistura.estaLigada());
        assertFalse(envase.estaLigada());
        String antes = mistura.gerarRelatorioDiagnostico();
        linha.repararMaquinas();
        assertFalse(linha.precisaManutencao());
        assertFalse(linha.gerarRelatorioDiagnostico().contains("Com falha"));
        assertTrue(antes.contains("Falhas: 1"));
        assertTrue(mistura.gerarRelatorioDiagnostico().contains("Falhas: 1"));
    }

    @Test
    void inspecaoAprovaOuRejeitaComQualidadeFixa() {
        MaquinaInspecao inspecao = new MaquinaInspecao("Inspecao", 100, 0, 1, 100, 0);
        Produto bom = produtoComQualidade(1);
        Produto ruim = produtoComQualidade(0);
        assertTrue(inspecao.processar(bom));
        assertEquals("Aprovado", bom.getStatus());
        assertFalse(inspecao.processar(ruim));
        assertEquals("Rejeitado", ruim.getStatus());
        assertTrue(inspecao.gerarRelatorioDiagnostico().contains("Descartados: 1"));
    }

    @Test
    void segundaFalhaDaInspecaoTravaAtuador() {
        MaquinaInspecao inspecao = new MaquinaInspecao("Inspecao", 100, 1, 1, 100, 0, 1);
        assertFalse(inspecao.processar(produtoComQualidade(1)));
        assertFalse(inspecao.processar(produtoComQualidade(1)));
        assertTrue(inspecao.gerarRelatorioDiagnostico().contains("Atuador de descarte | Com falha"));
        inspecao.reparar();
        assertFalse(inspecao.precisaManutencao());
    }

    @Test
    void linhaReparaMaquinaQuebradaESomaCustos() {
        LinhaDeProducao linha = new LinhaDeProducao("Teste");
        linha.adicionarMaquina(new MaquinaHomogeneizador("Mistura", 100, 0, 2, 0, 0));
        linha.adicionarMaquina(new MaquinaEmpacotadora("Envase", 100, 0, 3, 100, 0));
        linha.adicionarMaquina(new MaquinaInspecao("Inspecao", 100, 0, 4, 100, 0));
        Produto produto = produtoComQualidade(1);
        assertEquals(9, linha.getCustoPorProduto());
        assertEquals(9, linha.processarProduto(produto));
        assertEquals("Aprovado", produto.getStatus());
        assertFalse(linha.precisaManutencao());
    }

    @Test
    void listaDeMaquinasNaoPermiteAlterarLinha() {
        LinhaDeProducao linha = new LinhaDeProducao("Teste");
        Maquina maquina = new MaquinaHomogeneizador("M", 100, 0, 1, 100, 0);
        linha.adicionarMaquina(maquina);
        assertThrows(UnsupportedOperationException.class, () -> linha.getMaquinas().clear());
        assertThrows(IllegalArgumentException.class, () -> linha.adicionarMaquina(maquina));
        assertEquals(1, linha.getMaquinas().size());
        assertThrows(IllegalArgumentException.class, () -> linha.processarProduto(produtoComQualidade(1)));
        assertThrows(IllegalArgumentException.class, () -> new MaquinaEmpacotadora("M", 100, 0, 1, 100, 0, 0));
    }

    private Produto produtoComQualidade(double qualidade) {
        // Os limites 0 e 1 tiram o sorteio da aprovacao deste teste.
        return new Produto("P", "Produto", 20, qualidade) {
            @Override public void processar() { setStatus("Em processamento"); }
            @Override public double calcularTempoProducao() { return 1; }
            @Override public TipoProduto getTipo() { return TipoProduto.HIDRATANTE; }
        };
    }
}
