package esideal.ui;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import esideal.data.CheckUpDAO;
import esideal.data.ServicoDAO;
import esideal.data.TurnosDAO;
import esideal.station.checkup.CheckUp;
import esideal.station.checkup.CheckUpFacade;
import esideal.station.checkup.ICheckUp;
import esideal.station.cliente.Cliente;
import esideal.station.cliente.ClienteFacade;
import esideal.station.cliente.ICliente;
import esideal.station.ficha.FichaFacade;
import esideal.station.ficha.FichaVeiculo;
import esideal.station.ficha.IFichaVeiculo;
import esideal.station.funcionario.FuncFacade;
import esideal.station.funcionario.Funcionario;
import esideal.station.funcionario.IFuncionário;
import esideal.station.funcionario.TipoFuncionario;
import esideal.station.servico.Estado;
import esideal.station.servico.IServico;
import esideal.station.servico.Servico;
import esideal.station.servico.ServicoFacade;
import esideal.station.servico.TipoServico;
import esideal.station.turnos.ITurnos;
import esideal.station.turnos.Turnos;
import esideal.station.turnos.TurnosFacade;
import esideal.station.veiculo.IVeiculos;
import esideal.station.veiculo.Veiculo;
import esideal.station.veiculo.VeiculoFacade;

public class EstacaoUI {

    private final IServico servicos;
    private final ICliente clientes;
    private final IVeiculos veiculos;
    private final IFichaVeiculo fichas;
    private final IFuncionário funcionarios;
    private final ITurnos turnos;
    private final ICheckUp checkups;

    private final Scanner sc;

    public EstacaoUI() {
        this.servicos = new ServicoFacade();
        this.clientes = new ClienteFacade();
        this.veiculos = new VeiculoFacade();
        this.fichas = new FichaFacade();
        this.funcionarios = new FuncFacade();
        this.turnos = new TurnosFacade();
        this.checkups = new CheckUpFacade();
        
        sc = new Scanner(System.in);
    }

    /**
     * Executa o menu principal e invoca o método correspondente à opção seleccionada.
     */
    public void run() {
        System.out.println("Bem vindo ao Sistema da nossa Estação de Serviço!");
        this.menuPrincipal();
        System.out.println("Até Já!");
    }

    /**
     * Mostra o menu principal e suas opções.
     */

    private void menuPrincipal() {
        Menu menu = new Menu(new String[]{
            "Login"
        });

        // Registar os handlers das transições
        menu.setHandler(1, ()->login());

        menu.run();
    }

    /**
     * Realiza o processo de login para os funcionários e redireciona para o sistema apropriado.
     */
    
    private void login() {
        while (true) {
            System.out.println("----------BEM VINDO----------");
            System.out.println("Introduza o seu ID de funcionário");
            int id;
            try {
                id = sc.nextInt();
            } catch (InputMismatchException | NumberFormatException e) {
                sc.nextLine();
                System.out.println("Opção Inválida!!!");
                return;     
            }
            
            int numTurno = 0;
    
            if (!this.funcionarios.funcionarioExiste(id)) {
                System.out.println("Funcionário não encontrado.");
                continue; // Volta ao início do loop para nova tentativa de login
            }
    
            if (this.funcionarios.getFuncionarios().get(id).getTipoFuncionario() == TipoFuncionario.MECANICO) {
                System.out.println("Bem vindo, " + id + ".");
                this.turnos.iniciarTurno(numTurno, id, LocalDateTime.now());
                sistemaMecanico();

                while (true) {
                    System.out.println("Deseja encerrar seu turno? S para encerrar, qualquer outra para voltar atrás");
                    String resposta = sc.next();
                    if (resposta.equalsIgnoreCase("S")) {
                        this.turnos.finalizarTurno(numTurno, id, LocalDateTime.now());
                        break; // Encerra o loop e sai do sistema
                    } else {
                        sistemaMecanico();
                    }
                }
                break; // Sai do loop após o login bem-sucedido
            } else if (this.funcionarios.getFuncionarios().get(id).getTipoFuncionario() == TipoFuncionario.GERENTE) {
                System.out.println("Bem vindo, " + id + ".");
                this.turnos.iniciarTurno(numTurno, id, LocalDateTime.now());
                sistemaGerente();
    
                while (true) {
                    System.out.println("Deseja encerrar seu turno? S para encerrar, qualquer outra para voltar atrás");
                    String resposta = sc.next();
                    if (resposta.equalsIgnoreCase("S")) {
                        this.turnos.finalizarTurno(numTurno, id, LocalDateTime.now());
                        break; // Encerra o loop e sai do sistema
                    } else {
                        sistemaGerente();
                    }
                }
                break; // Sai do loop após o login bem-sucedido
            }
        }
    }    

