package esideal.station.turnos;

import java.time.LocalDateTime;
import java.util.Map;

import esideal.data.TurnosDAO;
import esideal.station.funcionario.EstadoTurno;
import esideal.station.funcionario.FuncFacade;
import esideal.station.funcionario.Funcionario;

public class TurnosFacade implements ITurnos{
    private Map<Integer, Turnos> turnos; 

    public TurnosFacade(){
        this.turnos = TurnosDAO.getInstance(); 
    }

    public Map<Integer, Turnos> getTurnos() {
        return this.turnos;
    }

    public void iniciarTurno(int numTurno, int cartaoFuncionario, LocalDateTime horaEntrada) {
        FuncFacade f = new FuncFacade();

        Funcionario funcionario = f.getFuncionarios().get(cartaoFuncionario);
        if (funcionario != null) {
            funcionario.setEstadoTurno(EstadoTurno.INICIADO);
            System.out.println("Turno iniciado para o funcionário " + cartaoFuncionario + " às " + horaEntrada);
            
            Turnos novoTurno = new Turnos(numTurno,cartaoFuncionario, horaEntrada, null);
            turnos.put(cartaoFuncionario, novoTurno);
        }
    }

    public void finalizarTurno(int numTurno, int cartaoFuncionario, LocalDateTime horaSaida) {
        FuncFacade f = new FuncFacade();

        Funcionario funcionario = f.getFuncionarios().get(cartaoFuncionario);
        if (funcionario != null) {
            funcionario.setEstadoTurno(EstadoTurno.TERMINADO);
            TurnosDAO turnosDAO = TurnosDAO.getInstance();
            turnosDAO.updateHoraSaida(cartaoFuncionario, horaSaida);
            System.out.println("Turno finalizado para o funcionário " + cartaoFuncionario + " às " + horaSaida);
        }
    }
}
