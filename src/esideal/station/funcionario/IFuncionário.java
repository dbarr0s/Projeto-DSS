package esideal.station.funcionario;

import java.time.LocalDateTime;

public interface IFuncionário {
    void iniciarTurno(int cartaoFuncionario, LocalDateTime horaEntrada);
    void finalizarTurno(int cartaoFuncionario, LocalDateTime horaEntrada, LocalDateTime horaSaida);
    void penalizarAtraso(int cartaoFuncionario, LocalDateTime horaEntrada); 
    boolean funcionarioExiste(int cartaoFuncionario);
}
