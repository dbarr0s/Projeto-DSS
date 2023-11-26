package esideal.station.facades;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import esideal.station.checkup.CheckUp;
import esideal.station.checkup.ICheckUp;
import esideal.station.cliente.*;
import esideal.station.ficha.IFichaVeiculo;
import esideal.station.funcionario.*;
import esideal.station.servico.IServico;
import esideal.station.servico.Servico;
import esideal.station.veiculo.IVeiculos;

public class StationFacade implements IFuncionário, ICheckUp, IFichaVeiculo, IServico, ICliente, IVeiculos{
    private Map<String, Cliente> clientes;
    public Map<Integer, Funcionario> funcionarios;
    private Map<Integer, Servico> servicosPendentes; //Identificados pelo número de serviço
    private Map<Integer, Servico> servicosExecutados; //Identificados pelo número de serviço
    private Map<Integer, CheckUp> checkUpPendentes; //Identificados pelo número de check-up
    private Map<Integer, CheckUp> checkUpExecutados; //Identificados pelo número de check-up

    public StationFacade(){
        this.clientes = clientes;
        this.funcionarios = funcionarios;
        this.servicosPendentes = servicosPendentes;
        this.servicosExecutados = servicosExecutados; 
        this.checkUpPendentes = checkUpPendentes; 
        this.checkUpExecutados = checkUpExecutados;
    }

    @Override
    public void veicExiste() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'veicExiste'");
    }

    @Override
    public void clienteValido() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'clienteValido'");
    }

    @Override
    public void addVeiculos() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addVeiculos'");
    }

    @Override
    public void agendarServico() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'agendarServico'");
    }

    @Override
    public void pedirServico() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'pedirServico'");
    }

    @Override
    public void iniciarServico() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'iniciarServico'");
    }

    @Override
    public void finalizarServico() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'finalizarServico'");
    }

    @Override
    public void servExiste() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'servExiste'");
    }

    @Override
    public void notificarCliente() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'notificarCliente'");
    }

    @Override
    public void registoCheckUp() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'registoCheckUp'");
    }

    @Override
    public void registoServicos() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'registoServicos'");
    }

    @Override
    public void adicionarAgendados() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'adicionarAgendados'");
    }

    @Override
    public void iniciarCheckUp() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'iniciarCheckUp'");
    }

    @Override
    public void finalizarCheckUp() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'finalizarCheckUp'");
    }

    @Override
    public void emExecucao() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'emExecucao'");
    }

    @Override
    public void iniciarTurno() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'iniciarTurno'");
    }

    @Override
    public void finalizarTurno() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'finalizarTurno'");
    }

    @Override
    public void funcExiste() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'funcExiste'");
    }

    @Override
    public void atrasoFunc() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'atrasoFunc'");
    }

    @Override
    public void addServ() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addServ'");
    }

}