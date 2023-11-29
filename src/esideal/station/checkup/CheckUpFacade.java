package esideal.station.checkup;

import java.time.LocalDateTime;
import java.util.Map;

import esideal.data.CheckUpDAO;
import esideal.station.funcionario.FuncFacade;
import esideal.station.funcionario.Funcionario;
import esideal.station.funcionario.TipoFuncionario;
import esideal.station.servico.Estado;
import esideal.station.servico.Servico;

public class CheckUpFacade implements ICheckUp{
    private Map<Integer, CheckUp> checkups;  

    public CheckUpFacade(){
        this.checkups = CheckUpDAO.getInstance(); 
    }

    public void criarNovoCheckUpEAgendar(int numCheckUp, int numFicha, int funcResponsavel, String matricula, LocalDateTime dataCheckUp, Estado estado, Map<Integer, Servico> servicosAExecutar, FuncFacade f) {
        Funcionario mecanicoMenosOcupado = null;
        int menorNumeroServicos = 0;

        for (Funcionario f1 : f.getFuncionarios().values()) {
            if (f1.getTipoFuncionario() == TipoFuncionario.MECANICO) {
                int numeroServicos = f1.getServDoDia().size();
                int numeroCheckUp = f1.getCheckUpDoDia().size();
                int total = numeroCheckUp + numeroServicos;

                if (total < menorNumeroServicos) {
                    menorNumeroServicos = total;
                    mecanicoMenosOcupado = f1;
                }
            }
        }

        if (mecanicoMenosOcupado != null) {
            CheckUp novoCheckUp = new CheckUp(numCheckUp, numFicha,funcResponsavel, matricula, dataCheckUp, estado, servicosAExecutar);
            checkups.put(numCheckUp, novoCheckUp);

            novoCheckUp.setDataCheckUp(dataCheckUp);
            checkups.put(novoCheckUp.getNumCheckUp(), novoCheckUp);
            mecanicoMenosOcupado.getCheckUpDoDia().put(numCheckUp, novoCheckUp);

            System.out.println("Novo check-up criado e serviços agendados com sucesso com número: " + numCheckUp);
        } else {
            System.out.println("Não foi possível encontrar um mecânico disponível para o check-up.");
        }
    }

    //ALTERAR//

    public void iniciarCheckUp(int numCheckUp, LocalDateTime data){
        for(Map.Entry<Integer, CheckUp> entry : checkups.entrySet()){
            if (entry.getKey() == numCheckUp && entry.getValue().getDataCheckUp() == data) {
                CheckUp c = checkups.get(numCheckUp);
                c.setEstado(Estado.EM_ANDAMENTO);
                checkups.put(c.getNumCheckUp(), c);
                System.out.println("Check-up " + numCheckUp + " iniciado com sucesso.");
            } else {
                System.out.println("O check-up com o número " + numCheckUp + " não foi encontrado.");
            }
        }
    }

    public void finalizarCheckUp(int numCheckUp, Map<Integer, Servico> servAExecutar){
        for(Map.Entry<Integer, CheckUp> entry : checkups.entrySet()){
            if (entry.getKey() == numCheckUp) {
                CheckUp c = checkups.get(numCheckUp);
                c.setEstado(Estado.CONCLUÍDO);
                for (Servico s : servAExecutar.values()) {
                    c.getServAExecutar().put(s.getNumServiço(), s.clone());
                }
                checkups.put(c.getNumCheckUp(), c);
                System.out.println("Check-up " + numCheckUp + " finalizado com sucesso.");
            } else {
                System.out.println("O check-up com o número " + numCheckUp + " não foi encontrado.");
            }
        }
    }

    public Map<Integer, CheckUp> getCheckUps() {
        return this.checkups;
    }
}
