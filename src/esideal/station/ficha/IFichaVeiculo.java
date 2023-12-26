package esideal.station.ficha;

import java.util.Map;

public interface IFichaVeiculo {
    boolean existeFicha(int numeroFicha);
    Map<Integer, FichaVeiculo> getFichas();
}
