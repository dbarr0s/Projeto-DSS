package esideal.station.veiculo;

import java.util.Map;

import esideal.data.VeiculoDAO;
import esideal.station.cliente.Cliente;
import esideal.station.cliente.ClienteFacade;

public class VeiculoFacade implements IVeiculos{
    private Map<String, Veiculo> veiculos;

    public VeiculoFacade(){
        this.veiculos = VeiculoDAO.getInstance(); 
    }

    public boolean veicExiste(String matricula){   
        return veiculos.containsKey(matricula);
    }

    public Cliente encontrarClientePorVeiculo(String matricula, ClienteFacade c){
        for (Cliente cliente : c.getClientes().values()) {
            if (cliente.getVeiculos().containsKey(matricula)) {
                return cliente;
            }
        }
        return null;
    }
}
