package esideal.station;

import java.util.HashMap;
import java.util.Map;

import esideal.station.cliente.*;
import esideal.station.funcionario.*;

public class StationFacade {
    private Map<String, Cliente> clientes;
    private Map<String, Funcionario> funcionarios;

    public StationFacade(){
        this.clientes = new HashMap<>();
        this.funcionarios = new HashMap<>();
    }

    public boolean login(String username, String password){
        if(funcionarios.containsKey(username)){
            if (funcionarios.get(username).checkPassword(password)) {
                return true;
            }
        }
        return false;
    }
}