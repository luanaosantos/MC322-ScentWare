// Testes das partes antes e depois de falhas e reparos.
package br.unicamp.ic.mc322.model;

import br.unicamp.ic.mc322.exception.PlantaException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ComponentesTest {
    @Test
    void motorQuebradoNaoLigaEReparoDesliga() {
        Motor motor = new Motor(100);
        motor.ligar();
        motor.desgastar(100);
        assertTrue(motor.quebrado());
        assertThrows(PlantaException.class, motor::ligar);
        motor.reparar();
        assertFalse(motor.quebrado());
        assertFalse(motor.estaLigado());
        assertEquals(100, motor.getSaude());
    }

    @Test
    void sensorReparadoVoltaADetectar() {
        Sensor sensor = new Sensor();
        assertTrue(sensor.detectarPresenca());
        sensor.registrarFalha();
        assertTrue(sensor.precisaReparo());
        assertFalse(sensor.detectarPresenca());
        sensor.reparar();
        assertTrue(sensor.detectarPresenca());
        sensor.limparPresenca();
        assertFalse(sensor.isPresencaDetectada());
    }

    @Test
    void bicoEValvulaFechamQuandoFalham() {
        BicoEnvasador bico = new BicoEnvasador();
        assertTrue(bico.abrirBicoEnvasador());
        bico.registrarFalha();
        assertFalse(bico.isAberto());
        assertFalse(bico.abrirBicoEnvasador());
        bico.reparar();
        assertTrue(bico.abrirBicoEnvasador());
        bico.fechar();
        assertFalse(bico.isAberto());

        ValvulaHomogeneizadora valvula = new ValvulaHomogeneizadora();
        assertTrue(valvula.abrir());
        assertEquals(1, valvula.getPressaoAtual());
        valvula.registrarFalha();
        assertEquals(0, valvula.getPressaoAtual());
        assertFalse(valvula.abrir());
        valvula.reparar();
        assertTrue(valvula.abrir());
        valvula.fechar();
        assertEquals(0, valvula.getPressaoAtual());
    }

    @Test
    void atuadorPreservaDescartesAposReparo() {
        AtuadorDescarte atuador = new AtuadorDescarte();
        assertTrue(atuador.encaminhar(false));
        assertTrue(atuador.encaminhar(true));
        atuador.registrarFalha();
        assertFalse(atuador.encaminhar(true));
        atuador.reparar();
        assertTrue(atuador.encaminhar(false));
        assertEquals(1, atuador.getDescartados());
    }

    @Test
    void restauracaoRecusaEstadosIncompativeisSemAlterarParte() {
        Motor motor = new Motor(100);
        assertThrows(IllegalArgumentException.class, () -> motor.restaurarEstado(true, 0));
        assertEquals(100, motor.getSaude());
        assertFalse(motor.estaLigado());
        Sensor sensor = new Sensor();
        assertThrows(IllegalArgumentException.class, () -> sensor.restaurarEstado(true, false));
        assertTrue(sensor.isFuncionando());
        BicoEnvasador bico = new BicoEnvasador();
        assertThrows(IllegalArgumentException.class, () -> bico.restaurarEstado(true, false));
        assertFalse(bico.isAberto());
        ValvulaHomogeneizadora valvula = new ValvulaHomogeneizadora();
        assertThrows(IllegalArgumentException.class, () -> valvula.restaurarEstado(Double.NaN, true));
        assertEquals(0, valvula.getPressaoAtual());
        AtuadorDescarte atuador = new AtuadorDescarte();
        assertThrows(IllegalArgumentException.class, () -> atuador.restaurarEstado(false, -1));
        assertTrue(atuador.isFuncionando());
    }
}
