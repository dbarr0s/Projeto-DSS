package esideal.station.facades;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;

import esideal.station.checkup.CheckUp;
import esideal.station.checkup.ICheckUp;
import esideal.station.cliente.*;
import esideal.station.ficha.FichaVeiculo;
import esideal.station.ficha.IFichaVeiculo;
import esideal.station.funcionario.*;
import esideal.station.servico.Estado;
import esideal.station.servico.IServico;
import esideal.station.servico.Servico;
import esideal.station.servico.TipoServico;
import esideal.station.veiculo.IVeiculos;
import esideal.station.veiculo.Veiculo;

public class StationFacade implements IFuncionário, ICheckUp, IFichaVeiculo, IServico, ICliente, IVeiculos{
    private Map<Integer, Cliente> clientes;             //Integer == NIF
    private Map<Integer, Funcionario> funcionarios;     //Integer == cartao
    private Map<String, Veiculo> veiculos;              //String == Matricula
    private Map<Integer, Servico> servicosPendentes;    //Identificados pelo número de serviço
    private Map<Integer, Servico> servicosExecutados;   //Identificados pelo número de serviço
    private Map<Integer, CheckUp> checkUpPendentes;     //Identificados pelo número de check-up
    private Map<Integer, CheckUp> checkUpExecutados;    //Identificados pelo número de check-up
    private Map<Integer, CheckUp> checkUpEmExecucao;     //Identificados pelo número de check-up
    private Map<Integer, Servico> servicosEmExecucao; 

    public StationFacade(){
        this.clientes = clientes;
        this.funcionarios = funcionarios;
        this.veiculos = veiculos;
        this.servicosPendentes = servicosPendentes;
        this.servicosExecutados = servicosExecutados; 
        this.checkUpPendentes = checkUpPendentes; 
        this.checkUpExecutados = checkUpExecutados; 
    }

    /*OPERAÇÕES PARA SERVIÇOS*/

    public void agendarServico(FichaVeiculo f, Servico s, LocalDateTime horario) {
        Funcionario mecanicoMenosOcupado = encontrarMecanicoMenosOcupado(s);
        mecanicoMenosOcupado.getServDoDia().put(s.getNumServiço(), s);
        f.getServAgendados().put(s.getNumServiço(), s);
        s.setHoraInicio(horario);
        this.servicosPendentes.put(s.getNumServiço(), s);
    }

    public void criarNovoServico(int numServico, Veiculo v, float custo, Estado estado, LocalDateTime inicio, LocalDateTime fim, String sms, TipoServico tipoServico){
        Servico novoServico = new Servico(numServico, v, custo, estado, inicio, fim, sms, tipoServico);
        servicosPendentes.put(numServico, novoServico);
        System.out.println("Novo serviço criado com sucesso com número: " + numServico);
    }
    
    public void iniciarServico(int numServico, LocalDateTime horaInicio){
        for(Map.Entry<Integer, Servico> entry : servicosPendentes.entrySet()){
            if (entry.getKey() == numServico) {
                Servico servico = servicosPendentes.get(numServico);
                servico.setHoraInicio(horaInicio);
                servico.setEstado(Estado.EM_ANDAMENTO);
                servicosEmExecucao.put(servico.getNumServiço(), servico);
                servicosPendentes.remove(numServico);
                System.out.println("Serviço " + numServico + " iniciado com sucesso.");
            } else {
                System.out.println("O serviço com o número " + numServico + " não foi encontrado.");
            }
        }
    }
    
    public void finalizarServico(int numServico){
        for(Map.Entry<Integer, Servico> entry : servicosEmExecucao.entrySet()){
            if (entry.getKey() == numServico) {
                Servico servico = servicosEmExecucao.get(numServico);
                servico.setHoraFim(LocalDateTime.now());
                servico.setEstado(Estado.CONCLUÍDO);
                servicosExecutados.put(servico.getNumServiço(), servico);
                servicosEmExecucao.remove(numServico);
                System.out.println("Serviço " + numServico + " concluído com sucesso.");
            } else {
                System.out.println("O serviço com o número " + numServico + " não foi encontrado.");
            }
        }
    }

    public void enviarMensagemCliente(Cliente cliente, String mensagem) {
        System.out.println("Mensagem enviada para " + cliente.getNome() + ": " + mensagem);
    }

    public void notificarClienteFimServico(int numServico) {
        Servico servico = servicosExecutados.get(numServico);
        if (servico != null) {
            Veiculo veiculo = servico.getVeiculo();
            Cliente cliente = encontrarClientePorVeiculo(veiculo);
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

    /*OPERAÇÕES PARA CHECK-UPS*/

    public void criarNovoCheckUp(int numCheckUp, Veiculo veiculo, LocalDateTime dataCheckUp, Funcionario funcionario, Estado estado, Map<String, Servico> servicosAExecutar) {
        CheckUp novoCheckUp = new CheckUp(numCheckUp, veiculo, dataCheckUp, funcionario, estado, servicosAExecutar);
        checkUpPendentes.put(numCheckUp, novoCheckUp);
        System.out.println("Novo check-up criado com sucesso com número: " + numCheckUp);
    }

    public void iniciarCheckUp(int numCheckUp, LocalDateTime data){
        for(Map.Entry<Integer, CheckUp> entry : checkUpPendentes.entrySet()){
            if (entry.getKey() == numCheckUp && entry.getValue().getDataCheckUp() == data) {
                CheckUp c = checkUpPendentes.get(numCheckUp);
                c.setEstado(Estado.EM_ANDAMENTO);
                checkUpEmExecucao.put(c.getNumCheckUp(), c);
                checkUpPendentes.remove(c);
                System.out.println("Check-up " + numCheckUp + " iniciado com sucesso.");
            } else {
                System.out.println("O serviço com o número " + numCheckUp + " não foi encontrado.");
            }
        }
    }

    public void finalizarCheckUp(int numCheckUp){
        for(Map.Entry<Integer, CheckUp> entry : checkUpEmExecucao.entrySet()){
            if (entry.getKey() == numCheckUp) {
                CheckUp c = checkUpEmExecucao.get(numCheckUp);
                c.setEstado(Estado.CONCLUÍDO);
                checkUpExecutados.put(c.getNumCheckUp(), c);
                checkUpEmExecucao.remove(c.getNumCheckUp());
                System.out.println("Check-up " + numCheckUp + " iniciado com sucesso.");
            } else {
                System.out.println("O serviço com o número " + numCheckUp + " não foi encontrado.");
            }
        }
    }

    /*OPERAÇÕES PARA FUNCIONÁRIOS*/

    @Override
    public void addServDoDia() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addServDoDia'");
    }

    @Override
    public void funcionarioExiste() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'funcExiste'");
    }

