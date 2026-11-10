// Testes de leitura e escrita isolados dos dados usados pelo menu.
package br.unicamp.ic.mc322.persistence;

import br.unicamp.ic.mc322.exception.PersistenciaException;
import br.unicamp.ic.mc322.model.*;
import br.unicamp.ic.mc322.service.GerenciadorProducao;
import br.unicamp.ic.mc322.strategy.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;
import static br.unicamp.ic.mc322.persistence.apoio.FabricaTeste.criar;

class PersistenciaPlantaTest {
    @TempDir Path pasta;

    @Test
    void salvarECarregarPreservaEstadoECriaOutrosObjetos() throws IOException {
        GerenciadorProducao original = criar();
        Path arquivo = pasta.resolve("planta.xml");
        PersistenciaPlanta persistencia = new PersistenciaPlanta(arquivo);
        persistencia.salvar(original);
        assertTrue(Files.size(arquivo) > 0);
        assertTrue(Files.readString(arquivo).contains("<planta"));
        int totalAntes = Produto.getTotalProdutosFabricados();
        GerenciadorProducao recuperada = persistencia.carregar();
        assertNotSame(original, recuperada);
        assertNotSame(original.getMateriaPrima(), recuperada.getMateriaPrima());
        assertNotSame(original.getLinha(), recuperada.getLinha());
        assertEquals(original.getBudget(), recuperada.getBudget());
        assertEquals(original.getMateriaPrima().getId(), recuperada.getMateriaPrima().getId());
        assertEquals(original.getMateriaPrima().getNome(), recuperada.getMateriaPrima().getNome());
        assertEquals(original.getMateriaPrima().getQuantidade(), recuperada.getMateriaPrima().getQuantidade());
        assertEquals(original.getMateriaPrima().getUnidade(), recuperada.getMateriaPrima().getUnidade());
        assertEquals(original.getMateriaPrima().getCustoPorUnidade(), recuperada.getMateriaPrima().getCustoPorUnidade());
        assertEquals(original.getCenario(), recuperada.getCenario());
        assertEquals(original.getRiscoInicialProduto(), recuperada.getRiscoInicialProduto());
        assertEquals(original.getEstrategiaAtual().getClass(), recuperada.getEstrategiaAtual().getClass());
        assertEquals(original.getProximoLote(), recuperada.getProximoLote());
        assertEquals(original.getProximoProduto(), recuperada.getProximoProduto());
        assertEquals(totalAntes, Produto.getTotalProdutosFabricados());
        assertEquals(original.getLinha().gerarRelatorioDiagnostico(), recuperada.getLinha().gerarRelatorioDiagnostico());
        for (int i = 0; i < original.getLinha().getMaquinas().size(); i++) {
            Maquina antes = original.getLinha().getMaquinas().get(i);
            Maquina depois = recuperada.getLinha().getMaquinas().get(i);
            assertNotSame(antes, depois);
            assertNotSame(antes.getMotor(), depois.getMotor());
            assertEquals(antes.getClass(), depois.getClass());
            assertEquals(antes.getCapacidadeMaxima(), depois.getCapacidadeMaxima());
            assertEquals(antes.getCustoOperacao(), depois.getCustoOperacao());
            assertEquals(antes.getMaximoDesgaste(), depois.getMaximoDesgaste());
            assertEquals(antes.getProbabilidadeFalha(), depois.getProbabilidadeFalha());
            assertEquals(antes.estaLigada(), depois.estaLigada());
            for (int s = 0; s < antes.getSensores().size(); s++) {
                assertNotSame(antes.getSensores().get(s), depois.getSensores().get(s));
            }
        }
        assertEquals(original.getDemandas().size(), recuperada.getDemandas().size());
        for (int i = 0; i < original.getDemandas().size(); i++) {
            Demanda antes = original.getDemandas().get(i);
            Demanda depois = recuperada.getDemandas().get(i);
            assertNotSame(antes, depois);
            assertEquals(antes.getTipoProduto(), depois.getTipoProduto());
            assertEquals(antes.getQuantidadeProdutos(), depois.getQuantidadeProdutos());
            assertEquals(antes.getStatus(), depois.getStatus());
            assertEquals(antes.getCustoTotal(), depois.getCustoTotal());
        }
        assertEquals(3, recuperada.getProdutosModelos().size());
        assertEquals(1, recuperada.getProdutosFabricados().size());
        Produto produto = recuperada.getProdutosFabricados().get(0);
        assertEquals("P3-7", produto.getId());
        assertEquals("Aprovado", produto.getStatus());
        assertEquals(2, produto.getLote());
        assertEquals(0.3, produto.getProbabilidadeFalhaAcumulada());
        assertEquals(0.9, produto.getQualidade());
    }

