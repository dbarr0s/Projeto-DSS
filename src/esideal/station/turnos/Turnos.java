package esideal.station.turnos;

import java.time.LocalDateTime;
import java.util.Objects;

public class Turnos {
    private int NumTurno;
    private int cartaoFuncionario;
    private LocalDateTime horaEntrada;
    private LocalDateTime horaSaida;

    /*CONSTRUTORES*/

    public Turnos(int NumTurno, int cartaoFuncionario, LocalDateTime horaEntrada, LocalDateTime horaSaida){
        this.NumTurno = NumTurno;
        this.cartaoFuncionario = cartaoFuncionario;
        this.horaEntrada = horaEntrada;
        this.horaSaida = horaSaida;
    }

    public Turnos(Turnos t) {
        this.NumTurno = t.getNumTurno();
        this.cartaoFuncionario = t.getCartaoFuncionario();
        this.horaEntrada = t.getHoraEntrada();
        this.horaSaida = t.getHoraSaida();
    }

    /*GETTERS E SETTERS*/

    public int getNumTurno(){
        return this.NumTurno;
    }

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

    /*OUTROS MÉTODOS*/

    public Turnos clone(){
        return new Turnos(this);
    }

    public boolean equals(Object o){
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Turnos t = (Turnos) o;
        return NumTurno == t.NumTurno &&
        cartaoFuncionario == t.cartaoFuncionario &&
        Objects.equals(horaEntrada, t.horaEntrada) &&
        Objects.equals(horaSaida, t.horaSaida);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ID de Turno: ").append(NumTurno).append("\n");
        sb.append("ID de Funcionário: ").append(cartaoFuncionario).append("\n");
        sb.append("Hora de entrada do turno: ").append(horaEntrada).append("\n");
        sb.append("Hora de saída do turno: ").append(horaSaida).append("\n");
        return sb.toString();
    }
}