    public Funcionario encontrarMecanicoMenosOcupado(Servico s) {
        Funcionario mecanicoMenosOcupado = null;
        int menorNumeroServicos = 0;

        for (Funcionario f : funcionarios.values()) {

            if (f.getTipoFuncionario() == TipoFuncionario.MECANICO && f.getPostosMecanico() == s.getTipoServico()) {
                int numeroServicos = f.getServDoDia().size();

                if (numeroServicos < menorNumeroServicos) {
                    menorNumeroServicos = numeroServicos;
                    mecanicoMenosOcupado = f;
                }
            }
        }
    
        return mecanicoMenosOcupado;
    }

    public void iniciarTurno(int cartaoFuncionario, LocalDateTime horaEntrada) {
        Funcionario funcionario = funcionarios.get(cartaoFuncionario);
        if (funcionario != null) {

            if (funcionario.getTipoFuncionario() == TipoFuncionario.MECANICO || funcionario.getTipoFuncionario() == TipoFuncionario.GERENTE) {
                LocalDateTime horaPrevista = funcionario.getHoraEntrada();
    
                if (horaEntrada.isAfter(horaPrevista)) {
                    System.out.println("Aviso de atraso para o funcionário " + cartaoFuncionario + ". Hora de entrada prevista: " + horaPrevista);
                    penalizarAtraso(cartaoFuncionario, horaEntrada);
                }

                System.out.println("Turno iniciado para o funcionário " + cartaoFuncionario + " às " + horaEntrada);
            }
        }
    }

    public void penalizarAtraso(int cartaoFuncionario, LocalDateTime horaEntrada) {
        Funcionario funcionario = funcionarios.get(cartaoFuncionario);
        if (funcionario != null) {
            LocalDateTime horaPrevista = funcionario.getHoraEntrada();
    
            long minutosAtraso = java.time.Duration.between(horaPrevista, horaEntrada).toMinutes();
    
            if (minutosAtraso > 0) {
                LocalDateTime novaHoraSaida = funcionario.getHoraSaida().plusMinutes(minutosAtraso);
                System.out.println("Funcionário " + cartaoFuncionario + " penalizado com " + minutosAtraso + " minutos de atraso. Nova hora de saída: " + novaHoraSaida);
            } else {
                System.out.println("Funcionário " + cartaoFuncionario + " chegou pontualmente. Sem penalidades.");
            }
        } else {
            System.out.println("Funcionário com cartão " + cartaoFuncionario + " não encontrado.");
        }
    }
    
    public void finalizarTurno(int cartaoFuncionario, LocalDateTime horaEntrada, LocalDateTime horaSaida) {
        Funcionario funcionario = funcionarios.get(cartaoFuncionario);
        if (funcionario != null) {
            LocalDateTime horaPrevista = funcionario.getHoraEntrada();

             if (horaEntrada.isAfter(horaPrevista)) {
                    System.out.println("Aviso de atraso para o funcionário " + cartaoFuncionario + ". Hora de entrada prevista: " + horaPrevista);
                    penalizarAtraso(cartaoFuncionario, horaEntrada);
            }
            System.out.println("Turno finalizado para o funcionário " + cartaoFuncionario + " às " + horaSaida);
        }
    }

    /*OPERAÇÕES PARA VEÍCULOS*/
    
    public boolean veicExiste(String matricula){   
        for(Map.Entry<String, Veiculo> entry : veiculos.entrySet())
        {
            if(entry.getKey().equals(matricula)){
                return true;
            }
        }
        return false;
    }

    public Cliente encontrarClientePorVeiculo(Veiculo veiculo){
        for (Cliente cliente : clientes.values()) {
            if (cliente.getVeiculos().containsValue(veiculo)) {
                return cliente;
            }
        }
        return null;
    }
    
    /*OPERAÇÕES PARA CLIENTES*/

    public boolean clienteValido(Integer NIF){
        for(Map.Entry<Integer, Cliente> entry : clientes.entrySet())
        {
            if(entry.getKey().equals(NIF)){
                return true;
            }
        }
        return false;
    }

    public boolean clienteTemVeiculos(Cliente c){
        if (c.getVeiculos().isEmpty()) return false;
        return true;
    }

    /*OPERAÇÕES PARA A FICHA DO VEICULO*/

    @Override
    public void registoCheckUpFicha() { //AGENDADOS E EXECUTADOS
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'registoCheckUp'");
    }

    @Override
    public void registoServicosFicha() { //AGENDADOS E EXECUTADOS
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'registoServicos'");
    }
}