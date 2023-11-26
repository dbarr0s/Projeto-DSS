package esideal.station.checkup;

import java.time.LocalDateTime;
import java.util.Map;

import esideal.station.funcionario.Funcionario;
import esideal.station.servico.Estado;
import esideal.station.servico.Servico;
import esideal.station.veiculo.Veiculo;

public interface ICheckUp {
    void iniciarCheckUp(int numCheckUp, LocalDateTime data);
    void finalizarCheckUp(int numCheckUp);
    void criarNovoCheckUp(int numCheckUp, Veiculo veiculo, LocalDateTime dataCheckUp, Funcionario funcionario, Estado estado, Map<String, Servico> servicosAExecutar);
}
