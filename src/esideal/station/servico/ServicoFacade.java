package esideal.station.servico;

import java.time.LocalDateTime;
import java.util.Map;

import esideal.data.ServicoDAO;
import esideal.station.checkup.CheckUp;
import esideal.station.cliente.Cliente;
import esideal.station.cliente.ClienteFacade;
import esideal.station.ficha.FichaFacade;
import esideal.station.funcionario.FuncFacade;
import esideal.station.veiculo.VeiculoFacade;

public class ServicoFacade implements IServico{
    private Map<Integer, Servico> servicos;   

    public ServicoFacade(){
        this.servicos = ServicoDAO.getInstance(); 
    }

    public Map<Integer, Servico> getServicos() {
        return this.servicos;
    }

    /**
     * Cria um novo serviço, verifica conflitos de horário com outros serviços do funcionário e agenda-o.
     * @param numServiço Número do serviço a ser criado.
     * @param numFicha Número da ficha associada ao serviço.
     * @param funcResponsavel Número do funcionário responsável pelo serviço.
     * @param matricula Matrícula do veículo associado ao serviço.
     * @param custServiço Custo do serviço.
     * @param estado Estado do serviço.
     * @param horaInicio Horário de início do serviço.
     * @param horaFim Horário de fim do serviço.
     * @param sms Mensagem do serviço.
     * @param tipoServico Tipo do serviço.
     */

    public void criarNovoServicoEAgendar(int numServiço, int numFicha, int funcResponsavel, String matricula, Float custServiço, Estado estado, LocalDateTime horaInicio, LocalDateTime horaFim, String sms, TipoServico tipoServico) {
        FichaFacade f1 = new FichaFacade();
        FuncFacade f2 = new FuncFacade();

        Servico novoServico = new Servico(numServiço, numFicha, funcResponsavel, matricula, custServiço, estado, horaInicio, horaFim, sms, tipoServico);

        novoServico.setHoraInicio(horaInicio);

                // Verificando conflito de horário com os serviços do funcionário
        for (Servico servico : f2.obterServicosDoFuncionario(funcResponsavel).values()) {
            if (servico.getEstado() == Estado.AGENDADO || servico.getEstado() == Estado.EM_ANDAMENTO) {
                LocalDateTime inicioServico = servico.getHoraInicio();
                LocalDateTime fimServico = servico.getHoraFim();
                
                if (horaInicio.isBefore(LocalDateTime.now()) || horaInicio.isEqual(inicioServico) || (horaInicio.isAfter(inicioServico) && horaInicio.isBefore(fimServico))) {
                    System.out.println("Conflito de horário com um serviço agendado ou em andamento ou horário.");
                    return; // Cancela o agendamento do check-up
                }
            }//METER HORAS DOS TURNOS DOS FUNC. NOS IFS
        }

        // Verificando conflito de horário com os check-ups do funcionário
        for (CheckUp checkup : f2.obterCheckUpsDoFuncionario(funcResponsavel).values()) {
            if (checkup.getEstado() == Estado.AGENDADO || checkup.getEstado() == Estado.EM_ANDAMENTO) {
                LocalDateTime inicioCheckUp = checkup.getDataCheckUp();
                LocalDateTime fimCheckUp = checkup.getDataFim();
                
                if (horaInicio.isBefore(LocalDateTime.now()) || horaInicio.isEqual(inicioCheckUp) || (horaInicio.isAfter(inicioCheckUp) && horaInicio.isBefore(fimCheckUp))) {
                    System.out.println("Conflito de horário com um serviço agendado ou em andamento ou horário.");
                    return; // Cancela o agendamento do check-up
                }
            }
        }

        if (horaInicio.getHour() < f2.getFuncionarios().get(funcResponsavel).getHoraEntrada().getHour() || horaInicio.getHour() > f2.getFuncionarios().get(funcResponsavel).getHoraSaida().getHour()){
            System.out.println("Conflito de horário com um serviço agendado ou em andamento ou horário.");
            return; // Cancela o agendamento do check-up
        }

        if (horaInicio.isBefore(LocalDateTime.now())) {
            System.out.println("Conflito de horário com um serviço agendado ou em andamento ou horário.");
            return; // Cancela o agendamento do check-up
        }

        System.out.println("Serviço criado com sucesso!");

        servicos.put(novoServico.getNumServiço(), novoServico.clone());
        f1.getFichas().get(numFicha).getServicos().put(numServiço, novoServico.clone());
    }

    /**
     * Finaliza um serviço com base no número do serviço e número do funcionário.
     * @param numServico Número do serviço a ser finalizado.
     * @param numFunc Número do funcionário.
     */
    
    public void finalizarServico(int numServico, int numFunc){
        Servico servico = servicos.get(numServico);
        if (servico != null && servico.getFuncResponsavel() == numFunc && servico.getEstado() == Estado.EM_ANDAMENTO){
            servico.setEstado(Estado.CONCLUÍDO);

            ServicoDAO servicoDAO = new ServicoDAO(); 
            servicoDAO.atualizarEstadoServico(servico);

            servico.setSms("Serviço " + numServico + " concluído com sucesso.");

            servicoDAO.atualizarSMSServico(servico);
            System.out.println("Serviço " + numServico + " concluído com sucesso.");
        }
        else  System.out.println("Serviço não foi encontrado!");
    }

    /**
     * Envia uma mensagem para um cliente específico.
     * @param cliente Cliente para o qual a mensagem será enviada.
     * @param mensagem Mensagem a ser enviada.
     */

    public void enviarMensagemCliente(Cliente cliente, String mensagem) {
        System.out.println("Mensagem enviada para " + cliente.getNome() + ", para o número " +cliente.getTelefone()+ ": " + mensagem);
    }

    /**
     * Notifica um cliente sobre o término de um serviço.
     * @param numServico Número do serviço.
     * @param v Instância de VeiculoFacade.
     * @param c Instância de ClienteFacade.
     */

    public void notificarClienteFimServico(int numServico, VeiculoFacade v, ClienteFacade c) {
        Servico servico = servicos.get(numServico);
        if (servico != null) {
            String matricula = servico.getMatricula();
            Cliente cliente = v.encontrarClientePorVeiculo(matricula);
            if (cliente != null) {
                String mensagem = "Serviço " + numServico + " foi concluído com sucesso.";
                enviarMensagemCliente(cliente, mensagem);
            } else {
                System.out.println("Cliente não encontrado para o veículo associado ao serviço " + numServico);
            }
        } else {
            System.out.println("O serviço com o número " + numServico + " não foi encontrado.");
        }
    }
}
