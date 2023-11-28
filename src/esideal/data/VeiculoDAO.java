package esideal.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.AbstractMap;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import esideal.station.veiculo.TipoMotor;
import esideal.station.veiculo.TipoVeiculo;
import esideal.station.veiculo.Veiculo;

public class VeiculoDAO implements Map<String, Veiculo>{
        private static VeiculoDAO singleton = null;

    public VeiculoDAO(){
        try{
            Connection connection = DriverManager.getConnection(ConfigDAO.URL, ConfigDAO.USERNAME, ConfigDAO.PASSWORD);
            Statement stm = connection.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS veiculos(" + 
                        "Matricula VARCHAR(50) NOT NULL, " +
                        "Dono VARCHAR(50) NOT NULL, " +
                        "Nome VARCHAR(50) NOT NULL, " +
                        "TVeiculo VARCHAR(50) NOT NULL, " +
                        "TMotor VARCHAR(50) NOT NULL, " +
                        "PRIMARY KEY (Matricula));";
            stm.executeUpdate(sql);
        }
        catch(SQLException e){
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
    }

    public static VeiculoDAO getInstance(){
        if(VeiculoDAO.singleton == null){
            VeiculoDAO.singleton = new VeiculoDAO();
        }
        return VeiculoDAO.singleton;
    }

    @Override
    public int size() {
        int i = 0;
        try (Connection conn = DriverManager.getConnection(ConfigDAO.URL, ConfigDAO.USERNAME, ConfigDAO.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT count(*) FROM veiculos;")) {
            if(rs.next()) {
                i = rs.getInt(1);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return i;
    }

    @Override
    public boolean isEmpty() {
        return this.size() == 0;
    }

    @Override
    public boolean containsKey(Object key) {
        boolean r;
        try (Connection conn = DriverManager.getConnection(ConfigDAO.URL, ConfigDAO.USERNAME, ConfigDAO.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs =
                     stm.executeQuery("SELECT Id FROM veiculos WHERE Matricula='"+key+"'")) {
            r = rs.next();
        } catch (SQLException e) {
            // Database error!
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return r;
    }

    @Override
    public boolean containsValue(Object value) {
        Veiculo v = (Veiculo) value;
        return this.containsKey(v.getMatricula());
    }

    @Override
    public Veiculo get(Object key) {
        Veiculo v = null;
        try (Connection conn = DriverManager.getConnection(ConfigDAO.URL, ConfigDAO.USERNAME, ConfigDAO.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT * FROM veiculos WHERE Matricula='"+key+"'")) {
            if(rs.next()){
                String matricula = rs.getString("Matricula");
                String dono = rs.getString("Dono");
                String nome = rs.getString("Nome");
                String tveiculo = rs.getString("TVeiculo");
                String tmotor = rs.getString("TMotor");

                TipoVeiculo t = TipoVeiculo.valueOf(tveiculo);
                TipoMotor m = TipoMotor.valueOf(tmotor);

                v = new Veiculo(nome, dono, matricula, t, m);
            }
        } catch (SQLException e) {
            // Database error!
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return v;
    }

    @Override
    public Veiculo put(String key, Veiculo value) {
        try (Connection conn = DriverManager.getConnection(ConfigDAO.URL, ConfigDAO.USERNAME, ConfigDAO.PASSWORD);
             Statement stm = conn.createStatement()) {
            try (PreparedStatement pstm = conn.prepareStatement("INSERT INTO veiculos (Matricula,Dono,Nome,TVeiculo,TMotor) VALUES (?, ?, ?, ?, ?)")){
                pstm.setString(1,value.getMatricula());
                pstm.setString(2,value.getDono());
                pstm.setString(3, value.getNome());
                pstm.setString(4, value.getTipoVeiculo().toString());
                pstm.setString(5, value.getTipoMotor().toString());
                pstm.executeUpdate(); 
            }
        }catch (SQLException e) {
            // Database error!
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return value;
    }

    @Override
    public Veiculo remove(Object key) {
        Veiculo v = null;
        try (Connection conn = DriverManager.getConnection(ConfigDAO.URL, ConfigDAO.USERNAME, ConfigDAO.PASSWORD);
             Statement stm = conn.createStatement()) {
            try (PreparedStatement pstm = conn.prepareStatement("DELETE FROM veiculos WHERE Matricula = ?")){
                v = this.get(key);
                pstm.setString(1,(String)key);
                pstm.executeUpdate(); 
            }
        }catch (SQLException e) {
            // Database error!
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return v;
    }

    @Override
    public void putAll(Map<? extends String, ? extends Veiculo> m) { 
    //Este método recebe (Map) como argumento, onde as chaves são do tipo String e os valores são do tipo Veiculo
        for(Veiculo v : m.values()) {
            this.put(v.getMatricula(), v.clone());
        }
    }

    @Override
    public void clear() {
        try (Connection conn = DriverManager.getConnection(ConfigDAO.URL, ConfigDAO.USERNAME, ConfigDAO.PASSWORD);
             Statement stm = conn.createStatement()) {
            stm.executeUpdate("TRUNCATE veiculos");
        } catch (SQLException e) {
            // Database error!
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
    }

    @Override
    public Set<String> keySet() {
        Set<String> res = new HashSet<>();
        try (Connection conn = DriverManager.getConnection(ConfigDAO.URL, ConfigDAO.USERNAME, ConfigDAO.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT Matricula FROM veiculos")) {
            while (rs.next()) {
                String idc = rs.getString("Matricula");
                res.add(idc);
            }
        } catch (Exception e) {
            // Database error!
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return res;
    }

    @Override
    public Collection<Veiculo> values() {
        Collection<Veiculo> res = new HashSet<>();
        try (Connection conn = DriverManager.getConnection(ConfigDAO.URL, ConfigDAO.USERNAME, ConfigDAO.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT * FROM veiculos")) { // Seleciona todas as colunas da tabela veiculos
            while (rs.next()) {
                String matricula = rs.getString("Matricula");
                String dono = rs.getString("Dono");
                String nome = rs.getString("Nome");
                String tveiculo = rs.getString("TVeiculo");
                String tmotor = rs.getString("TMotor");

                TipoVeiculo t = TipoVeiculo.valueOf(tveiculo);
                TipoMotor m = TipoMotor.valueOf(tmotor);

                Veiculo v = new Veiculo(nome, dono, matricula, t, m);
                res.add(v);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return res;
    }
    

@Override
public Set<Entry<String, Veiculo>> entrySet() {
    Set<Entry<String, Veiculo>> entrySet = new HashSet<>();
    try (Connection conn = DriverManager.getConnection(ConfigDAO.URL, ConfigDAO.USERNAME, ConfigDAO.PASSWORD);
         Statement stm = conn.createStatement();
         ResultSet rs = stm.executeQuery("SELECT * FROM veiculos")) { // Seleciona todas as colunas da tabela veiculos
        while (rs.next()) {
            String matricula = rs.getString("Matricula");
            String dono = rs.getString("Dono");
            String nome = rs.getString("Nome");
            String tveiculo = rs.getString("TVeiculo");
            String tmotor = rs.getString("TMotor");

            TipoVeiculo t = TipoVeiculo.valueOf(tveiculo);
            TipoMotor m = TipoMotor.valueOf(tmotor);

            Veiculo v = new Veiculo(nome, dono, matricula, t, m);
            Entry<String, Veiculo> entry = new AbstractMap.SimpleEntry<>(matricula, v);
            entrySet.add(entry);
        }
    } catch (SQLException e) {
        throw new RuntimeException(e);
    }
    return entrySet;
}

}
