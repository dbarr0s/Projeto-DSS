package esideal.station.checkup;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;

import esideal.station.funcionario.Funcionario;
import esideal.station.servico.Estado;
import esideal.station.servico.Servico;
import esideal.station.veiculo.Veiculo;

public class CheckUp {
    private Veiculo veiculo;
    private LocalDateTime dataCheckUp;
    private Funcionario funcionario;
    private Estado estado;
    private Map<String, Servico> servAExecutar; 

    public CheckUp(Veiculo veiculo, LocalDateTime dataCheckUp, Funcionario funcionario, Estado estado, Map<String, Servico> servAExecutar) {
        this.veiculo = veiculo;
        this.dataCheckUp = dataCheckUp;
        this.funcionario = funcionario;
        this.estado = estado;
        this.servAExecutar = servAExecutar;
    }

    public CheckUp(CheckUp c) {
        this.veiculo = c.getVeiculo();
        this.dataCheckUp = c.getDataCheckUp();
        this.funcionario = c.getFuncionario();
        this.estado = c.getEstado();
        this.servAExecutar = c.getServAExecutar();
    }

    public Veiculo getVeiculo(){
        return this.veiculo;
    }

    public LocalDateTime getDataCheckUp(){
        return this.dataCheckUp;
    }

    public Funcionario getFuncionario(){
        return this.funcionario;
    }

    public Estado getEstado(){
        return this.estado;
    }

    public Map<String, Servico> getServAExecutar(){
        return this.servAExecutar;
    }

    public CheckUp clone() {
        return new CheckUp(this);
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CheckUp c = (CheckUp) o;
        return veiculo == c.veiculo &&
        Objects.equals(dataCheckUp, c.dataCheckUp) &&
        funcionario == c.funcionario && 
        estado == c.estado;
    }
}
