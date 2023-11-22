package esideal.station.servico;
import java.time.LocalDateTime;

public class Servico {
    private int numServiço;
    private Float custServiço;
    private Estado estado;
    private LocalDateTime horaInicio;
    private LocalDateTime horaFim;
    private String sms;
    private TipoServico tipoServico;

    public Servico(int numServiço, Float custServiço, Estado estado, LocalDateTime horaInicio, LocalDateTime horaFim, String sms, TipoServico tipoServico) {
        this.numServiço = numServiço;
        this.custServiço = custServiço;
        this.estado = estado;
        this.horaInicio = horaInicio;
        this.horaFim = horaFim;
        this.sms = sms;
        this.tipoServico = tipoServico;
    }

    public Servico(Servico s) {
        this.numServiço = s.getNumServiço();
        this.custServiço = s.getCustServiço();
        this.estado = s.getEstado();
        this.horaInicio = s.getHoraInicio();
        this.horaFim = s.getHoraFim();
        this.sms = s.getSms();
        this.tipoServico = s.getTipoServico();
    }

    public int getNumServiço(){
        return this.numServiço;
    }

    public Float getCustServiço(){
        return this.custServiço;
    }

    public Estado getEstado(){
        return this.estado;
    }

    public LocalDateTime getHoraInicio(){
        return this.horaInicio;
    }

    public LocalDateTime getHoraFim(){
        return this.horaFim;
    }

    public String getSms(){
        return this.sms;
    }

    public TipoServico getTipoServico(){
        return this.tipoServico;
    }
}
