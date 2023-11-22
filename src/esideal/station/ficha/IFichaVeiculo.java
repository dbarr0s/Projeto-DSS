package esideal.station.ficha;

import java.time.LocalDateTime;

import esideal.station.funcionario.Funcionario;
import esideal.station.veiculo.Veiculo;

public interface IFichaVeiculo {
    void registoCheckUp(Veiculo veiculo, LocalDateTime dataRegisto, Funcionario funcionario);
    void registoServicos(Veiculo veiculo, LocalDateTime dataRegisto, Funcionario funcionario);
}
