package esideal.station.veiculo;

import esideal.station.cliente.Cliente;
import esideal.station.cliente.ClienteFacade;

public interface IVeiculos {
    boolean veicExiste(String matricula);
    Cliente encontrarClientePorVeiculo(String matricula, ClienteFacade c);
}
