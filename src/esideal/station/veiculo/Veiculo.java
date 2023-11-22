package esideal.station.veiculo;

public class Veiculo {
    private String nome;
    private String matricula;
    private TipoVeiculo tipoVeiculo;
    private TipoMotor tipoMotor;

    public Veiculo(String nome, String matricula, TipoVeiculo tipoVeiculo, TipoMotor tipoMotor) {
        this.nome = nome;
        this.matricula = matricula;
        this.tipoVeiculo = tipoVeiculo;
        this.tipoMotor = tipoMotor;
    }

    public Veiculo(Veiculo v) {
        this.nome = v.getNome();
        this.matricula = v.getMatricula();
        this.tipoVeiculo = v.getTipoVeiculo();
        this.tipoMotor = v.getTipoMotor();
    }

    public String getNome(){
        return this.nome;
    }

    public String getMatricula(){
        return this.matricula;
    }

    public TipoVeiculo getTipoVeiculo(){
        return this.tipoVeiculo;
    }

    public TipoMotor getTipoMotor(){
        return this.tipoMotor;
    }
}
