package esideal;

import esideal.ui.*;

public class Main {
    public static void main(String[] args) {
        StationFacade estacao = new StationFacade();
        UI.start(estacao);
    }
}
