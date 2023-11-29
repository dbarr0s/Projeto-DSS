package esideal.station.servico;

import java.time.LocalDateTime;
import java.util.Map;

import esideal.data.ServicoDAO;
import esideal.station.cliente.Cliente;
import esideal.station.cliente.ClienteFacade;
import esideal.station.funcionario.FuncFacade;
import esideal.station.funcionario.Funcionario;
import esideal.station.funcionario.TipoFuncionario;
import esideal.station.veiculo.VeiculoFacade;

public class ServicoFacade implements IServico{
    private Map<Integer, Servico> servicos;   

    public ServicoFacade(){
        this.servicos = ServicoDAO.getInstance(); 
    }

    public Map<Integer, Servico> getServicos() {
        return this.servicos;
    }

    public void criarNovoServicoEAgendar(int numServiço, int numCheckUp, int numFicha, int funcResponsavel, String matricula, Float custServiço, Estado estado, LocalDateTime horaInicio, LocalDateTime horaFim, String sms, TipoServico tipoServico, FuncFacade f) {
        Funcionario mecanicoMenosOcupado = null;
        int menorNumeroServicos = 0;

        for (Funcionario f1 : f.getFuncionarios().values()) {
            if (f1.getTipoFuncionario() == TipoFuncionario.MECANICO && f1.getPostosMecanico() == servicos.values().iterator().next().getTipoServico()) {
                int numeroServicos = f1.getServDoDia().size();
                int numeroCheckUp = f1.getCheckUpDoDia().size();
                int total = numeroCheckUp + numeroServicos;

                if (total < menorNumeroServicos) {
                    menorNumeroServicos = total;
                    mecanicoMenosOcupado = f1;
                }
            }
        }
        if (mecanicoMenosOcupado != null) {
            Servico novoServico = new Servico(numServiço, numCheckUp, numFicha, funcResponsavel, matricula, custServiço, estado, horaInicio, horaFim, sms, tipoServico);
            servicos.put(numServiço, novoServico);

            novoServico.setHoraInicio(horaInicio);
            servicos.put(novoServico.getNumServiço(), novoServico);
            mecanicoMenosOcupado.getServDoDia().put(numServiço, novoServico);

            System.out.println("Novo check-up criado e serviços agendados com sucesso com número: " + numServiço);
        } else {
            System.out.println("Não foi possível encontrar um mecânico disponível para o check-up.");
        }
    }
    
    public void iniciarServico(int numServico, LocalDateTime horaInicio){
        Servico servico = servicos.get(numServico);
        servico.setHoraInicio(horaInicio);
        servico.setEstado(Estado.EM_ANDAMENTO);
        servicos.put(servico.getNumServiço(), servico);
        System.out.println("Serviço " + numServico + " iniciado com sucesso.");
    }
    
    public void finalizarServico(int numServico){
        Servico servico = servicos.get(numServico);
        servico.setHoraFim(LocalDateTime.now());
        servico.setEstado(Estado.CONCLUÍDO);
        servicos.put(servico.getNumServiço(), servico);
        System.out.println("Serviço " + numServico + " concluído com sucesso.");
    }

    public void enviarMensagemCliente(Cliente cliente, String mensagem) {
        System.out.println("Mensagem enviada para " + cliente.getNome() + ": " + mensagem);
    }

    public void notificarClienteFimServico(int numServico, VeiculoFacade v, ClienteFacade c) {
        Servico servico = servicos.get(numServico);
        if (servico != null) {
            String matricula = servico.getMatricula();
            Cliente cliente = v.encontrarClientePorVeiculo(matricula, c);
            if (cliente != null) {
                String mensagem = "O serviço " + numServico + " foi concluído com sucesso.";
                enviarMensagemCliente(cliente, mensagem);
            } else {
                System.out.println("Cliente não encontrado para o veículo associado ao serviço " + numServico);
            }
        } else {
            System.out.println("O serviço com o número " + numServico + " não foi encontrado.");
        }
    }
}
