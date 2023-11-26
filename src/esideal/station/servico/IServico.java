package esideal.station.servico;

import java.time.LocalDateTime;

import esideal.station.cliente.Cliente;
import esideal.station.ficha.FichaVeiculo;
import esideal.station.veiculo.Veiculo;

public interface IServico {
    void agendarServico(FichaVeiculo f, Servico s, LocalDateTime horario);
    void criarNovoServico(int numServico, Veiculo v, float custo, Estado estado, LocalDateTime inicio, LocalDateTime fim, String sms, TipoServico tipoServico);
    void iniciarServico(int numServico, LocalDateTime horaInicio);
    void finalizarServico(int numServico);
    void enviarMensagemCliente(Cliente cliente, String mensagem);
    void notificarClienteFimServico(int numServico);
}
