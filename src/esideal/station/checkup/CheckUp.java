package esideal.station.checkup;

import java.time.LocalDateTime;
import java.util.Objects;

import esideal.station.servico.Estado;

public class CheckUp {
    private int numeroCheckUp;
    private int numFicha;
    private int funcResponsavel;
    private String matricula;
    private LocalDateTime dataCheckUp;
    private LocalDateTime datafim;
    private Estado estado;

    /*CONSTRUTORES*/

    public CheckUp(int numeroCheckUp, int numFicha,int funcResponsavel, String matricula, LocalDateTime dataCheckUp, LocalDateTime datafim, Estado estado) {
        this.numeroCheckUp = numeroCheckUp;
        this.numFicha = numFicha;
        this.funcResponsavel = funcResponsavel;
        this.matricula = matricula;
        this.dataCheckUp = dataCheckUp;
        this.datafim = datafim;
        this.estado = estado;
    }

    public CheckUp(CheckUp c) {
        this.numeroCheckUp = c.getNumCheckUp();
        this.numFicha = c.getNumFicha();
        this.funcResponsavel = c.getFuncResponsavel();
        this.matricula = c.getMatricula();
        this.dataCheckUp = c.getDataCheckUp();
        this.datafim = c.getDataFim();
        this.estado = c.getEstado();
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

    public LocalDateTime getDataFim(){
        return this.datafim;
    }

    public void setDataFim(LocalDateTime horario) {
        this.datafim = horario;
    }

    public Estado getEstado(){
        return this.estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
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
        Objects.equals(datafim, c.datafim) &&
        funcResponsavel == c.funcResponsavel && 
        estado == c.estado;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Nº do Check-Up: ").append(numeroCheckUp).append("\n");
        sb.append("Nº da Ficha a que pertence: ").append(numFicha).append("\n");
        sb.append("Check-Up para Veículo: ").append(matricula).append("\n");
        sb.append("Data do Check-Up: ").append(dataCheckUp).append("\n");
        sb.append("Data Final do Check-Up: ").append(datafim).append("\n");
        sb.append("Funcionário Responsável: ").append(funcResponsavel).append("\n");
        sb.append("Estado: ").append(estado).append("\n");
        return sb.toString();
    }
}
