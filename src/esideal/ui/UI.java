package esideal.ui;

import java.util.Scanner;

import esideal.station.*;

public class UI {
    static Scanner s = new Scanner(System.in);
    static String logado;

    public static void menuLogin(StationFacade estacao)
    {
        String username, password;

        System.out.println("----------BEM VINDO----------");
        System.out.println("Introduza o seu username de funcionário");
        username = s.nextLine();
        System.out.println("Introduza a sua password de funcionário");
        password = s.nextLine();

        if(estacao.login(username, password)){
            logado = username;
            menu();
        }
        else menuLogin(estacao);
    }
    
    public static void menu(){
        System.out.println("1. Novo serviço");
        System.out.println("1. Serviços pendentes");
    }

    public static void start(StationFacade estacao){
        menuLogin(estacao);
    }
}
