package esideal.station.servico;

import java.time.LocalDateTime;

import esideal.station.cliente.ClienteFacade;
import esideal.station.funcionario.FuncFacade;
import esideal.station.veiculo.VeiculoFacade;

public interface IServico {
    void criarNovoServicoEAgendar(int numServiço, int numCheckUp, int numFicha, int funcResponsavel, String matricula, Float custServiço, Estado estado, LocalDateTime horaInicio, LocalDateTime horaFim, String sms, TipoServico tipoServico, FuncFacade f);
    void iniciarServico(int numServico, LocalDateTime horaInicio);
    void finalizarServico(int numServico);
    void notificarClienteFimServico(int numServico, VeiculoFacade v, ClienteFacade c);
}
