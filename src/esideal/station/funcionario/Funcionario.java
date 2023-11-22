package esideal.station.funcionario;

import java.time.LocalDateTime;

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
}
