package esideal.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.AbstractMap;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import esideal.station.cliente.Cliente;
import esideal.station.veiculo.Veiculo;

public class ClienteDAO implements Map<Integer, Cliente>{
        private static ClienteDAO singleton = null;

    public ClienteDAO(){
        try{
            Connection connection = DriverManager.getConnection(ConfigDAO.URL, ConfigDAO.USERNAME, ConfigDAO.PASSWORD);
            Statement stm = connection.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS clientes(" + 
                        "Nome VARCHAR(150) NOT NULL, " +
                        "Morada VARCHAR(100) NOT NULL, " +
                        "NIF INT NOT NULL, " +
                        "Telefone INT NOT NULL, " +
                        "Email VARCHAR(100) NOT NULL, " +
                        "Voucher BIT, " +  //0 = FALSE, 1 = TRUE//
                        "PRIMARY KEY (NIF));";
            stm.executeUpdate(sql);
            sql = "CREATE TABLE IF NOT EXISTS veiculos(" + 
                        "Matricula VARCHAR(50) NOT NULL, " +
                        "Dono VARCHAR(50) NOT NULL, " +
                        "Nome VARCHAR(50) NOT NULL, " +
                        "TVeiculo VARCHAR(50) NOT NULL, " +
                        "TMotor VARCHAR(50) NOT NULL, " +
                        "FOREIGN KEY (Dono) REFERENCES clientes(Dono), " +
                        "PRIMARY KEY (Matricula));";
            stm.executeUpdate(sql);
        }
        catch(SQLException e){
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
    }

    public static ClienteDAO getInstance(){
        if(ClienteDAO.singleton == null){
            ClienteDAO.singleton = new ClienteDAO();
        }
        return ClienteDAO.singleton;
    }

    @Override
    public int size() {
        int i = 0;
        try (Connection conn = DriverManager.getConnection(ConfigDAO.URL, ConfigDAO.USERNAME, ConfigDAO.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT count(*) FROM clientes;")) {
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
                     stm.executeQuery("SELECT Id FROM clientes WHERE NIF='"+key+"'")) {
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
        Cliente c = (Cliente) value;
        return this.containsKey(c.getNif());
    }

    @Override
    public Cliente get(Object key) {
        Cliente c = null;
        try (Connection conn = DriverManager.getConnection(ConfigDAO.URL, ConfigDAO.USERNAME, ConfigDAO.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT * FROM clientes WHERE NIF='"+key+"'")) {
            if(rs.next()){
                String nome = rs.getString("Nome");
                String morada = rs.getString("Morada");
                int NIF = rs.getInt("NIF");
                int telefone = rs.getInt("Telefone");
                String email = rs.getString("Email");
                boolean voucher = rs.getBoolean("Voucher");

                Map<String, Veiculo> veiculos = new HashMap<>();
                String sql = "SELECT * FROM veiculos WHERE Dono='"+nome+"';";

                try(ResultSet r = stm.executeQuery(sql)){
                    while(r.next()){
                        veiculos.put(r.getString("Matricula"), VeiculoDAO.getInstance().get(r.getString("Matricula")));
                    }
                }
                c = new Cliente(nome, morada, NIF, telefone, email, voucher, veiculos);
            }
        } catch (SQLException e) {
            // Database error!
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return c;
    }

    @Override
    public Cliente put(Integer key, Cliente value) {
        try (Connection conn = DriverManager.getConnection(ConfigDAO.URL, ConfigDAO.USERNAME, ConfigDAO.PASSWORD);
             Statement stm = conn.createStatement()) {
            try (PreparedStatement pstm = conn.prepareStatement("INSERT INTO clientes (Nome,Morada,NIF,Telefone,Email,Voucher) VALUES (?, ?, ?, ?, ?)")){
                pstm.setString(1,value.getNome());
                pstm.setString(2,value.getMorada());
                pstm.setInt(3, value.getNif());
                pstm.setInt(3, value.getTelefone());
                pstm.setString(5, value.getEmail());    
                pstm.setBoolean(5, value.getVoucher());   

                Map<String, Veiculo> veiculos = value.getVeiculos();
                pstm.executeUpdate(); 

                String sql = "INSERT INTO veiculos (Matricula,Dono,Nome,TVeiculo,TMotor) VALUES (?, ?, ?, ?, ?)";
                
                try(PreparedStatement pstm1 = conn.prepareStatement(sql)){
                        for(Veiculo v : veiculos.values()){
                            pstm.setString(1,v.getMatricula());
                            pstm.setString(2,v.getDono());
                            pstm.setString(3, v.getNome());
                            pstm.setString(4, v.getTipoVeiculo().toString());
                            pstm.setString(5, v.getTipoMotor().toString());
                            pstm.execute();
                        }
                }           
            }
        }catch (SQLException e) {
            // Database error!
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return value;
    }

    @Override
    public Cliente remove(Object key) {
		Cliente c = this.get(key);
        try (Connection conn = DriverManager.getConnection(ConfigDAO.URL, ConfigDAO.USERNAME, ConfigDAO.PASSWORD);
             Statement stm = conn.createStatement()){
            conn.setAutoCommit(false);

            stm.executeUpdate("DELETE FROM clientes WHERE NIF='"+key+"';");
            stm.executeUpdate("DELETE FROM veiculos WHERE Dono='"+c.getNome()+"';");

            conn.commit();
        } catch (Exception e) {
            // Database error!
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return c;
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends Cliente> m) {
		for(Cliente c : m.values()) {
            this.put(c.getNif(), c.clone());
        }
    }

    @Override
    public void clear() {
        try (Connection conn = DriverManager.getConnection(ConfigDAO.URL, ConfigDAO.USERNAME, ConfigDAO.PASSWORD);
             Statement stm = conn.createStatement()) {
            stm.executeUpdate("TRUNCATE clientes");
        } catch (SQLException e) {
            // Database error!
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
    }

    @Override
    public Set<Integer> keySet() {
        Set<Integer> res = new HashSet<>();
        try (Connection conn = DriverManager.getConnection(ConfigDAO.URL, ConfigDAO.USERNAME, ConfigDAO.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT NIF FROM clientes")) {
            while (rs.next()) {
                Integer n = rs.getInt("NIF");
                res.add(n);
            }
        } catch (Exception e) {
            // Database error!
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return res;
    }

    @Override
    public Collection<Cliente> values() {
		Collection<Cliente> res = new HashSet<>();
        try (Connection conn = DriverManager.getConnection(ConfigDAO.URL, ConfigDAO.USERNAME, ConfigDAO.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT NIF FROM clientes")) { 
            while (rs.next()) {
                int NIF = rs.getInt("NIF");
                Cliente c = this.get(NIF);
                res.add(c);                              
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return res;
    }

    @Override
    public Set<Entry<Integer, Cliente>> entrySet() {
        Set<Entry<Integer, Cliente>> entries = new HashSet<>();
    
        try (Connection conn = DriverManager.getConnection(ConfigDAO.URL, ConfigDAO.USERNAME, ConfigDAO.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT * FROM clientes")) {
    
            while (rs.next()) {
                int NIF = rs.getInt("NIF");
                entries.add(new AbstractMap.SimpleEntry<>(NIF, this.get(NIF)));
            }
        } catch (SQLException e) {
            // Handle exception
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return entries;
    }
}