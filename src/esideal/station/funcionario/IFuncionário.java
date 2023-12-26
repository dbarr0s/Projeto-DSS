package esideal.station.funcionario;

import java.util.Map;

import esideal.station.checkup.CheckUp;
import esideal.station.servico.Servico;

public interface IFuncionário {
    boolean funcionarioExiste(int cartaoFuncionario);
    Map<Integer, Funcionario> getFuncionarios();
    Map<Integer, CheckUp> obterCheckUpsDoFuncionario(int idFuncionario);
    Map<Integer, Servico> obterServicosDoFuncionario(int idFuncionario);
}
