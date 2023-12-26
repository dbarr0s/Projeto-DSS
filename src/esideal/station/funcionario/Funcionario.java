package esideal.station.funcionario;

import java.time.LocalDateTime;
import java.util.Objects;

import esideal.station.servico.*;

public class Funcionario {
    private int cartaoFuncionario;
    private LocalDateTime horaEntrada;
    private LocalDateTime horaSaida;
    private TipoFuncionario tipoFuncionario;
    private TipoServico postosMecanico;

    /*CONSTRUTORES*/

    public Funcionario(int cartaoFuncionario, LocalDateTime horaEntrada, LocalDateTime horaSaida, TipoFuncionario tipoFuncionario, TipoServico postosMecanico){
        this.cartaoFuncionario = cartaoFuncionario;
        this.horaEntrada = horaEntrada;
        this.horaSaida = horaSaida;
        this.tipoFuncionario = tipoFuncionario;
        this.postosMecanico = postosMecanico;
    }

    public Funcionario(Funcionario f) {
        this.cartaoFuncionario = f.getCartaoFuncionario();
        this.horaEntrada = f.getHoraEntrada();
        this.horaSaida = f.getHoraSaida();
        this.tipoFuncionario = f.getTipoFuncionario();
        this.postosMecanico = f.getPostosMecanico();
    }

    /*GETTERS E SETTERS*/

    public int getCartaoFuncionario(){
        return this.cartaoFuncionario;
    }

    public LocalDateTime getHoraEntrada(){
        return this.horaEntrada;
    }

    public LocalDateTime getHoraSaida(){
        return this.horaSaida;
    }
    
    public void setHoraSaida(LocalDateTime novaHoraSaida) {
        this.horaSaida = novaHoraSaida;
    }

    public TipoFuncionario getTipoFuncionario(){
        return this.tipoFuncionario;
    }

    public TipoServico getPostosMecanico(){
        return this.postosMecanico;
    }

    /*OUTROS MÉTODOS*/

    public Funcionario clone(){
        return new Funcionario(this);
    }

    public boolean equals(Object o){
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Funcionario f = (Funcionario) o;
        return cartaoFuncionario == f.cartaoFuncionario &&
        Objects.equals(horaEntrada, f.horaEntrada) &&
        Objects.equals(horaSaida, f.horaSaida) &&
        tipoFuncionario == f.tipoFuncionario && 
        postosMecanico == f.postosMecanico;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ID de Funcionário: ").append(cartaoFuncionario).append("\n");
        sb.append("Hora de entrada do turno: ").append(horaEntrada).append("\n");
        sb.append("Hora de saída do turno: ").append(horaSaida).append("\n");
        sb.append("Tipo de Funcionário: ").append(tipoFuncionario).append("\n");
        sb.append("Tipo de Mecânico: ").append(postosMecanico).append("\n");
        return sb.toString();
    }
}
