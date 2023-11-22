package esideal.station.facades;

import java.util.HashMap;
import java.util.Map;

import esideal.station.cliente.*;
import esideal.station.funcionario.*;
import esideal.station.servico.Servico;

public class StationFacade {
    private Map<String, Cliente> clientes;
    private Map<Integer, Funcionario> funcionarios;
    private Map<String, Servico> servicosPendentes; //Identificados pelo nome do cliente
    private Funcionario logado;

    public StationFacade(){
        this.clientes = new HashMap<>();
        this.funcionarios = new HashMap<>();
    }

    public boolean login(int IDFuncionario){
        if(funcionarios.containsKey(IDFuncionario)){
            if (funcionarios.containsKey(IDFuncionario)) {
                logado = funcionarios.get(IDFuncionario);
                return true;
            }
        }
        return false;
    }
}