    //SISTEMA DO GERENTE//

    /**
     * Redireciona para o sistema de gerente após o login bem-sucedido.
     */
    
    private void sistemaGerente() {
        Menu menu = new Menu(new String[]{
                "Gerir Serviços",
                "Gerir Check-Ups",
                "Gerir Clientes",
                "Gerir Fichas de Veículo",
                "Gerir Veículos",
                "Gerir Funcionários"
        });

        // Registar os handlers das transições
        menu.setHandler(1, ()->gerirServicos());
        menu.setHandler(2, ()->gerirCheckUps());
        menu.setHandler(3, ()->gerirClientes());;
        menu.setHandler(4, ()->gerirFichas());
        menu.setHandler(5, ()->gerirVeiculos());
        menu.setHandler(6, ()->gerirFuncionarios());

        menu.run();
    }

    //GESTÃO DE SERVIÇOS//

    /**
     * Realiza a gestão de serviços oferecidos pela estação de serviço.
     */

    private void gerirServicos() {
        Menu menu = new Menu("Gestão de Serviços", new String[]{
            "Criar e Agendar Serviços",
            "Lista de Serviços"
        });

        menu.setHandler(1, ()->criarAgendarServico());
        menu.setHandler(2, ()->listarServicos());

        menu.run();
    }

    /**
     * Cria e agenda um novo serviço na estação de serviço.
     */

    private void criarAgendarServico() {
        System.out.println("----------NOVO SERVIÇO----------");
        int idServico = 0;
        
        System.out.println("Introduzir ID do funcionário responsável: ");
        int IDFuncionario;
        try {
            IDFuncionario = sc.nextInt();
        } catch (InputMismatchException | NumberFormatException e) {
            sc.nextLine();
            System.out.println("Funcionário Inválido!!!");
            return;     
        }
        
        if (!this.funcionarios.funcionarioExiste(IDFuncionario)) {
            System.out.println("Não introduziu o número de um funcionário válido");
            sistemaGerente();
        }
        
        System.out.println("Introduzir ID da ficha de veículo: ");
        int idFicha;
        try {
            idFicha = sc.nextInt();
        } catch (InputMismatchException | NumberFormatException e) {
            sc.nextLine();
            System.out.println("Ficha de Veículo Inválida!!!");
            return;
        }
        if (!this.fichas.existeFicha(idFicha)) {
            System.out.println("Não introduziu o número de uma ficha do veículo válida");
            sistemaGerente();
        }

        FichaVeiculo ficha = this.fichas.getFichas().get(idFicha);
        String matricula = ficha.getMatricula();
        
        System.out.println("Introduzir custo do serviço: ");
        float custo;
        try {
            custo = sc.nextFloat();
        } catch (InputMismatchException | NumberFormatException e) {
            sc.nextLine();
            System.out.println("Custo Inválido!!!");
            return;
        }
        Estado estado = Estado.AGENDADO;
        
        System.out.println("Introduzir hora de inicio (Formato: yyyy-MM-ddTHH:mm:ss): ");
        String horaInicio = sc.next();
        LocalDateTime horaInicioServico;
        try {
            horaInicioServico = LocalDateTime.parse(horaInicio);
        } catch (DateTimeParseException e) {
            sc.nextLine();
            System.out.println("Hora de Início Inválida!!!");
            return;
        }
        horaInicioServico = LocalDateTime.parse(horaInicio);
        LocalDateTime horaFimServico = horaInicioServico.plusHours(2); 

        String sms = "Serviço foi criado com sucesso!";
        System.out.println("Introduza o tipo de serviço:");
        System.out.println("1 - UNIVERSAL");
        System.out.println("2 - GASOLEO");
        System.out.println("3 - GASOLINA");
        System.out.println("4 - ELETRICO");
        System.out.println("5 - HIBRIDO");

        int tipo;
        try {
            tipo = sc.nextInt();            
        } catch (InputMismatchException | NumberFormatException e) {
            sc.nextLine();
            System.out.println("Tipo Inválido!!!");
            return;
        }
        
        TipoServico t = null;

        switch (tipo) {
            case 1:
                t = TipoServico.UNIVERSAL;
                break;
            case 2:
                t = TipoServico.GASOLEO;
                break;
            case 3:
                t = TipoServico.GASOLINA;
                break;
            case 4:
                t = TipoServico.ELETRICO;
                break;
            case 5:
                t = TipoServico.HIBRIDO;
                break;
            default:
                System.out.println("Opção inválida. O serviço não foi criado.");
                return; // Sai do método sem criar o serviço
        }

        if((this.funcionarios.getFuncionarios().get(IDFuncionario).getPostosMecanico().toString() == this.veiculos.getVeiculos().get(matricula).getTipoMotor().toString()) && (t.toString() == this.veiculos.getVeiculos().get(matricula).getTipoMotor().toString())){
            this.servicos.criarNovoServicoEAgendar(idServico, idFicha, IDFuncionario, matricula, custo, estado, horaInicioServico, horaFimServico, sms, t);
        }        
        else 
        {
            System.out.println("Mensagem Enviada: Serviço não foi criado com sucesso pois este funcionário não sabe fazer este serviço ou este serviço não é para este veículo!");
            System.out.println("Tente Novamente!");
            criarAgendarServico();
        }
    }

