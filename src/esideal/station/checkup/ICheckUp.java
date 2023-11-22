package esideal.station.checkup;

import java.time.LocalDateTime;
import esideal.station.veiculo.Veiculo;

public interface ICheckUp {
    void iniciarCheckUp(Veiculo veiculo);
    void finalizarCheckUp(Veiculo veiculo);
}
