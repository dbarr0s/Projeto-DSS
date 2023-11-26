package esideal.station.ficha;

import java.util.Map;

import esideal.station.checkup.CheckUp;
import esideal.station.cliente.Cliente;
import esideal.station.funcionario.Funcionario;
import esideal.station.servico.Servico;
import esideal.station.veiculo.Veiculo;

public class FichaVeiculo {
    private Cliente cliente;
    private Veiculo veiculo;
    private Funcionario funcionario;
    private Map<Integer, Servico> servExecutados;
    private Map<Integer, CheckUp> checkupExecutados;
    private Map<Integer, Servico> servAgendados;
    private Map<Integer, Servico> checkUpAgendados;    

    /*CONSTRUTORES*/    

    public FichaVeiculo(Cliente cliente, Veiculo veiculo, Funcionario funcionario, Map<Integer, Servico> servExecutados, Map<Integer, CheckUp> checkupExecutados, Map<Integer, Servico> servAgendados, Map<Integer, Servico> checkUpAgendados){
        this.cliente = cliente;
        this.veiculo = veiculo;
        this.funcionario = funcionario;
        this.servExecutados = servExecutados;
        this.checkupExecutados = checkupExecutados;
        this.servAgendados = servAgendados;
        this.checkUpAgendados = checkUpAgendados;
    }

    public FichaVeiculo(FichaVeiculo f){
        this.cliente = f.getCliente();
        this.veiculo = f.getVeiculo();
        this.funcionario = f.getFuncionario();
        this.servExecutados = f.getServExecutados();
        this.checkupExecutados = f.getCheckupExecutados();
        this.servAgendados = f.getServAgendados();
        this.checkUpAgendados = f.getCheckUpAgendados();
    }

    /*GETTERS*/

    public Cliente getCliente(){
        return this.cliente;
    }

    public Veiculo getVeiculo(){
        return this.veiculo;
    }

    public Funcionario getFuncionario(){
        return this.funcionario;
    }

    public Map<Integer, Servico> getServExecutados(){
        return this.servExecutados;
    }

    public Map<Integer, CheckUp> getCheckupExecutados(){
        return this.checkupExecutados;
    }

    public Map<Integer, Servico> getServAgendados(){
        return this.servAgendados;
    }

    public Map<Integer, Servico> getCheckUpAgendados(){
        return this.checkUpAgendados;
    }

    /*OUTROS MÉTODOS*/

    public FichaVeiculo clone(){
        return new FichaVeiculo(this);
    }

    public boolean equals(Object o){
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FichaVeiculo f = (FichaVeiculo) o;
        return cliente == f.cliente &&
        veiculo == f.veiculo &&
        funcionario == f.funcionario;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Dono do Veículo: ").append(cliente).append("\n");
        sb.append("Veículo: ").append(veiculo).append("\n");
        sb.append("Funcionário responsável pelos serviços: ").append(funcionario).append("\n");
        sb.append("Serviços Executados no veículo: ").append(servExecutados.keySet()).append("\n");
        sb.append("Check-Up's Executados no veículo: ").append(checkupExecutados.keySet()).append("\n");
        sb.append("Serviços Agendados no veículo: ").append(servAgendados.keySet()).append("\n");
        sb.append("Check-Up's Agendados no veículo: ").append(checkUpAgendados.keySet()).append("\n");
        return sb.toString();
    }
}