    /**
     * Lista todos os serviços registados na estação de serviço.
     */

    private void listarServicos() {
        try
        {
            if (this.servicos.getServicos().isEmpty()) {
                System.out.println("Não há servicos registados.");
            } else {
                System.out.println("Lista de todos os servicos:");
                for (Servico s : this.servicos.getServicos().values()) {
                    System.out.println(s.clone());
                }
            }
        }catch (Exception e) {
            // Database error!
            e.printStackTrace();
        }
    }

    //GESTÃO DE CHECK-UPS//

    /**
     * Menu para gestão de check-ups na estação de serviço.
     */
        
    private void gerirCheckUps() {
        Menu menu = new Menu("Gestão de Check-Ups", new String[]{
            "Criar e Agendar Check-Ups",
            "Listar Check-Ups"
        });
        menu.setHandler(1, ()->criarCheckUps());
        menu.setHandler(2, ()->listarCheckUps());

        menu.run();
    }

    /**
     * Cria e agenda um novo check-up para um veículo na estação de serviço.
     */

    private void criarCheckUps() {
        System.out.println("----------NOVO CHECK-UP----------");
        int idCheckUp = 0;

        System.out.println("Introduzir ID do funcionário responsável: ");
        int IDFuncionario;
        try {
            IDFuncionario = sc.nextInt();
        } catch (InputMismatchException | NumberFormatException e) {
            sc.nextLine();
            System.out.println("Funcionário Inválido!!!");
            return;     
        }
        if (!this.funcionarios.funcionarioExiste(IDFuncionario)) {
            System.out.println("Não introduziu o número de um funcionário válido");
            sistemaGerente();
        }
        
        System.out.println("Introduzir ID da ficha de veiculo: ");
        int idFicha;
        try {
            idFicha = sc.nextInt();
        } catch (InputMismatchException | NumberFormatException e) {
            sc.nextLine();
            System.out.println("Ficha de Veículo Inválida!!!");
            return;
        }
        if (!this.fichas.existeFicha(idFicha)) {
            System.out.println("Não introduziu o número de uma ficha do veículo válida");
            sistemaGerente();
        }
        FichaVeiculo ficha = this.fichas.getFichas().get(idFicha);
        String matricula = ficha.getMatricula();
        
        System.out.println("Introduzir hora de inicio (Formato: yyyy-MM-ddTHH:mm:ss): ");
        //2023-12-20T11:50:00 -> FORMATO//
        String horaInicio = sc.next();
        
        LocalDateTime horaInicioServico;
        try {
            horaInicioServico = LocalDateTime.parse(horaInicio);
        } catch (DateTimeParseException e) {
            sc.nextLine();
            System.out.println("Hora de Início Inválida!!!");
            return;
        }
        horaInicioServico = LocalDateTime.parse(horaInicio);
        LocalDateTime datafim = horaInicioServico.plusHours(1); 
        
        if(this.funcionarios.getFuncionarios().get(IDFuncionario).getPostosMecanico().toString() == this.veiculos.getVeiculos().get(matricula).getTipoMotor().toString()){
            this.checkups.criarNovoCheckUpEAgendar(idCheckUp, idFicha, IDFuncionario, matricula, horaInicioServico, datafim, Estado.AGENDADO);
        }
        else{
            System.out.println("Check-up não foi criado com sucesso!");
            System.out.println("Tente Novamente!");
            criarCheckUps();
        }
    }

