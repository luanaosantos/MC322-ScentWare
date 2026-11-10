// Converte cada maquina junto de suas partes.
package br.unicamp.ic.mc322.persistence.conversores;

import br.unicamp.ic.mc322.model.*;
import java.util.List;
import org.w3c.dom.Element;
import static br.unicamp.ic.mc322.persistence.xml.ElementosXml.*;

/**
 * Converte cada maquina junto de suas partes.
 * @author Luana Oliveira e Roberta Santos
 */
public class MaquinaXml {
    /**
     * Escreve a maquina e suas partes dentro do mesmo elemento.
     * @param pai elemento da linha
     * @param maquina maquina que sera salva
     * @throws IllegalArgumentException se o tipo da maquina nao for suportado
     */
    public void escrever(Element pai, Maquina maquina) {
        Element elemento = adicionar(pai, "maquina");
        atributos(elemento, "tipo", maquina.getTipo(), "nome", maquina.getNome(), "capacidade", maquina.getCapacidadeMaxima(),
                "probabilidadeFalha", maquina.getProbabilidadeFalha(), "custo", maquina.getCustoOperacao(),
                "saude", maquina.getHealth(), "desgaste", maquina.getMaximoDesgaste(),
                "ligada", maquina.estaLigada(), "falhas", maquina.getFalhas());
        atributos(adicionar(elemento, "motor"), "ligado", maquina.getMotor().estaLigado(), "saude", maquina.getMotor().getSaude());
        Element sensores = adicionar(elemento, "sensores");
        for (Sensor sensor : maquina.getSensores()) {
            atributos(adicionar(sensores, "sensor"), "presenca", sensor.isPresencaDetectada(), "funcionando", sensor.isFuncionando());
        }
        if (maquina instanceof MaquinaHomogeneizador) {
            ValvulaHomogeneizadora valvula = ((MaquinaHomogeneizador) maquina).getValvula();
            atributos(adicionar(elemento, "valvula"), "pressao", valvula.getPressaoAtual(), "funcionando", valvula.isFuncionando());
        } else if (maquina instanceof MaquinaEmpacotadora) {
            BicoEnvasador bico = ((MaquinaEmpacotadora) maquina).getBico();
            atributos(adicionar(elemento, "bico"), "aberto", bico.isAberto(), "funcionando", bico.isFuncionando());
        } else if (maquina instanceof MaquinaInspecao) {
            AtuadorDescarte atuador = ((MaquinaInspecao) maquina).getAtuador();
            atributos(adicionar(elemento, "atuador"), "descartados", atuador.getDescartados(), "funcionando", atuador.isFuncionando());
        } else throw new IllegalArgumentException("Tipo de maquina desconhecido.");
    }

    /**
     * Os construtores criam partes novas; a leitura so recupera seus valores.
     * @param elemento maquina salva com suas partes
     * @return maquina reconstruida com componentes proprios
     * @throws IllegalArgumentException se a configuracao ou as partes forem invalidas
     */
    public Maquina ler(Element elemento) {
        String nome = valor(elemento, "nome");
        double capacidade = numero(elemento, "capacidade");
        double falha = numero(elemento, "probabilidadeFalha");
        double custo = numero(elemento, "custo");
        double saude = numero(elemento, "saude");
        double desgaste = numero(elemento, "desgaste");
        List<Element> sensores = filhos(unico(elemento, "sensores"), "sensor");
        Maquina maquina;
        switch (valor(elemento, "tipo")) {
            case "Homogeneizador":
                conferirFilhos(elemento, "motor", "sensores", "valvula");
                MaquinaHomogeneizador homogeneizador = new MaquinaHomogeneizador(nome, capacidade, falha, custo, saude, desgaste, sensores.size());
                Element valvula = unico(elemento, "valvula");
                conferirFilhos(valvula);
                homogeneizador.getValvula().restaurarEstado(numero(valvula, "pressao"), logico(valvula, "funcionando"));
                maquina = homogeneizador;
                break;
            case "Empacotadora":
                conferirFilhos(elemento, "motor", "sensores", "bico");
                MaquinaEmpacotadora empacotadora = new MaquinaEmpacotadora(nome, capacidade, falha, custo, saude, desgaste, sensores.size());
                Element bico = unico(elemento, "bico");
                conferirFilhos(bico);
                empacotadora.getBico().restaurarEstado(logico(bico, "aberto"), logico(bico, "funcionando"));
                maquina = empacotadora;
                break;
            case "Inspecao":
                conferirFilhos(elemento, "motor", "sensores", "atuador");
                MaquinaInspecao inspecao = new MaquinaInspecao(nome, capacidade, falha, custo, saude, desgaste, sensores.size());
                Element atuador = unico(elemento, "atuador");
                conferirFilhos(atuador);
                inspecao.getAtuador().restaurarEstado(logico(atuador, "funcionando"), inteiro(atuador, "descartados"));
                maquina = inspecao;
                break;
            default: throw new IllegalArgumentException("Tipo de maquina desconhecido.");
        }
        Element motor = unico(elemento, "motor");
        conferirFilhos(motor);
        maquina.getMotor().restaurarEstado(logico(motor, "ligado"), numero(motor, "saude"));
        for (int i = 0; i < sensores.size(); i++) {
            Element sensor = sensores.get(i);
            conferirFilhos(sensor);
            maquina.getSensores().get(i).restaurarEstado(logico(sensor, "presenca"), logico(sensor, "funcionando"));
        }
        maquina.restaurarEstado(logico(elemento, "ligada"), inteiro(elemento, "falhas"));
        return maquina;
    }
}
