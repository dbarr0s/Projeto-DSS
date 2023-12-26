package esideal.station.veiculo;

import java.util.Map;

import esideal.station.cliente.Cliente;

public interface IVeiculos {
    boolean veicExiste(String matricula);
    Cliente encontrarClientePorVeiculo(String matricula);
    Map<String, Veiculo> getVeiculos();
}
