package esideal.station.servico;

import java.time.LocalDateTime;

import esideal.station.cliente.Cliente;
import esideal.station.funcionario.Funcionario;
import esideal.station.veiculo.Veiculo;

public interface IServico {
    void agendarServico(Veiculo veiculo, LocalDateTime dataAgendamento);
    void pedirServico(Veiculo veiculo);
    void iniciarServico(Veiculo veiculo, Funcionario funcionario);
    void finalizarServico(Veiculo veiculo, Funcionario funcionario);
    void notificarCliente(Cliente cliente);
}
