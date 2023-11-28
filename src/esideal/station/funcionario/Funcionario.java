package esideal.station.funcionario;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;

import esideal.station.checkup.CheckUp;
import esideal.station.servico.Servico;
import esideal.station.servico.TipoServico;

public class Funcionario {
    private int cartaoFuncionario;
    private EstadoTurno estadoTurno;
    private LocalDateTime horaEntrada;
    private LocalDateTime horaSaida;
    private TipoFuncionario tipoFuncionario;
    private TipoServico postosMecanico;
    private Map<Integer, Servico> servDoDia;
    private Map<Integer, CheckUp> checkUpDoDia;

    /*CONSTRUTORES*/

    public Funcionario(int cartaoFuncionario, EstadoTurno estadoTurno, LocalDateTime horaEntrada, LocalDateTime horaSaida, TipoFuncionario tipoFuncionario, TipoServico postosMecanico, Map<Integer, Servico> servDoDia, Map<Integer, CheckUp> checkUpDoDia){
        this.cartaoFuncionario = cartaoFuncionario;
        this.estadoTurno = estadoTurno;
        this.horaEntrada = horaEntrada;
        this.horaSaida = horaSaida;
        this.tipoFuncionario = tipoFuncionario;
        this.postosMecanico = postosMecanico;
        this.servDoDia = servDoDia;
        this.checkUpDoDia = checkUpDoDia;
    }

    public Funcionario(Funcionario f) {
        this.cartaoFuncionario = f.getCartaoFuncionario();
        this.estadoTurno = f.getEstadoTurno();
        this.horaEntrada = f.getHoraEntrada();
        this.horaSaida = f.getHoraSaida();
        this.tipoFuncionario = f.getTipoFuncionario();
        this.postosMecanico = f.getPostosMecanico();
        this.servDoDia = f.getServDoDia();
        this.checkUpDoDia = f.getCheckUpDoDia();
    }

    /*GETTERS E SETTERS*/

    public int getCartaoFuncionario(){
        return this.cartaoFuncionario;
    }

    public EstadoTurno getEstadoTurno(){
        return this.estadoTurno;
    }

    public void setEstadoTurno(EstadoTurno estadoTurno){
        this.estadoTurno = estadoTurno;
    }

    public LocalDateTime getHoraEntrada(){
        return this.horaEntrada;
    }

    public LocalDateTime getHoraSaida(){
        return this.horaSaida;
    }
    
    public void setHoraSaida(LocalDateTime novaHoraSaida) {
        this.horaSaida = novaHoraSaida;
    }

    public TipoFuncionario getTipoFuncionario(){
        return this.tipoFuncionario;
    }

    public TipoServico getPostosMecanico(){
        return this.postosMecanico;
    }

    public Map<Integer, Servico> getServDoDia(){
        return this.servDoDia;
    }

    public Map<Integer, CheckUp> getCheckUpDoDia() {
        return this.checkUpDoDia;
    }

    /*OUTROS MÉTODOS*/

    public Funcionario clone(){
        return new Funcionario(this);
    }

    public boolean equals(Object o){
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Funcionario f = (Funcionario) o;
        return cartaoFuncionario == f.cartaoFuncionario &&
        estadoTurno == f.estadoTurno &&
        Objects.equals(horaEntrada, f.horaEntrada) &&
        Objects.equals(horaSaida, f.horaSaida) &&
        tipoFuncionario == f.tipoFuncionario && 
        postosMecanico == f.postosMecanico;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ID de Funcionário: ").append(cartaoFuncionario).append("\n");
        sb.append("Turno do Funcionário: ").append(estadoTurno).append("\n");
        sb.append("Hora de entrada do turno: ").append(horaEntrada).append("\n");
        sb.append("Hora de saída do turno: ").append(horaSaida).append("\n");
        sb.append("Tipo de Funcionário: ").append(tipoFuncionario).append("\n");
        sb.append("Tipo de Mecânico: ").append(postosMecanico).append("\n");
        sb.append("Serviços do Dia: ").append(servDoDia).append("\n");
        sb.append("Check-up do Dia: ").append(checkUpDoDia).append("\n");
        return sb.toString();
    }
}
