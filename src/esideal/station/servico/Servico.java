package esideal.station.servico;

import java.time.LocalDateTime;
import java.util.Objects;

public class Servico {
    private int numServiço;
    private int numFicha;
    private int funcResponsavel;
    private String matricula;
    private Float custServiço;
    private Estado estado;
    private LocalDateTime horaInicio;
    private LocalDateTime horaFim;
    private String sms;
    private TipoServico tipoServico;

    /*CONSTRUTORES*/
    
    public Servico(int numServiço, int numFicha, int funcResponsavel, String matricula, Float custServiço, Estado estado, LocalDateTime horaInicio, LocalDateTime horaFim, String sms, TipoServico tipoServico) {
        this.numServiço = numServiço;
        this.numFicha = numFicha;
        this.funcResponsavel = funcResponsavel;
        this.matricula = matricula;
        this.custServiço = custServiço;
        this.estado = estado;
        this.horaInicio = horaInicio;
        this.horaFim = horaFim;
        this.sms = sms;
        this.tipoServico = tipoServico;
    }

    public Servico(Servico s) {
        this.numServiço = s.getNumServiço();
        this.numFicha = s.getNumFicha();
        this.funcResponsavel = s.getFuncResponsavel();
        this.matricula = s.getMatricula();
        this.custServiço = s.getCustServiço();
        this.estado = s.getEstado();
        this.horaInicio = s.getHoraInicio();
        this.horaFim = s.getHoraFim();
        this.sms = s.getSms();
        this.tipoServico = s.getTipoServico();
    }

    /*GETTERS*/

    public int getNumServiço(){
        return this.numServiço;
    }

    public int getNumFicha() {
        return this.numFicha;
    }

    public int getFuncResponsavel(){
        return this.funcResponsavel;
    }

    public String getMatricula(){
        return this.matricula;
    }

    public Float getCustServiço(){
        return this.custServiço;
    }

    public Estado getEstado(){
        return this.estado;
    }
    
    public void setEstado(Estado e){
        this.estado = e;
    }

    public LocalDateTime getHoraInicio(){
        return this.horaInicio;
    }

    public void setHoraInicio(LocalDateTime horario){
        this.horaInicio = horario;
    }

    public LocalDateTime getHoraFim(){
        return this.horaFim;
    }

    public void setHoraFim(LocalDateTime horaFim){
        this.horaFim = horaFim;
    }

    public String getSms(){
        return this.sms;
    }

    public void setSms(String sms){
        this.sms = sms;
    }

    public TipoServico getTipoServico(){
        return this.tipoServico;
    }

    /*OUTROS MÉTODOS*/

    public Servico clone() {
        return new Servico(this);
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Servico s = (Servico) o;
        return numServiço == s.numServiço &&
        numFicha == s.numFicha &&
        funcResponsavel == s.funcResponsavel &&
        matricula == s.matricula &&
        Objects.equals(custServiço, s.custServiço) &&
        estado == s.estado &&
        Objects.equals(horaInicio, s.horaInicio) &&
        Objects.equals(horaFim, s.horaFim) &&
        Objects.equals(sms, s.sms) &&
        tipoServico == s.tipoServico;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Nº do Serviço: ").append(numServiço).append("\n");
        sb.append("Nº da Ficha associada: ").append(numFicha).append("\n");
        sb.append("Funcionário responsável: ").append(funcResponsavel).append("\n");
        sb.append("Matrícula do Veículo: ").append(matricula).append("\n");
        sb.append("Custo do Serviço: ").append(custServiço).append("\n");
        sb.append("Estado do serviço: ").append(estado).append("\n");
        sb.append("Hora do ínicio do serviço: ").append(horaInicio).append("\n");
        sb.append("Hora do fim do serviço: ").append(horaFim).append("\n");
        sb.append("SMS: ").append(sms).append("\n");
        sb.append("Tipo de Serviço: ").append(tipoServico).append("\n");
        return sb.toString();
    }
}
