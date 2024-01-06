package esideal.station.checkup;

import java.time.LocalDateTime;
import java.util.Map;

import esideal.data.CheckUpDAO;
import esideal.station.cliente.Cliente;
import esideal.station.cliente.ClienteFacade;
import esideal.station.ficha.FichaFacade;
import esideal.station.funcionario.FuncFacade;
import esideal.station.servico.Estado;
import esideal.station.servico.Servico;
import esideal.station.veiculo.VeiculoFacade;

public class CheckUpFacade implements ICheckUp{
    private Map<Integer, CheckUp> checkups;  

    public CheckUpFacade(){
        this.checkups = CheckUpDAO.getInstance(); 
    }

    /**
     * Cria um novo check-up e agenda o serviço, verificando conflitos de horários.
     * @param numCheckUp Número do check-up.
     * @param numFicha Número da ficha associada.
     * @param funcResponsavel ID do funcionário responsável.
     * @param matricula Matrícula do veículo.
     * @param dataCheckUp Data do check-up.
     * @param datafim Data de finalização do check-up.
     * @param estado Estado do check-up.
     */

    public void criarNovoCheckUpEAgendar(int numCheckUp, int numFicha, int funcResponsavel, String matricula, LocalDateTime dataCheckUp, LocalDateTime datafim, Estado estado) {
        FichaFacade f1 = new FichaFacade();
        FuncFacade f2 = new FuncFacade();

        CheckUp novoCheckUp = new CheckUp(numCheckUp, numFicha,funcResponsavel, matricula, dataCheckUp, datafim, estado);

        novoCheckUp.setDataCheckUp(dataCheckUp);

        // Verificando conflito de horário com os serviços do funcionário
        for (Servico servico : f2.obterServicosDoFuncionario(funcResponsavel).values()) {
            if (servico.getEstado() == Estado.AGENDADO || servico.getEstado() == Estado.EM_ANDAMENTO) {
                LocalDateTime inicioServico = servico.getHoraInicio();
                LocalDateTime fimServico = servico.getHoraFim();
                
                if (dataCheckUp.isBefore(LocalDateTime.now()) || dataCheckUp.isEqual(inicioServico) || (dataCheckUp.isAfter(inicioServico) && dataCheckUp.isBefore(fimServico))) {
                    System.out.println("Conflito de horário com um serviço agendado ou em andamento ou horário.");
                    return; // Cancela o agendamento do check-up
                }//METER HORAS DOS TURNOS DOS FUNC. NOS IFS
            }
        }

        // Verificando conflito de horário com os check-ups do funcionário
        for (CheckUp checkup : f2.obterCheckUpsDoFuncionario(funcResponsavel).values()) {
            if (checkup.getEstado() == Estado.AGENDADO || checkup.getEstado() == Estado.EM_ANDAMENTO) {
                LocalDateTime inicioCheckUp = checkup.getDataCheckUp();
                LocalDateTime fimCheckUp = checkup.getDataFim();
                
                if (dataCheckUp.isBefore(LocalDateTime.now()) || dataCheckUp.isEqual(inicioCheckUp) || (dataCheckUp.isAfter(inicioCheckUp) && dataCheckUp.isBefore(fimCheckUp))) {
                    System.out.println("Conflito de horário com um serviço agendado ou em andamento ou horário.");
                    return; // Cancela o agendamento do check-up
                }
            }
        }

        if (dataCheckUp.getHour() < f2.getFuncionarios().get(funcResponsavel).getHoraEntrada().getHour() || dataCheckUp.getHour() > f2.getFuncionarios().get(funcResponsavel).getHoraSaida().getHour()){
            System.out.println("Conflito de horário com um serviço agendado ou em andamento ou horário.");
            return; // Cancela o agendamento do check-up
        }

        if (dataCheckUp.isBefore(LocalDateTime.now())) {
            System.out.println("Conflito de horário com um serviço agendado ou em andamento ou horário.");
            return; // Cancela o agendamento do check-up
        }

        checkups.put(numCheckUp, novoCheckUp.clone());
        f1.getFichas().get(numFicha).getCheckups().put(numCheckUp, novoCheckUp.clone());

        System.out.println("Novo check-up criado agendado com sucesso");
    }

    /**
     * Envia uma mensagem para o cliente.
     * @param cliente Cliente a quem a mensagem será enviada.
     * @param mensagem Mensagem a ser enviada.
     */

    public void enviarMensagemCliente(Cliente cliente, String mensagem) {
        System.out.println("Mensagem enviada para " + cliente.getNome() + ": " + mensagem);
    }

    /**
     * Notifica o cliente sobre o fim do serviço associado ao check-up.
     * @param numCheckUp Número do check-up.
     * @param v Facade de Veículo.
     * @param c1 Facade de Cliente.
     */

    public void notificarClienteFimServico(int numCheckUp, VeiculoFacade v, ClienteFacade c1) {
        CheckUp c = checkups.get(numCheckUp);
        if (c != null) {
            String matricula = c.getMatricula();
            Cliente cliente = v.encontrarClientePorVeiculo(matricula);
            if (cliente != null) {
                String mensagem = "O check-up " + numCheckUp + " foi concluído com sucesso.";
                enviarMensagemCliente(cliente, mensagem);
            } else {
                System.out.println("Cliente não encontrado para o veículo associado ao serviço " + numCheckUp);
            }
        } else {
            System.out.println("O check-up com o número " + numCheckUp + " não foi encontrado.");
        }
    }

    public Map<Integer, CheckUp> getCheckUps() {
        return this.checkups;
    }
}