    @Test
    void salvarNovamenteAtualizaArquivoSemDuplicarColecoes() {
        PersistenciaPlanta persistencia = new PersistenciaPlanta(pasta.resolve("planta.xml"));
        GerenciadorProducao fabrica = criar();
        persistencia.salvar(fabrica);
        fabrica.comprarMateriaPrima(10);
        fabrica.atualizarDemanda(TipoProduto.HIDRATANTE, 4);
        fabrica.setEstrategia(new EstrategiaOrdemChegada());
        persistencia.salvar(fabrica);
        GerenciadorProducao recuperada = persistencia.carregar();
        assertEquals(310, recuperada.getMateriaPrima().getQuantidade());
        assertEquals(199, recuperada.getBudget());
        assertEquals(4, recuperada.buscarDemandaPendente(TipoProduto.HIDRATANTE).getQuantidadeProdutos());
        assertEquals(4, recuperada.getDemandas().size());
        assertEquals(3, recuperada.getLinha().getMaquinas().size());
        assertEquals(1, recuperada.getProdutosFabricados().size());
        assertInstanceOf(EstrategiaOrdemChegada.class, recuperada.getEstrategiaAtual());
    }

    @Test
    void repararObjetosCarregadosNaoAlteraOriginais() {
        GerenciadorProducao original = criar();
        PersistenciaPlanta persistencia = new PersistenciaPlanta(pasta.resolve("planta.xml"));
        persistencia.salvar(original);
        GerenciadorProducao recuperada = persistencia.carregar();
        recuperada.repararLinha();
        assertFalse(recuperada.getLinha().precisaManutencao());
        assertTrue(original.getLinha().precisaManutencao());
        MaquinaInspecao inspecao = (MaquinaInspecao) recuperada.getLinha().getMaquinas().get(2);
        assertEquals(12, inspecao.getAtuador().getDescartados());
    }

    @Test
    void plantaCarregadaContinuaProducaoComContadoresSalvos() {
        PersistenciaPlanta persistencia = new PersistenciaPlanta(pasta.resolve("planta.xml"));
        persistencia.salvar(criar());
        GerenciadorProducao recuperada = persistencia.carregar();
        // O atuador salvo esta com falha: nenhum produto novo pode ir pro armazem.
        recuperada.executarProximaProducao();
        assertEquals(4, recuperada.getProximoLote());
        assertEquals(10, recuperada.getProximoProduto());
        assertEquals(260, recuperada.getMateriaPrima().getQuantidade());
        assertEquals(182, recuperada.getBudget());
        assertEquals(1, recuperada.getProdutosFabricados().size());
        assertEquals(2, recuperada.buscarDemandaPendente(TipoProduto.HIDRATANTE).getQuantidadeProdutos());
        persistencia.salvar(recuperada);
        GerenciadorProducao outraExecucao = persistencia.carregar();
        assertEquals(10, outraExecucao.getProximoProduto());
        assertEquals(4, outraExecucao.getProximoLote());
    }

    @Test
    void arquivoInexistenteGeraErroDePersistencia() {
        PersistenciaPlanta persistencia = new PersistenciaPlanta(pasta.resolve("ausente.xml"));
        assertThrows(PersistenciaException.class, persistencia::carregar);
    }

    @Test
    void arquivoVazioOuCorrompidoGeraErroDePersistencia() throws IOException {
        Path arquivo = pasta.resolve("planta.xml");
        PersistenciaPlanta persistencia = new PersistenciaPlanta(arquivo);
        Files.writeString(arquivo, "");
        assertThrows(PersistenciaException.class, persistencia::carregar);
        Files.writeString(arquivo, "<planta><estoque>");
        assertThrows(PersistenciaException.class, persistencia::carregar);
    }

