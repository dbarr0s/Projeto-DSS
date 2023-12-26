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

    /**
     * Verifica se uma ficha com um número específico está registada no sistema.
     * @param numeroFicha Número da ficha a ser verificado.
     * @return Verdadeiro se a ficha estiver registada; falso, caso contrário.
     */
    
    public boolean existeFicha(int numeroFicha) {
        return fichas.containsKey(numeroFicha);
    }
}
