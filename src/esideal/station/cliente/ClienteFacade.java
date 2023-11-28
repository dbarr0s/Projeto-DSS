package esideal.station.cliente;

import java.util.Map;

import esideal.data.ClienteDAO;

public class ClienteFacade implements ICliente{

    private Map<Integer, Cliente> clientes;

    public ClienteFacade(){
        this.clientes = ClienteDAO.getInstance(); 
    }

    public boolean clienteValido(Integer NIF){
        return clientes.containsKey(NIF);
    }

    public boolean clienteTemVeiculos(Cliente c){
        if (c.getVeiculos().isEmpty()) return false;
        return true;
    }

    public Map<Integer, Cliente> getClientes() {
        return this.clientes;
    }
    
}
