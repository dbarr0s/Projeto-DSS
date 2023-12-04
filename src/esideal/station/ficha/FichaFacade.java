package esideal.station.ficha;

import java.util.Map;

import esideal.data.FichaDAO;
import esideal.station.checkup.CheckUp;
import esideal.station.checkup.CheckUpFacade;
import esideal.station.servico.Estado;
import esideal.station.servico.Servico;
import esideal.station.servico.ServicoFacade;

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
   
    public void atualizarEstadoCheckUp(int numeroFicha, int numCheckUp, Estado novoEstado) {
        FichaVeiculo ficha = fichas.get(numeroFicha);
        if (ficha != null) {
            CheckUp checkUp = ficha.getCheckups().get(numCheckUp);
            if (checkUp != null) {
                checkUp.setEstado(novoEstado);
                System.out.println("Estado do check-up atualizado com sucesso.");
            } else {
                System.out.println("O check-up não está presente na ficha do veículo.");
            }
        } else {
            System.out.println("A ficha do veículo não existe.");
        }
    }

    public void atualizarEstadoServico(int numeroFicha, int numServico, Estado novoEstado) {
        FichaVeiculo ficha = fichas.get(numeroFicha);
        if (ficha != null) {
            Servico s = ficha.getServicos().get(numServico);
            if (s != null) {
                s.setEstado(novoEstado);
                System.out.println("Estado do serviço atualizado com sucesso.");
            } else {
                System.out.println("O serviço não está presente na ficha do veículo.");
            }
        } else {
            System.out.println("A ficha do veículo não existe.");
        }
    }
    
    public void registoCheckUpFicha(int numeroFicha, CheckUp checkUp, CheckUpFacade c) {
        FichaVeiculo ficha = fichas.get(numeroFicha);
        if (ficha == null) {
            System.out.println("A ficha do veículo não existe.");
            return;
        }

        if (c.getCheckUps().containsKey(checkUp.getNumCheckUp()) && ficha.getCheckups().containsKey(checkUp.getNumCheckUp())) {
            System.out.println("O check-up já foi registado na ficha do veículo.");
            return;
        }

        c.getCheckUps().put(checkUp.getNumCheckUp(), checkUp);
        ficha.getCheckups().put(checkUp.getNumCheckUp(), checkUp);
        System.out.println("Check-up registado na ficha do veículo com sucesso.");
    }

    public void registoServicosFicha(int numeroFicha, Servico s, ServicoFacade sf) { 
        FichaVeiculo ficha = fichas.get(numeroFicha);
        if (ficha == null) {
            System.out.println("A ficha do veículo não existe.");
            return;
        }

        if (sf.getServicos().containsKey(s.getNumServiço()) && ficha.getCheckups().containsKey(s.getNumServiço())) {
            System.out.println("O check-up já foi registrado na ficha do veículo.");
            return;
        }
        
        sf.getServicos().put(s.getNumServiço(), s);
        ficha.getServicos().put(s.getNumServiço(), s);
        System.out.println("Check-up registrado na ficha do veículo com sucesso.");
    }
}
