// Testes da recuperacao das maquinas e das partes que pertencem a elas.
package br.unicamp.ic.mc322.persistence.conversores;

import br.unicamp.ic.mc322.model.*;
import br.unicamp.ic.mc322.persistence.xml.DocumentoXml;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import static br.unicamp.ic.mc322.persistence.xml.ElementosXml.*;
import static org.junit.jupiter.api.Assertions.*;

class MaquinaXmlTest {
    @Test
    void cadaTipoRecuperaPartesNovasComHistoricoDeFalhas() throws Exception {
        Element raiz = raiz();
        MaquinaXml xml = new MaquinaXml();
        MaquinaHomogeneizador mistura = new MaquinaHomogeneizador("Mistura", 100, 0, 2, 80, 3, 3);
        mistura.getValvula().restaurarEstado(0, false);
        mistura.getSensores().get(1).restaurarEstado(false, false);
        MaquinaEmpacotadora envase = new MaquinaEmpacotadora("Envase", 200, 0, 3, 90, 2, 1);
        envase.getBico().restaurarEstado(false, false);
        MaquinaInspecao inspecao = new MaquinaInspecao("Inspecao", 300, 0, 4, 70, 1, 2);
        inspecao.getAtuador().restaurarEstado(false, 7);
        for (Maquina maquina : List.of(mistura, envase, inspecao)) {
            maquina.restaurarEstado(false, 4);
            xml.escrever(raiz, maquina);
        }
        List<Element> elementos = filhos(raiz, "maquina");
        List<Maquina> originais = List.of(mistura, envase, inspecao);
        for (int i = 0; i < elementos.size(); i++) {
            Maquina recuperada = xml.ler(elementos.get(i));
            Maquina original = originais.get(i);
            assertEquals(original.gerarRelatorioDiagnostico(), recuperada.gerarRelatorioDiagnostico());
            assertNotSame(original.getMotor(), recuperada.getMotor());
            assertNotSame(original.getSensores().get(0), recuperada.getSensores().get(0));
            recuperada.reparar();
            assertFalse(recuperada.precisaManutencao());
            assertTrue(original.precisaManutencao());
            assertEquals(4, recuperada.getFalhas());
            if (recuperada instanceof MaquinaInspecao) {
                assertEquals(7, ((MaquinaInspecao) recuperada).getAtuador().getDescartados());
            }
        }
    }

    @Test
    void maquinaLigadaExigeMotorLigadoNoArquivo() throws Exception {
        Element raiz = raiz();
        MaquinaXml xml = new MaquinaXml();
        xml.escrever(raiz, new MaquinaInspecao("Inspecao", 100, 0, 1, 100, 0));
        Element maquina = unico(raiz, "maquina");
        maquina.setAttribute("ligada", "true");
        assertThrows(IllegalArgumentException.class, () -> xml.ler(maquina));
        maquina.setAttribute("ligada", "false");
        maquina.removeChild(unico(maquina, "motor"));
        assertThrows(IllegalArgumentException.class, () -> xml.ler(maquina));
    }

    private Element raiz() throws Exception {
        Document documento = new DocumentoXml().criar();
        Element raiz = documento.createElement("linha");
        documento.appendChild(raiz);
        return raiz;
    }
}
