package esideal.station.checkup;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;

import esideal.station.funcionario.Funcionario;
import esideal.station.servico.Estado;
import esideal.station.servico.Servico;
import esideal.station.veiculo.Veiculo;

public class CheckUp {
    private int numeroCheckUp;
    private Veiculo veiculo;
    private LocalDateTime dataCheckUp;
    private Funcionario funcionario;
    private Estado estado;
    private Map<String, Servico> servAExecutar; 

    /*CONSTRUTORES*/

    public CheckUp(int numeroCheckUp, Veiculo veiculo, LocalDateTime dataCheckUp, Funcionario funcionario, Estado estado, Map<String, Servico> servAExecutar) {
        this.numeroCheckUp = numeroCheckUp;
        this.veiculo = veiculo;
        this.dataCheckUp = dataCheckUp;
        this.funcionario = funcionario;
        this.estado = estado;
        this.servAExecutar = servAExecutar;
    }

    public CheckUp(CheckUp c) {
        this.numeroCheckUp = c.getNumCheckUp();
        this.veiculo = c.getVeiculo();
        this.dataCheckUp = c.getDataCheckUp();
        this.funcionario = c.getFuncionario();
        this.estado = c.getEstado();
        this.servAExecutar = c.getServAExecutar();
    }

    /*GETTERS*/

    public int getNumCheckUp(){
        return this.numeroCheckUp;
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

    /*OUTROS MÉTODOS*/

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

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Nº do Check-Up: ").append(numeroCheckUp).append("\n");
        sb.append("Check-Up para Veículo: ").append(veiculo).append("\n");
        sb.append("Data do Check-Up: ").append(dataCheckUp).append("\n");
        sb.append("Funcionário Responsável: ").append(funcionario).append("\n");
        sb.append("Estado: ").append(estado).append("\n");
        sb.append("Serviços a Executar: ").append(servAExecutar.keySet()).append("\n");
        return sb.toString();
    }
}
