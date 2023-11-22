package esideal.station.ficha;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;

import esideal.station.checkup.CheckUp;
import esideal.station.cliente.Cliente;
import esideal.station.funcionario.Funcionario;
import esideal.station.servico.Servico;
import esideal.station.veiculo.Veiculo;

public class FichaVeiculo {
    private Cliente cliente;
    private Veiculo veiculo;
    private Funcionario funcionario;
    private Map<String, Servico> servExecutados;
    private Map<String, CheckUp> checkupExecutados;

    public FichaVeiculo(Cliente cliente, Veiculo veiculo, Funcionario funcionario, Map<String, Servico> servExecutados, Map<String, CheckUp> checkupExecutados){
        this.cliente = cliente;
        this.veiculo = veiculo;
        this.funcionario = funcionario;
        this.servExecutados = servExecutados;
        this.checkupExecutados = checkupExecutados;
    }

    public FichaVeiculo(FichaVeiculo f){
        this.cliente = f.getCliente();
        this.veiculo = f.getVeiculo();
        this.funcionario = f.getFuncionario();
        this.servExecutados = f.getServExecutados();
        this.checkupExecutados = f.getCheckupExecutados();
    }

    public Cliente getCliente(){
        return this.cliente;
    }

    public Veiculo getVeiculo(){
        return this.veiculo;
    }

    public Funcionario getFuncionario(){
        return this.funcionario;
    }

    public Map<String, Servico> getServExecutados(){
        return this.servExecutados;
    }

    public Map<String, CheckUp> getCheckupExecutados(){
        return this.checkupExecutados;
    }

    public FichaVeiculo clone(){
        return new FichaVeiculo(this);
    }

    public boolean equals(Object o){
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FichaVeiculo f = (FichaVeiculo) o;
        return cliente == f.cliente &&
        veiculo == f.veiculo &&
        funcionario == f.funcionario;
    }
}
