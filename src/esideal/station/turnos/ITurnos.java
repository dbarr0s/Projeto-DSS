package esideal.station.turnos;

import java.time.LocalDateTime;
import java.util.Map;

public interface ITurnos {
    void iniciarTurno(int NumTurno, int cartaoFuncionario, LocalDateTime horaEntrada);
    void finalizarTurno(int NumTurno, int cartaoFuncionario, LocalDateTime horaSaida);
    Map<Integer, Turnos> getTurnos();
}