    @Test
    void documentoComDtdNaoECarregado() throws IOException {
        Path arquivo = pasta.resolve("planta.xml");
        PersistenciaPlanta persistencia = new PersistenciaPlanta(arquivo);
        persistencia.salvar(criar());
        String xml = Files.readString(arquivo);
        String dtd = "<!DOCTYPE planta [<!ENTITY nome 'Outra planta'>]>";
        Files.writeString(arquivo, xml.replaceFirst("<planta", dtd + "\n<planta"));
        assertThrows(PersistenciaException.class, persistencia::carregar);
    }

    @Test
    void estruturaIncompativelNaoSubstituiEstadoAtual() throws IOException {
        GerenciadorProducao atual = criar();
        Path arquivo = pasta.resolve("planta.xml");
        Files.writeString(arquivo, "<outraFabrica/>");
        PersistenciaPlanta persistencia = new PersistenciaPlanta(arquivo);
        assertThrows(PersistenciaException.class, persistencia::carregar);
        assertEquals(300, atual.getMateriaPrima().getQuantidade());
        assertEquals(200, atual.getBudget());
        assertEquals(4, atual.getDemandas().size());
        Files.writeString(arquivo, "<planta/>");
        assertThrows(PersistenciaException.class, persistencia::carregar);
    }

    @Test
    void diretorioNaoPodeSerLidoOuSobrescritoComoArquivo() {
        PersistenciaPlanta persistencia = new PersistenciaPlanta(pasta);
        assertThrows(PersistenciaException.class, persistencia::carregar);
        assertThrows(PersistenciaException.class, () -> persistencia.salvar(criar()));
        assertTrue(Files.isDirectory(pasta));
    }

    @ParameterizedTest(name = "XML invalido: {0}")
    @ValueSource(strings = {"estoqueNegativo", "saudeNaN", "booleanoInvalido", "statusDesconhecido",
            "custoIncompativel", "contadorIncompativel", "semMotor", "secaoRepetida", "versaoDesconhecida", "textoCdata"})
    void dadosInvalidosDoArquivoSaoRecusados(String caso) throws IOException {
        Path arquivo = pasta.resolve("planta.xml");
        PersistenciaPlanta persistencia = new PersistenciaPlanta(arquivo);
        persistencia.salvar(criar());
        String xml = Files.readString(arquivo);
        String alterado;
        switch (caso) {
            case "estoqueNegativo": alterado = xml.replace("quantidade=\"300.0\"", "quantidade=\"-1\""); break;
            case "saudeNaN": alterado = xml.replace("saude=\"35.0\"", "saude=\"NaN\""); break;
            case "booleanoInvalido": alterado = xml.replace("ligada=\"false\"", "ligada=\"talvez\""); break;
            case "statusDesconhecido": alterado = xml.replace("status=\"PENDENTE\"", "status=\"DESCONHECIDO\""); break;
            case "custoIncompativel": alterado = xml.replace("custo=\"18.0\"", "custo=\"1.0\""); break;
            case "contadorIncompativel": alterado = xml.replace("proximoProduto=\"8\"", "proximoProduto=\"7\""); break;
            case "semMotor": alterado = xml.replaceAll("<motor[^>]*/>", ""); break;
            case "secaoRepetida": alterado = xml.replace("</planta>", "<demandas/></planta>"); break;
            case "versaoDesconhecida": alterado = xml.replace("versao=\"1\"", "versao=\"99\""); break;
            case "textoCdata": alterado = xml.replace("</planta>", "<![CDATA[texto inesperado]]></planta>"); break;
            default: throw new IllegalArgumentException("Caso desconhecido.");
        }
        assertNotEquals(xml, alterado, "O caso precisa alterar o XML salvo.");
        Files.writeString(arquivo, alterado);
        assertThrows(PersistenciaException.class, persistencia::carregar);
    }

    @Test
    void salvarEstadoInvalidoPreservaUltimoArquivoValido() throws IOException {
        Path arquivo = pasta.resolve("planta.xml");
        PersistenciaPlanta persistencia = new PersistenciaPlanta(arquivo);
        GerenciadorProducao fabrica = criar();
        persistencia.salvar(fabrica);
        String anterior = Files.readString(arquivo);
        fabrica.getDemandas().get(0).setCustoTotal(1);
        assertThrows(PersistenciaException.class, () -> persistencia.salvar(fabrica));
        assertEquals(anterior, Files.readString(arquivo));
        assertEquals(18, persistencia.carregar().getDemandas().get(0).getCustoTotal());
    }

}