    /**
     * Lista todos os check-ups registados na estação de serviço.
     */

    private void listarCheckUps() {
        try
        {
            if (this.checkups.getCheckUps().values().isEmpty()) {
                System.out.println("Não há check-ups registados.");
            } else {
                System.out.println("Lista de todos os check-ups:");
                for (CheckUp c : this.checkups.getCheckUps().values()) {
                    System.out.println(c.clone());
                }
            }
        }catch (Exception e) {
            // Database error!
            e.printStackTrace();
        }
    }
    
    //GESTÃO DE CLIENTES//

    /**
     * Menu para gestão de clientes na estação de serviço.
     */

    private void gerirClientes() {
        Menu menu = new Menu("Gestão de Clientes", new String[]{
            "Verificar se há informação sobre o cliente",
            "Verificar se cliente tem veículos",
            "Listar clientes"
        });
        menu.setHandler(1, ()->verificarClienteExiste());
        menu.setHandler(2, ()->verificarClienteVeiculos());
        menu.setHandler(3, ()->listarClientes());

        menu.run();
    }

    /**
     * Verifica se um cliente com um determinado nome está registado na estação de serviço.
     */
    
    private void verificarClienteExiste() {
        System.out.println("Introduza o nome do cliente: ");
        String n = sc.nextLine();

        if (this.clientes.clienteValido(n)) {
            System.out.println("O cliente com nome " + n + " é válido.");
            Cliente c = this.clientes.getClientes().get(n);
            System.out.println("Cliente: " + c);
        } else {
            System.out.println("Não existe cliente com o nome " + n);
        }
    }

    /**
     * Verifica se um cliente possui veículos associados na estação de serviço.
     */
    
    private void verificarClienteVeiculos() {
        System.out.println("Introduza o nome do cliente: ");
        String n = sc.nextLine();

        Cliente cliente = this.clientes.getClientes().get(n);
        if (cliente != null) {
            if (this.clientes.clienteTemVeiculos(cliente)) {
                System.out.println("O cliente com nome " + n + " possui veículos associados.");
                Map<String, Veiculo> todosVeiculos = cliente.getVeiculos();

                if (todosVeiculos.isEmpty()) {
                    System.out.println("Não há veículos registados.");
                } else {
                    System.out.println("Lista de todos os veículos:");
                    for (Veiculo v : todosVeiculos.values()) {
                        System.out.println(v);
                    }
                }
            } else {
                System.out.println("O cliente com nome " + n + " não possui veículos associados.");
            }
        } else {
            System.out.println("Não existe cliente com o nome " + n);
        }
    }

    /**
     * Lista todos os clientes registados na estação de serviço.
     */

    private void listarClientes() {
        if (clientes.getClientes().isEmpty()) {
            System.out.println("Não há clientes registados.");
        } else {
            System.out.println("Lista de todos os clientes:");
            for (Cliente cliente : clientes.getClientes().values()) {
                System.out.println(cliente);
            }
        }
    }

    //GESTÃO DE VEÍCULOS//

