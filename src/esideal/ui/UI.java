package esideal.ui;

import java.time.LocalDateTime;
import java.util.Scanner;

import esideal.station.funcionario.*;
import esideal.station.servico.*;
import esideal.station.checkup.*;
import esideal.station.ficha.FichaFacade;
import esideal.station.ficha.FichaVeiculo;

public class UI {
    static int IDFuncionario;
    static Scanner s = new Scanner(System.in);
    static Funcionario funcionario;
    static FuncFacade funcionarios = new FuncFacade();
    static ServicoFacade servicos = new ServicoFacade();
    static CheckUpFacade checkups = new CheckUpFacade();
    static FichaFacade fichas = new FichaFacade();

    public static void menuLogin() {
        int IDFuncionario;

        System.out.println("----------BEM VINDO----------");
        System.out.println("Introduza o seu ID de funcionário");
        IDFuncionario = s.nextInt();

        if (funcionarios.funcionarioExiste(IDFuncionario)) {
            funcionario = funcionarios.getFuncionarios().get(IDFuncionario);
            System.out.println("\u001B[32mBem vindo " + funcionario.getCartaoFuncionario() + "\u001B[42m");
            menu();
        } else {
            System.out.println("\u001B[31mFuncionário não encontrado\u001B[41m");
            menuLogin();
        }
    }

    public static void menuCriarServico() {
        System.out.println("----------NOVO SERVIÇO----------");
        int idServico = servicos.getServicos().size();
        System.out.println("Introduzir ID do checkup: ");
        int idCheckup = s.nextInt();
        System.out.println("Introduzir ID da ficha de veiculo: ");
        int idFicha = s.nextInt();
        FichaVeiculo ficha = fichas.getFichas().get(idFicha);
        String matricula = ficha.getMatricula();
        System.out.println("Introduzir custo do serviço: ");
        float custo = s.nextFloat();
        Estado estado = Estado.AGENDADO;
        System.out.println("Introduzir hora de inicio: ");
        String horaInicio = s.next();
        LocalDateTime horaInicioServico = LocalDateTime.parse(horaInicio);
        LocalDateTime horaFimServico = null;
        String sms = null;
        System.out.println("Introduzir tipo de serviço: ");
        String tipoServico = s.next();
        TipoServico tipoServicoEnum = TipoServico.valueOf(tipoServico);
        servicos.criarNovoServicoEAgendar(idServico, idCheckup, idFicha, IDFuncionario, matricula, custo, estado, horaInicioServico, horaFimServico, sms, tipoServicoEnum, funcionarios);
        System.out.println("\u001B[32mServiço criado com sucesso\u001B[42m");
    }

    public static void mostrarServicos() {
        System.out.println("----------SERVIÇOS----------");
        for (Servico servico : servicos.getServicos().values()) {
            System.out.println("ID do serviço: " + servico.getNumServiço());
            System.out.println("ID do checkup: " + servico.getNumCheckUp());
            System.out.println("ID da ficha de veiculo: " + servico.getNumFicha());
            System.out.println("ID do funcionário responsável: " + servico.getFuncResponsavel());
            System.out.println("Matricula do veiculo: " + servico.getMatricula());
            System.out.println("Custo do serviço: " + servico.getCustServiço());
            System.out.println("Estado do serviço: " + servico.getEstado());
            System.out.println("Hora de inicio do serviço: " + servico.getHoraInicio());
            System.out.println("Hora de fim do serviço: " + servico.getHoraFim());
            System.out.println("SMS: " + servico.getSms());
            System.out.println("Tipo de serviço: " + servico.getTipoServico());
            System.out.println("----------------------------");
        }
    }
    
    public static void menu() {
        System.out.println("1. Novo serviço"); 
        System.out.println("2. Serviços");

        String opcao = s.next();

        switch (opcao) {
            case "1":
                menuCriarServico();
                menu();
                break;

            case "2":
                mostrarServicos();
                menu();
                break;

            default:
                System.out.println("Opção inválida");
                menu();
                break;
        }
    }

    public static void start(){
        menuLogin();
    }
}
