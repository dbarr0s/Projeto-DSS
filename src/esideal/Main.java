package esideal;

import esideal.ui.*;
import esideal.station.*;
import esideal.station.facades.StationFacade;

public class Main {
    public static void main(String[] args) {
        StationFacade estacao = new StationFacade();
        UI.start(estacao);
    }
}
