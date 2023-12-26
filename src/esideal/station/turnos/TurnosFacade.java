package esideal.station.turnos;

import java.time.LocalDateTime;
import java.util.Map;

import esideal.data.TurnosDAO;
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

    /**
     * Inicia um turno para um funcionário especificado.
     * @param numTurno Número do turno a ser iniciado.
     * @param cartaoFuncionario Cartão de identificação do funcionário.
     * @param horaEntrada Horário de entrada do funcionário no turno.
     */

    public void iniciarTurno(int numTurno, int cartaoFuncionario, LocalDateTime horaEntrada) {
        FuncFacade f = new FuncFacade();

        Funcionario funcionario = f.getFuncionarios().get(cartaoFuncionario);
        if (funcionario != null) {
            System.out.println("Turno iniciado para o funcionário " + cartaoFuncionario + " às " + horaEntrada);
            
            Turnos novoTurno = new Turnos(numTurno,cartaoFuncionario, horaEntrada, null);
            turnos.put(cartaoFuncionario, novoTurno);
        }
    }

    /**
     * Finaliza um turno para um funcionário especificado.
     * @param numTurno Número do turno a ser finalizado.
     * @param cartaoFuncionario Cartão de identificação do funcionário.
     * @param horaSaida Horário de saída do funcionário do turno.
     */

    public void finalizarTurno(int numTurno, int cartaoFuncionario, LocalDateTime horaSaida) {
        FuncFacade f = new FuncFacade();

        Funcionario funcionario = f.getFuncionarios().get(cartaoFuncionario);
        if (funcionario != null) {
            TurnosDAO turnosDAO = TurnosDAO.getInstance();
            turnosDAO.updateHoraSaida(cartaoFuncionario, horaSaida);
            System.out.println("Turno finalizado para o funcionário " + cartaoFuncionario + " às " + horaSaida);
        }
    }
}
