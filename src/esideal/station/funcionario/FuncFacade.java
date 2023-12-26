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

    /**
     * Verifica se um funcionário com um determinado número de cartão está registado.
     * @param cartaoFuncionario Número de cartão do funcionário a ser verificado.
     * @return Verdadeiro se o funcionário existir; falso, caso contrário.
     */

    public boolean funcionarioExiste(int cartaoFuncionario) {
        for (Funcionario funcionario : funcionarios.values()) {
            if (funcionario.getCartaoFuncionario() == cartaoFuncionario) {
                return true;
            }
        }
        return false;
    }  

    /**
     * Obtém todos os serviços associados a um funcionário com base no seu identificador.
     * @param idFuncionario Identificador do funcionário para o qual os serviços estão sendo obtidos.
     * @return Um mapa de serviços associados ao funcionário, onde a chave é o número do serviço e o valor é o serviço correspondente.
     */

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

    /**
     * Obtém todos os check-ups associados a um funcionário com base no seu identificador.
     * @param idFuncionario Identificador do funcionário para o qual os check-ups estão sendo obtidos.
     * @return Um mapa de check-ups associados ao funcionário, onde a chave é o número do check-up e o valor é o check-up correspondente.
     */
    
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