    /**
     * Menu para gestão de veículos na estação de serviço.
     */
    
    private void gerirVeiculos() {
        Menu menu = new Menu("Gestão de Veículos", new String[]{
            "Verificar se há informação sobre o Veículo",
            "Verificar o dono do veículo",
            "Listar Veículos"
        });
        menu.setHandler(1, ()->verificarVeicExiste());
        menu.setHandler(2, ()->verificarDono());
        menu.setHandler(3, ()->listarVeiculos());

        menu.run();
    }

    /**
     * Verifica se um veículo com uma determinada matrícula está registado na estação de serviço.
     */

    private void verificarVeicExiste() {
        System.out.println("Introduza a matrícula do veículo: ");
        String matricula = sc.nextLine();

        if (this.veiculos.veicExiste(matricula)) {
            System.out.println("O veículo com matrícula " + matricula + " é válido.");
            Veiculo v = this.veiculos.getVeiculos().get(matricula);
            System.out.println("Veiculo: " + v);
        } else {
            System.out.println("Não existe veículo com a matricula " + matricula);
        }
    }

    /**
     * Verifica o proprietário de um veículo com base na matrícula na estação de serviço.
     */

    private void verificarDono() {
        System.out.println("Introduza a matrícula do veículo: ");
        String matricula = sc.nextLine();

        Veiculo v = this.veiculos.getVeiculos().get(matricula);
        if (v != null) {
            if (this.veiculos.veicExiste(matricula)) {
                Cliente c = this.veiculos.encontrarClientePorVeiculo(matricula);
                System.out.println("O veículo com matrícula " + matricula + " tem como dono, o cliente com o Nome: " +c.getNome()+ ".");
                String id = c.getNome();
                this.clientes.getClientes().get(id);
            }
        } else {
            System.out.println("Não existe veículo com matrícula " + matricula + ".");
        }
    }

    /**
     * Lista todos os veículos registados na estação de serviço.
     */

    private void listarVeiculos() {
        if (this.veiculos.getVeiculos().isEmpty()) {
            System.out.println("Não há veículos registados.");
        } else {
            System.out.println("Lista de todos os veículos:");
            for (Veiculo v : this.veiculos.getVeiculos().values()) {
                System.out.println(v);
            }
        }
    }

    //GESTÃO DE FICHAS DE VEÍCULOS//

    /**
     * Menu para gestão de fichas de veículos na estação de serviço.
     */

    private void gerirFichas() {
        Menu menu = new Menu("Gestão de Fichas de Veículos", new String[]{
            "Ver Ficha de Veículo",
            "Listar Serviços do Veículo",
            "Listar Check-Ups do Veículo"
        });
        menu.setHandler(1, ()->fichaVeic());
        menu.setHandler(2, ()->listarServicos1());
        menu.setHandler(3, ()->listarCheckUps1());

        menu.run();
    }

    /**
     * Mostra a ficha de um veículo com base no número da ficha na estação de serviço.
     */

    private void fichaVeic() {
        System.out.println("Introduza o número da ficha do veículo: ");
        int numFicha;
        try {
            numFicha = sc.nextInt();
        } catch (InputMismatchException | NumberFormatException e) {
            sc.nextLine();
            System.out.println("Ficha de Veículo Inválida!!!");
            return;
        }

        if (this.fichas.existeFicha(numFicha)) {
            FichaVeiculo f = this.fichas.getFichas().get(numFicha);
            System.out.println("Ficha de Veículo: " + f);
        } else {
            System.out.println("Não existe ficha com este número " + numFicha);
        }
    }

    /**
     * Lista os serviços associados a um veículo com base no número da ficha na estação de serviço.
     */

    private void listarServicos1() {
        System.out.println("Introduza o número da ficha do veículo: ");
        int numFicha;
        try {
            numFicha = sc.nextInt();
        } catch (InputMismatchException | NumberFormatException e) {
            sc.nextLine();
            System.out.println("Ficha de Veículo Inválida!!!");
            return;
        }

        if (this.servicos.getServicos().isEmpty()) {
            System.out.println("Não há servicos registados.");
        } else {
            System.out.println("Lista de todos os servicos:");
            for (Servico s : this.servicos.getServicos().values()){
                if(s.getNumFicha() == numFicha) System.out.println(s.clone());
            }
        }
    }

