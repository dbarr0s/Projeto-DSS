package esideal.station.cliente;

import java.util.Map;

import esideal.data.ClienteDAO;

public class ClienteFacade implements ICliente{

    private Map<String, Cliente> clientes;

    public ClienteFacade(){
        this.clientes = ClienteDAO.getInstance(); 
    }

    /**
     * Verifica se um cliente está regisrado no sistema.
     * @param nome Nome do cliente a ser verificado.
     * @return Verdadeiro se o cliente estiver registado; falso, caso contrário.
     */

    public boolean clienteValido(String nome){
        return clientes.containsKey(nome);
    }

    /**
     * Verifica se um cliente possui veículos registados.
     * @param c Cliente para verificar se possui veículos.
     * @return Verdadeiro se o cliente possui veículos registados; falso, caso contrário.
     */

    public boolean clienteTemVeiculos(Cliente c){
        if (c.getVeiculos().isEmpty()) return false;
        return true;
    }

    public Map<String, Cliente> getClientes() {
        return this.clientes;
    }
    
}
