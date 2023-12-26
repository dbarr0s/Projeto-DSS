package esideal.station.funcionario;

import java.util.HashMap;
import java.util.Map;

import esideal.data.FuncionarioDAO;
import esideal.station.checkup.CheckUp;
import esideal.station.checkup.CheckUpFacade;
import esideal.station.servico.Servico;
import esideal.station.servico.ServicoFacade;

public class FuncFacade implements IFuncionário{
    private Map<Integer, Funcionario> funcionarios; 

    public FuncFacade(){
        this.funcionarios = FuncionarioDAO.getInstance(); 
    }

    public Map<Integer, Funcionario> getFuncionarios() {
        return this.funcionarios;
    }

    public boolean funcionarioExiste(int cartaoFuncionario) {
        for (Funcionario funcionario : funcionarios.values()) {
            if (funcionario.getCartaoFuncionario() == cartaoFuncionario) {
                return true;
            }
        }
        return false;
    }  

    public Map<Integer, Servico> obterServicosDoFuncionario(int idFuncionario) {
        Map<Integer, Servico> servicosDoFuncionario = new HashMap<>();
        ServicoFacade s = new ServicoFacade();
    
        for (Servico servico : s.getServicos().values()) {
            if (servico.getFuncResponsavel() == idFuncionario) {
                servicosDoFuncionario.put(servico.getNumServiço(), servico.clone());
            }
        }
        return servicosDoFuncionario;
    }
    
    public Map<Integer, CheckUp> obterCheckUpsDoFuncionario(int idFuncionario) {
        Map<Integer, CheckUp> checkUpsDoFuncionario = new HashMap<>();
        CheckUpFacade c = new CheckUpFacade();
    
        for (CheckUp checkUp : c.getCheckUps().values()) {
            if (checkUp.getFuncResponsavel() == idFuncionario) {
                checkUpsDoFuncionario.put(checkUp.getNumCheckUp(), checkUp.clone());
            }
        }
    
        return checkUpsDoFuncionario;
    }
    

}
