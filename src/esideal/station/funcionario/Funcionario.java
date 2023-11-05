package esideal.station.funcionario;

public class Funcionario {
    private String username;
    private String password;

    public Funcionario(String username, String password){
        this.username = username;
        this.password = password;
    }

    public boolean checkPassword(String password){
        if(this.password == password) return true;
        return false;
    }

    public String getPassword(){
        return this.password;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public String getUsername(){
        return this.username;
    }

    public void setUsername(String username){
        this.username = username;
    }
}
