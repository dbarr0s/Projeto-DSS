package esideal.station.funcionario;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;

import esideal.station.servico.Servico;

public class Funcionario {
    private int cartaoFuncionario;
    private LocalDateTime horaEntrada;
    private LocalDateTime horaSaida;
    private TipoFuncionario tipoFuncionario;
    private Map<Integer, Servico> servDoDia;

    /*CONSTRUTORES*/

    public Funcionario(int cartaoFuncionario, LocalDateTime horaEntrada, LocalDateTime horaSaida, TipoFuncionario tipoFuncionario, Map<Integer, Servico> servDoDia){
        this.cartaoFuncionario = cartaoFuncionario;
        this.horaEntrada = horaEntrada;
        this.horaSaida = horaSaida;
        this.tipoFuncionario = tipoFuncionario;
        this.servDoDia = servDoDia;
    }

    public Funcionario(Funcionario f) {
        this.cartaoFuncionario = f.getCartaoFuncionario();
        this.horaEntrada = f.getHoraEntrada();
        this.horaSaida = f.getHoraSaida();
        this.tipoFuncionario = f.getTipoFuncionario();
        this.servDoDia = f.getServDoDia();
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

    public TipoFuncionario getTipoFuncionario(){
        return this.tipoFuncionario;
    }

    public Map<Integer, Servico> getServDoDia(){
        return this.servDoDia;
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
        tipoFuncionario == f.tipoFuncionario;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ID de Funcionário: ").append(cartaoFuncionario).append("\n");
        sb.append("Hora de entrada do turno: ").append(horaEntrada).append("\n");
        sb.append("Hora de saída do turno: ").append(horaSaida).append("\n");
        sb.append("Tipo de Funcionário: ").append(tipoFuncionario).append("\n");
        return sb.toString();
    }
}
