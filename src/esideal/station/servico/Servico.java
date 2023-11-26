package esideal.station.servico;
import java.time.LocalDateTime;
import java.util.Objects;

import esideal.station.veiculo.Veiculo;

public class Servico {
    private int numServiço;
    private Veiculo veiculo;
    private Float custServiço;
    private Estado estado;
    private LocalDateTime horaInicio;
    private LocalDateTime horaFim;
    private String sms;
    private TipoServico tipoServico;

    /*CONSTRUTORES*/
    
    public Servico(int numServiço, Veiculo veiculo, Float custServiço, Estado estado, LocalDateTime horaInicio, LocalDateTime horaFim, String sms, TipoServico tipoServico) {
        this.numServiço = numServiço;
        this.veiculo = veiculo;
        this.custServiço = custServiço;
        this.estado = estado;
        this.horaInicio = horaInicio;
        this.horaFim = horaFim;
        this.sms = sms;
        this.tipoServico = tipoServico;
    }

    public Servico(Servico s) {
        this.numServiço = s.getNumServiço();
        this.veiculo = s.getVeiculo();
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

    public Veiculo getVeiculo(){
        return this.veiculo;
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
        veiculo == s.veiculo &&
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
        sb.append("Veículo: ").append(veiculo).append("\n");
        sb.append("Custo do Serviço: ").append(custServiço).append("\n");
        sb.append("Estado do serviço: ").append(estado).append("\n");
        sb.append("Hora do ínicio do serviço: ").append(horaInicio).append("\n");
        sb.append("Hora do fim do serviço: ").append(horaFim).append("\n");
        sb.append("SMS: ").append(sms).append("\n");
        sb.append("Tipo de Serviço: ").append(tipoServico).append("\n");
        return sb.toString();
    }
}
