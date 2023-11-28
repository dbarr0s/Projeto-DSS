package esideal.station.ficha;

import java.util.Map;

import esideal.station.checkup.CheckUp;
import esideal.station.servico.Servico;

public class FichaVeiculo {
    private int numFicha;
    private String nomeCliente;
    private String nomeVeiculo;
    private Map<Integer, Servico> servicos;
    private Map<Integer, CheckUp> checkups;

    /*CONSTRUTORES*/    

    public FichaVeiculo(int numFicha, String nomeCliente, String nomeVeiculo, Map<Integer, Servico> servicos, Map<Integer, CheckUp> checkups){
        this.numFicha = numFicha;
        this.nomeCliente = nomeCliente;
        this.nomeVeiculo = nomeVeiculo;
        this.servicos = servicos;
        this.checkups = checkups;
    }

    public FichaVeiculo(FichaVeiculo f){
        this.numFicha = f.getNumFicha();
        this.nomeCliente = f.getNomeCliente();
        this.nomeVeiculo = f.getNomeVeiculo();
        this.servicos = f.getServicos();
        this.checkups = f.getCheckups();
    }

    /*GETTERS*/

    public int getNumFicha(){
        return this.numFicha;
    }

    public String getNomeCliente(){
        return this.nomeCliente;
    }

    public String getNomeVeiculo(){
        return this.nomeVeiculo;
    }

    public Map<Integer, Servico> getServicos(){
        return this.servicos;
    }

    public Map<Integer, CheckUp> getCheckups(){
        return this.checkups;
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
        return nomeCliente == f.nomeCliente &&
        numFicha == f.numFicha &&
        nomeVeiculo == f.nomeVeiculo;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Nº da Ficha: ").append(numFicha).append("\n");
        sb.append("Dono do Veículo: ").append(nomeCliente).append("\n");
        sb.append("Veículo: ").append(nomeVeiculo).append("\n");
        sb.append("Serviços do veículo: ").append(servicos.keySet()).append("\n");
        sb.append("Check-Up's do veículo: ").append(checkups.keySet()).append("\n");
        return sb.toString();
    }
}