    /**
     * Lista os check-ups associados a um veículo com base no número da ficha na estação de serviço.
     */

    private void listarCheckUps1() {
        System.out.println("Introduza o número da ficha do veículo: ");
        int numFicha;
        try {
            numFicha = sc.nextInt();
        } catch (InputMismatchException | NumberFormatException e) {
            sc.nextLine();
            System.out.println("Ficha de Veículo Inválida!!!");
            return;
        }

        if (this.checkups.getCheckUps().values().isEmpty()) {
            System.out.println("Não há check-ups registados.");
        } else {
            System.out.println("Lista de todos os check-ups:");
            for (CheckUp c : this.checkups.getCheckUps().values()) {
                if(c.getNumFicha() == numFicha) System.out.println(c.clone());
            }
        }
    }

    //GESTÃO DE FUNCIONÁRIOS//

    /**
     * Menu para gestão de funcionários na estação de serviço.
     */

    private void gerirFuncionarios() {
        Menu menu = new Menu("Gestão de Funcionários", new String[]{
            "Listar Serviços do dia do Funcionário",
            "Listar Check-Ups do dia do Funcionário",
            "Listar os Funcionários",
            "Registos dos Turnos"
        });
        menu.setHandler(1, ()->servDiaFuncionarios());
        menu.setHandler(2, ()->checkUpsDiaFuncionario());
        menu.setHandler(3, ()->listaFuncionarios());
        menu.setHandler(4, ()->mostrarRegistosDeTodosFuncionarios());

        menu.run();
    }

    /**
     * Mostra os registos de turnos de um funcionário com base no número do cartão.
     */
    
    private void mostrarRegistosDeTodosFuncionarios() {
        System.out.println("Introduza o número de cartão de funcionário: ");
        int numFunc;
        try {
            numFunc = sc.nextInt();
        } catch (InputMismatchException | NumberFormatException e) {
            sc.nextLine();
            System.out.println("Funcionário inválido!!!");
            return;
        }

        if (!this.funcionarios.funcionarioExiste(numFunc)) {
                System.out.println("Funcionário não encontrado.");
                return; // Volta ao início do loop para nova tentativa de login
        }

        TurnosDAO turnosDAO = TurnosDAO.getInstance();
        List<Turnos> registos = turnosDAO.getAllUniqueTurnos();

        List<Turnos> registosFuncionario = new ArrayList<>();

        for (Turnos registo : registos) {
            if (registo.getCartaoFuncionario() == numFunc) {
                registosFuncionario.add(registo);
            }
        }

        if (!registosFuncionario.isEmpty()) {
            System.out.println("Registos de turnos do funcionário " + numFunc + ":");
            for (Turnos registo : registosFuncionario) {
                System.out.println(registo);
            }
        } else {
            System.out.println("Nenhum registo de turno encontrado para o funcionário " + numFunc + ".");
        }
    }

    /**
     * Lista os serviços do dia de um funcionário com base no número do cartão.
     */

    private void servDiaFuncionarios() {
        System.out.println("Introduza o número do cartão do funcionário: ");
        int numFunc;
        try {
            numFunc = sc.nextInt();
        } catch (InputMismatchException | NumberFormatException e) {
            sc.nextLine();
            System.out.println("Funcionário inválido!!!");
            return;
        }

         if (!this.funcionarios.funcionarioExiste(numFunc)) {
                System.out.println("Funcionário não encontrado.");
                return; // Volta ao início do loop para nova tentativa de login
        }

        System.out.println("Lista dos serviços do dia:");
        for (Servico s : this.servicos.getServicos().values()) {
            if(s.getFuncResponsavel() == numFunc && (s.getEstado() == Estado.AGENDADO || s.getEstado() == Estado.EM_ANDAMENTO)) 
                System.out.println(s.getNumServiço());
        }
    }

    /**
     * Lista os check-ups do dia de um funcionário com base no número do cartão.
     */

