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

    public Map<String, Veiculo> getVeiculos(){
        return this.veiculos;
    }

    /**
     * Verifica se um veículo com a matrícula especificada existe.
     * @param matricula Matrícula do veículo a ser verificado.
     * @return true se o veículo com a matrícula existir, caso contrário, false.
     */

    public boolean veicExiste(String matricula){   
        return veiculos.containsKey(matricula);
    }

    /**
     * Encontra o cliente associado a um veículo específico através da matrícula.
     * @param matricula Matrícula do veículo para encontrar o cliente associado.
     * @return O cliente associado ao veículo, se encontrado; caso contrário, retorna null.
     */

    public Cliente encontrarClientePorVeiculo(String matricula){
        ClienteFacade c = new ClienteFacade();
        for (Cliente cliente : c.getClientes().values()) {
            if (cliente.getVeiculos().containsKey(matricula)) {
                return cliente;
            }
        }
        return null;
    }
}
