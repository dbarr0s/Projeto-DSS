package esideal.station.cliente;

import java.util.Map;

import esideal.data.ClienteDAO;

public class ClienteFacade implements ICliente{

    private Map<String, Cliente> clientes;

    public ClienteFacade(){
        this.clientes = ClienteDAO.getInstance(); 
    }

    public boolean clienteValido(String nome){
        return clientes.containsKey(nome);
    }

    public boolean clienteTemVeiculos(Cliente c){
        if (c.getVeiculos().isEmpty()) return false;
        return true;
    }

    public Map<String, Cliente> getClientes() {
        return this.clientes;
    }
    
}
