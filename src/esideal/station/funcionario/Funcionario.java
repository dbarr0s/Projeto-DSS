package esideal.station.funcionario;

import java.time.LocalDateTime;
import java.util.Objects;

public class Funcionario {
    private int cartaoFuncionario;
    private LocalDateTime horaEntrada;
    private LocalDateTime horaSaida;
    private TipoFuncionario tipoFuncionario;

    public Funcionario(int cartaoFuncionario, LocalDateTime horaEntrada, LocalDateTime horaSaida, TipoFuncionario tipoFuncionario){
        this.cartaoFuncionario = cartaoFuncionario;
        this.horaEntrada = horaEntrada;
        this.horaSaida = horaSaida;
        this.tipoFuncionario = tipoFuncionario;
    }

    public Funcionario(Funcionario f) {
        this.cartaoFuncionario = f.getCartaoFuncionario();
        this.horaEntrada = f.getHoraEntrada();
        this.horaSaida = f.getHoraSaida();
        this.tipoFuncionario = f.getTipoFuncionario();
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

    public TipoFuncionario getTipoFuncionario(){
        return this.tipoFuncionario;
    }

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
}
