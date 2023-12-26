package esideal.station.checkup;

import java.time.LocalDateTime;
import java.util.Map;

import esideal.station.cliente.Cliente;
import esideal.station.cliente.ClienteFacade;
import esideal.station.servico.Estado;
import esideal.station.veiculo.VeiculoFacade;

public interface ICheckUp {
    Map<Integer, CheckUp> getCheckUps();
    void enviarMensagemCliente(Cliente cliente, String mensagem);
    void notificarClienteFimServico(int numCheckUp, VeiculoFacade v, ClienteFacade c);
    void criarNovoCheckUpEAgendar(int numCheckUp, int numFicha, int funcResponsavel, String matricula, LocalDateTime dataCheckUp, LocalDateTime datafim, Estado estado);
}