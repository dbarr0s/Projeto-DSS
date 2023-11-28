package esideal.station.checkup;

import java.time.LocalDateTime;
import java.util.Map;

import esideal.station.funcionario.FuncFacade;
import esideal.station.servico.Estado;
import esideal.station.servico.Servico;

public interface ICheckUp {
    void iniciarCheckUp(int numCheckUp, LocalDateTime data);
    void finalizarCheckUp(int numCheckUp, Map<Integer, Servico> servAExecutar);
    void criarNovoCheckUpEAgendar(int numCheckUp, int funcResponsavel, String matricula, LocalDateTime dataCheckUp, Estado estado, Map<Integer, Servico> servicosAExecutar, FuncFacade f);
}