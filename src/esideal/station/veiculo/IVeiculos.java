package esideal.station.veiculo;

import esideal.station.cliente.Cliente;

public interface IVeiculos {
    boolean veicExiste(String matricula);
    Cliente encontrarClientePorVeiculo(Veiculo veiculo);
}
