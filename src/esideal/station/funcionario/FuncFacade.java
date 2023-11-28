package esideal.station.funcionario;

import java.time.LocalDateTime;
import java.util.Map;

import esideal.data.FuncionarioDAO;


public class FuncFacade implements IFuncionário{
    private Map<Integer, Funcionario> funcionarios; 

    public FuncFacade(){
        this.funcionarios = FuncionarioDAO.getInstance(); 
    }

    public Map<Integer, Funcionario> getFuncionarios() {
        return this.funcionarios;
    }

    public boolean funcionarioExiste(int cartaoFuncionario) {
        for (Funcionario funcionario : funcionarios.values()) {
            if (funcionario.getCartaoFuncionario() == cartaoFuncionario) {
                return true;
            }
        }
        return false;
    }  

    public void iniciarTurno(int cartaoFuncionario, LocalDateTime horaEntrada) {
        Funcionario funcionario = funcionarios.get(cartaoFuncionario);
        if (funcionario != null) {

        LocalDateTime horaPrevista = funcionario.getHoraEntrada();

        if (horaEntrada.isAfter(horaPrevista)) {
            System.out.println("Aviso de atraso para o funcionário " + cartaoFuncionario + ". Hora de entrada prevista: " + horaPrevista);
            penalizarAtraso(cartaoFuncionario, horaEntrada);
            funcionario.setEstadoTurno(EstadoTurno.INICIADO);
        }
        funcionario.setEstadoTurno(EstadoTurno.INICIADO);
        System.out.println("Turno iniciado para o funcionário " + cartaoFuncionario + " às " + horaEntrada);
        }
    }

    public void penalizarAtraso(int cartaoFuncionario, LocalDateTime horaEntrada) {
        Funcionario funcionario = funcionarios.get(cartaoFuncionario);
        if (funcionario != null) {
            LocalDateTime horaPrevista = funcionario.getHoraEntrada();
    
            long minutosAtraso = java.time.Duration.between(horaPrevista, horaEntrada).toMinutes();
    
            if (minutosAtraso > 0) {
                LocalDateTime novaHoraSaida = funcionario.getHoraSaida().plusMinutes(minutosAtraso);
                System.out.println("Funcionário " + cartaoFuncionario + " penalizado com " + minutosAtraso + " minutos de atraso. Nova hora de saída: " + novaHoraSaida);
            } else {
                System.out.println("Funcionário " + cartaoFuncionario + " chegou pontualmente. Sem penalidades.");
            }
        } else {
            System.out.println("Funcionário com cartão " + cartaoFuncionario + " não encontrado.");
        }
    }
    
    public void finalizarTurno(int cartaoFuncionario, LocalDateTime horaEntrada, LocalDateTime horaSaida) {
        Funcionario funcionario = funcionarios.get(cartaoFuncionario);
        if (funcionario != null) {
            LocalDateTime horaPrevista = funcionario.getHoraEntrada();

             if (horaEntrada.isAfter(horaPrevista)) {
                    System.out.println("Aviso de atraso para o funcionário " + cartaoFuncionario + ". Hora de entrada prevista: " + horaPrevista);
                    penalizarAtraso(cartaoFuncionario, horaEntrada);
                    funcionario.setEstadoTurno(EstadoTurno.TERMINADO);
            }
            funcionario.setEstadoTurno(EstadoTurno.TERMINADO);
            System.out.println("Turno finalizado para o funcionário " + cartaoFuncionario + " às " + horaSaida);
        }
    }
}
