package esideal.station.ficha;

import java.util.Map;

import esideal.data.FichaDAO;

public class FichaFacade implements IFichaVeiculo{
    private Map<Integer, FichaVeiculo> fichas;  

    public FichaFacade(){
        this.fichas = FichaDAO.getInstance(); 
    }

    public Map<Integer, FichaVeiculo> getFichas() {
        return this.fichas;
    }
    
    public boolean existeFicha(int numeroFicha) {
        return fichas.containsKey(numeroFicha);
    }
}
