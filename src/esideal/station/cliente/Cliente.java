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
}
