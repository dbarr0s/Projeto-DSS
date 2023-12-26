package esideal.station.servico;

import java.time.LocalDateTime;
import java.util.Map;

import esideal.station.cliente.Cliente;
import esideal.station.cliente.ClienteFacade;
import esideal.station.veiculo.VeiculoFacade;

public interface IServico {
    void criarNovoServicoEAgendar(int numServiço, int numFicha, int funcResponsavel, String matricula, Float custServiço, Estado estado, LocalDateTime horaInicio, LocalDateTime horaFim, String sms, TipoServico tipoServico);
    void finalizarServico(int numServico, int numFunc);
    void notificarClienteFimServico(int numServico, VeiculoFacade v, ClienteFacade c);
    void enviarMensagemCliente(Cliente cliente, String mensagem);
    Map<Integer, Servico> getServicos();
}
