package esideal.station.funcionario;

import java.time.LocalDateTime;

import esideal.station.servico.Servico;

public interface IFuncionário {
    void iniciarTurno(int cartaoFuncionario, LocalDateTime horaEntrada);
    void finalizarTurno(int cartaoFuncionario, LocalDateTime horaEntrada, LocalDateTime horaSaida);
    void penalizarAtraso(int cartaoFuncionario, LocalDateTime horaEntrada); 
    Funcionario encontrarMecanicoMenosOcupado(Servico servico);
    void addServDoDia();
    void funcionarioExiste();
}
