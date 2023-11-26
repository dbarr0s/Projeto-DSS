package esideal.station.cliente;

import java.util.Map;
import java.util.Objects;

import esideal.station.veiculo.Veiculo;

public class Cliente {
    private String nome;
    private String morada;
    private int nif;
    private int telefone;
    private String email;
    private boolean voucher;
    private Map<String, Veiculo> veiculos;

    /*CONSTRUTORES*/

    public Cliente(String nome, String morada, int nif, int telefone, String email, boolean voucher, Map<String, Veiculo> veiculos){
        this.nome = nome;
        this.morada = morada;
        this.nif = nif;
        this.telefone = telefone;
        this.email = email;
        this.voucher = voucher;
        this.veiculos = veiculos;
    }

    public Cliente(Cliente c) {
        this.nome = c.getNome();
        this.morada = c.getMorada();
        this.nif = c.getNif();
        this.telefone = c.getTelefone();
        this.email = c.getEmail();
        this.voucher = c.getVoucher();
        this.veiculos = c.getVeiculos();
    }

    /*GETTERS*/

    public String getNome(){
        return this.nome;
    }

    public String getMorada(){
        return this.morada;
    }

    public int getNif(){
        return this.nif;
    }

    public int getTelefone(){
        return this.telefone;
    }

    public String getEmail(){
        return this.email;
    }

    public boolean getVoucher(){
        return this.voucher;
    }

    public Map<String, Veiculo> getVeiculos(){
        return this.veiculos;
    }

    /*OUTROS MÉTODOS*/

    public Cliente clone() {
        return new Cliente(this);
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Cliente c = (Cliente) o;
        return Objects.equals (nome, c.nome) && 
        Objects.equals(morada, c.morada) && 
        nif == c.nif && 
        telefone == c.telefone && 
        Objects.equals(email, c.email) && 
        voucher == c.voucher;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Nome do Cliente: ").append(nome).append("\n");
        sb.append("Morada do Cliente: ").append(morada).append("\n");
        sb.append("NIF do Cliente: ").append(nif).append("\n");
        sb.append("Nº do Cliente: ").append(telefone).append("\n");
        sb.append("E-mail do Cliente: ").append(email).append("\n");
        sb.append("Cliente tem voucher?: ").append(voucher).append("\n");
        sb.append("Lista dos veículos do Cliente: ").append(veiculos.keySet()).append("\n");
        return sb.toString();
    }
}