    private void checkUpsDiaFuncionario() {
        System.out.println("Introduza o número do cartão do funcionário: ");
        int numFunc;
        try {
            numFunc = sc.nextInt();
        } catch (InputMismatchException | NumberFormatException e) {
            sc.nextLine();
            System.out.println("Funcionário inválido!!!");
            return;
        }

         if (!this.funcionarios.funcionarioExiste(numFunc)) {
                System.out.println("Funcionário não encontrado.");
                return; // Volta ao início do loop para nova tentativa de login
        }
        System.out.println("Lista dos check-ups do dia:");
        for (CheckUp c : this.checkups.getCheckUps().values()) {
            if(c.getFuncResponsavel() == numFunc && (c.getEstado() == Estado.AGENDADO || c.getEstado() == Estado.EM_ANDAMENTO))
                System.out.println(c.getNumCheckUp());
        }
    }

    /**
     * Lista todos os funcionários registados na estação de serviço.
     */

    private void listaFuncionarios(){
        if (this.funcionarios.getFuncionarios().values().isEmpty()) {
            System.out.println("Não há funcionários registados.");
        } else {
            System.out.println("Lista de todos os funcionários:");
            for (Funcionario f : this.funcionarios.getFuncionarios().values()) {
                System.out.println(f.clone());
            }
        }
    }

    //SISTEMA DO MECÂNICO//

    /**
     * Menu para o sistema do mecânico na estação de serviço.
     */

    private void sistemaMecanico() {
        Menu menu = new Menu(new String[]{
                "Iniciar Serviço",
                "Finalizar Serviço e Notificar o Cliente",
                "Iniciar Check-Up",
                "Finalizar Check-Up e Notificar o Cliente",
                "Serviços do Dia",
                "CheckUps do Dia"
        });

        menu.setHandler(1, ()->iniciarServico());
        menu.setHandler(2, ()->finalizarEnotificar());
        menu.setHandler(3, ()->iniciarCheckUps());
        menu.setHandler(4, ()->finalizarCheckUps());
        menu.setHandler(5, ()->servDiaFuncionarios());
        menu.setHandler(6, ()->checkUpsDiaFuncionario());

        menu.run();
    }

    /**
     * Inicia um serviço na estação de serviço com base no número do serviço.
     */

    private void iniciarServico() {
        System.out.println("Introduza o número do serviço a iniciar: ");
        int numServico;
        try {
            numServico = sc.nextInt();
        } catch (InputMismatchException | NumberFormatException e) {
            sc.nextLine();
            System.out.println("Serviço inválido!!!");
            return;
        }
        
        if(this.servicos.getServicos().get(numServico) != null)
        {
            System.out.println("Introduza o seu cartão a iniciar: ");
            int numFunc;
            try {
                numFunc = sc.nextInt();
            } catch (InputMismatchException | NumberFormatException e) {
                sc.nextLine();
                System.out.println("Funcionário inválido!!!");
                return;
            }

            Servico servico = this.servicos.getServicos().get(numServico);

            if (servico.getEstado() != Estado.CONCLUÍDO){
                if (servico != null && servico.getFuncResponsavel() == numFunc) {
                    servico.setEstado(Estado.EM_ANDAMENTO);

                    ServicoDAO servicoDAO = ServicoDAO.getInstance(); 
                    servicoDAO.atualizarEstadoServico(servico); 

                    servico.setSms("Serviço " + numServico + " iniciado com sucesso.");

                    servicoDAO.atualizarSMSServico(servico);
                    System.out.println("Serviço " + numServico + " iniciado com sucesso.");
                } else {
                    System.out.println("Serviço não encontrado para iniciar.");
                }
            }
        }
        else System.out.println("Serviço não encontrado para iniciar.");
    }

    /**
     * Finaliza um serviço na estação de serviço e notifica o cliente correspondente.
     */
    
