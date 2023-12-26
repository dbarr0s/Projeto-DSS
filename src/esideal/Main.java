package esideal;

import esideal.ui.EstacaoUI;

public class Main {
    public static void main(String[] args) {
        try {
            new EstacaoUI().run();
        }
        catch (Exception e) {
            e.printStackTrace();
            // System.out.println("Não foi possível arrancar: "+e.getMessage());
        }
    }
}
