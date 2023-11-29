package esideal.station.checkup;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;

import esideal.station.servico.Estado;
import esideal.station.servico.Servico;

public class CheckUp {
    private int numeroCheckUp;
    private int numFicha;
    private int funcResponsavel;
    private String matricula;
    private LocalDateTime dataCheckUp;
    private Estado estado;
    private Map<Integer, Servico> servAExecutar; 

    /*CONSTRUTORES*/

    public CheckUp(int numeroCheckUp, int numFicha,int funcResponsavel, String matricula, LocalDateTime dataCheckUp, Estado estado, Map<Integer, Servico> servAExecutar) {
        this.numeroCheckUp = numeroCheckUp;
        this.numFicha = numFicha;
        this.funcResponsavel = funcResponsavel;
        this.matricula = matricula;
        this.dataCheckUp = dataCheckUp;
        this.estado = estado;
        this.servAExecutar = servAExecutar;
    }

    public CheckUp(CheckUp c) {
        this.numeroCheckUp = c.getNumCheckUp();
        this.numFicha = c.getNumFicha();
        this.funcResponsavel = c.getFuncResponsavel();
        this.matricula = c.getMatricula();
        this.dataCheckUp = c.getDataCheckUp();
        this.estado = c.getEstado();
        this.servAExecutar = c.getServAExecutar();
    }

    /*GETTERS*/

    public int getNumCheckUp(){
        return this.numeroCheckUp;
    }

    public int getNumFicha() {
        return this.numFicha;
    }

    public int getFuncResponsavel(){
        return this.funcResponsavel;
    }

    public String getMatricula(){
        return this.matricula;
    }

    public LocalDateTime getDataCheckUp(){
        return this.dataCheckUp;
    }

    public void setDataCheckUp(LocalDateTime horario) {
        this.dataCheckUp = horario;
    }

    public Estado getEstado(){
        return this.estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public Map<Integer, Servico> getServAExecutar(){
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
        return numeroCheckUp == c.numeroCheckUp &&
        numFicha == c.numFicha &&
        matricula == c.matricula &&
        Objects.equals(dataCheckUp, c.dataCheckUp) &&
        funcResponsavel == c.funcResponsavel && 
        estado == c.estado;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Nº do Check-Up: ").append(numeroCheckUp).append("\n");
        sb.append("Nº da Ficha a que pertence: ").append(numFicha).append("\n");
        sb.append("Check-Up para Veículo: ").append(matricula).append("\n");
        sb.append("Data do Check-Up: ").append(dataCheckUp).append("\n");
        sb.append("Funcionário Responsável: ").append(funcResponsavel).append("\n");
        sb.append("Estado: ").append(estado).append("\n");
        sb.append("Serviços a Executar: ").append(servAExecutar.keySet()).append("\n");
        return sb.toString();
    }
}
