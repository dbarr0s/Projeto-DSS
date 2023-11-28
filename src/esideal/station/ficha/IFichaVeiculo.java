package esideal.station.ficha;

import esideal.station.checkup.CheckUp;
import esideal.station.checkup.CheckUpFacade;
import esideal.station.servico.Servico;
import esideal.station.servico.ServicoFacade;

public interface IFichaVeiculo {
    void registoCheckUpFicha(int numCheckUp, CheckUp c, CheckUpFacade cf);
    void registoServicosFicha(int numServico, Servico s, ServicoFacade sf);   
}