    private void finalizarEnotificar() {
        VeiculoFacade v = new VeiculoFacade();
        ClienteFacade c = new ClienteFacade();
        System.out.println("Introduza o número do serviço a finalizar: ");
        int numServico;
        try {
            numServico = sc.nextInt();
        } catch (InputMismatchException | NumberFormatException e) {
            sc.nextLine();
            System.out.println("Serviço inválido!!!");
            return;
        }
        
        if(this.servicos.getServicos().get(numServico) != null){
            System.out.println("Introduza o seu cartão a iniciar: ");
            int numFunc;
            try {
                numFunc = sc.nextInt();
            } catch (InputMismatchException | NumberFormatException e) {
                sc.nextLine();
                System.out.println("Funcionário inválido!!!");
                return;
            }
        
            this.servicos.finalizarServico(numServico, numFunc);
            this.servicos.notificarClienteFimServico(numServico, v, c);
        }
        else System.out.println("Serviço não encontrado para finalizar.");
    }

    /**
     * Inicia um check-up na estação de serviço com base no número do check-up.
     */

    private void iniciarCheckUps() {
        System.out.println("Introduza o número do Check-Up a iniciar: ");
        int numCheckUp;
        try {
            numCheckUp = sc.nextInt();
        } catch (InputMismatchException | NumberFormatException e) {
            sc.nextLine();
            System.out.println("Check-Up inválido!!!");
            return;
        }
        if(checkups.getCheckUps().get(numCheckUp) != null){
            System.out.println("Introduza o seu cartão a iniciar: ");
            int numFunc;
            try {
                numFunc = sc.nextInt();
            } catch (InputMismatchException | NumberFormatException e) {
                sc.nextLine();
                System.out.println("Funcionário inválido!!!");
                return;
            }
        
            CheckUp checkUp = this.checkups.getCheckUps().get(numCheckUp);
            if (checkUp.getEstado() != Estado.CONCLUÍDO){
                if (checkUp != null && checkUp.getFuncResponsavel() == numFunc) {
                    checkUp.setEstado(Estado.EM_ANDAMENTO);

                    CheckUpDAO checkUpDAO = CheckUpDAO.getInstance(); 
                    checkUpDAO.atualizarEstadoCheckUp(checkUp); 
                    System.out.println("Check-Up " + numCheckUp + " iniciado com sucesso.");
                } else {
                    System.out.println("Check-Up não encontrado para iniciar.");
                }
            }
        } else System.out.println("Check-Up não encontrado para iniciar.");
    }

    /**
     * Finaliza um check-up na estação de serviço e notifica o cliente correspondente.
     */

    private void finalizarCheckUps() {
        VeiculoFacade v = new VeiculoFacade();
        ClienteFacade cliente = new ClienteFacade();
        System.out.println("Introduza o número do Check-Up a finalizar: ");
        int numCheckUp;
        try {
            numCheckUp = sc.nextInt();
        } catch (InputMismatchException | NumberFormatException e) {
            sc.nextLine();
            System.out.println("Check-Up inválido!!!");
            return;
        }
        System.out.println("Introduza o seu cartão a iniciar: ");
        int numFunc;
            try {
                numFunc = sc.nextInt();
            } catch (InputMismatchException | NumberFormatException e) {
                sc.nextLine();
                System.out.println("Funcionário inválido!!!");
                return;
            }
        CheckUp c = checkups.getCheckUps().get(numCheckUp);
        if (c != null && c.getFuncResponsavel() == numFunc && c.getEstado() == Estado.EM_ANDAMENTO){
            c.setEstado(Estado.CONCLUÍDO);

            CheckUpDAO checkUpDAO = CheckUpDAO.getInstance(); 
            checkUpDAO.atualizarEstadoCheckUp(c); 
            System.out.println("Introduza o número de serviços a agendar: ");
            int num;
            try {
                num = sc.nextInt();
            } catch (InputMismatchException | NumberFormatException e) {
                sc.nextLine();
                System.out.println("Número inválido!!!");
                return;
            }
            

            if (num > 0){
                for (int i = 0; i < num; i++){
                    criarAgendarServico();
                }
                System.out.println("Serviço(s) agendado(s) com sucesso!");
                System.out.println("Check-Up " + numCheckUp + " concluído com sucesso.");
                this.checkups.notificarClienteFimServico(numCheckUp, v, cliente);
            }
        }
        else System.out.println("Check-Up não foi encontrado!");
    } 
}
