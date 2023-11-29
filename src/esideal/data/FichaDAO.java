package esideal.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.AbstractMap;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import esideal.station.checkup.CheckUp;
import esideal.station.ficha.FichaVeiculo;
import esideal.station.servico.Servico;

public class FichaDAO implements Map<Integer, FichaVeiculo>{
        private static FichaDAO singleton = null;

    public FichaDAO(){
        try{
            Connection connection = DriverManager.getConnection(ConfigDAO.URL, ConfigDAO.USERNAME, ConfigDAO.PASSWORD);
            Statement stm = connection.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS fichas(" + 
                        "NumFicha INT NOT NULL AUTO_INCREMENT, " +
                        "Matricula VARCHAR(50) NOT NULL, " +
                        "NomeDono VARCHAR(100) NOT NULL, " +
                        "NomeVeiculo VARCHAR(50) NOT NULL, " +
                        "FOREIGN KEY (Matricula) REFERENCES veiculos(Matricula), " +
                        "FOREIGN KEY (Dono) REFERENCES clientes(Dono), " +
                        "PRIMARY KEY (NumFicha));";
            stm.executeUpdate(sql);

            sql = "CREATE TABLE IF NOT EXISTS checkups(" + 
                "NumCheckUp INT NOT NULL AUTO_INCREMENT, " +
                "NumFicha INT NOT NULL AUTO_INCREMENT," +
                "FuncResponsavel INT NOT NULL, " +
                "Matricula VARCHAR(50) NOT NULL, " +
                "DataCheckUp DATETIME NOT NULL, " +
                "Estado VARCHAR(50) NOT NULL, " +
                "FOREIGN KEY (NumFicha) REFERENCES fichas(NumFicha), " +
                "FOREIGN KEY (FuncResponsavel) REFERENCES funcionarios(FuncResponsavel), " +
                "FOREIGN KEY (Matricula) REFERENCES veiculos(Matricula), " +
                "PRIMARY KEY (NumCheckUp));";
            stm.executeUpdate(sql);
            
            sql = "CREATE TABLE IF NOT EXISTS servicos(" + 
                "NumServico INT NOT NULL AUTO_INCREMENT, " +
                "NumCheckUp INT NOT NULL AUTO_INCREMENT, " +
                "NumFicha INT NOT NULL AUTO_INCREMENT," +
                "FuncResponsavel INT NOT NULL, " +
                "Matricula VARCHAR(50) NOT NULL, " +
                "CustoServico FLOAT NOT NULL, " +
                "Estado VARCHAR(50) NOT NULL, " +
                "HInicio DATETIME NOT NULL, " +
                "HFim DATETIME NOT NULL, " +
                "sms VARCHAR(100) NOT NULL, " +
                "TipoServico VARCHAR(50) NOT NULL, " +
                "FOREIGN KEY (NumFicha) REFERENCES fichas(NumFicha), " +
                "FOREIGN KEY (NumCheckUp) REFERENCES checkups(NumCheckUp), " +
                "FOREIGN KEY (FuncResponsavel) REFERENCES funcionarios(FuncResponsavel), " +
                "FOREIGN KEY (Matricula) REFERENCES veiculos(Matricula), " +
                "PRIMARY KEY (NumServico));";
            stm.executeUpdate(sql);
        }
        catch(SQLException e){
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
    }

    public static FichaDAO getInstance(){
        if(FichaDAO.singleton == null){
            FichaDAO.singleton = new FichaDAO();
        }
        return FichaDAO.singleton;
    }

    @Override
    public int size() {
        int i = 0;
        try (Connection conn = DriverManager.getConnection(ConfigDAO.URL, ConfigDAO.USERNAME, ConfigDAO.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT count(*) FROM fichas;")) {
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
                     stm.executeQuery("SELECT Id FROM fichas WHERE NumFicha='"+key+"'")) {
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
        FichaVeiculo f = (FichaVeiculo) value;
        return this.containsKey(f.getNumFicha());
    }

    @Override
    public FichaVeiculo get(Object key) {
        FichaVeiculo f = null;
        try (Connection conn = DriverManager.getConnection(ConfigDAO.URL, ConfigDAO.USERNAME, ConfigDAO.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT * FROM fichas WHERE NumFicha='"+key+"'")) {
            if(rs.next()){
                int numFicha = rs.getInt("NumFicha");
                String matricula = rs.getString("Matricula");
                String nome = rs.getString("NomeDono");
                String nomeVeic = rs.getString("NomeVeiculo");

                Map<Integer, Servico> servicos = new HashMap<>();
                String sql = "SELECT * FROM servicos WHERE NumFicha='"+key+"';";

                try(ResultSet r = stm.executeQuery(sql)){
                    while(r.next()){
                        servicos.put(r.getInt("NumServico"), ServicoDAO.getInstance().get(r.getInt("NumFicha")));
                    }
                }

                Map<Integer, CheckUp> checkups = new HashMap<>();
                sql = "SELECT * FROM checkups WHERE NumFicha='"+key+"';";

                try(ResultSet r = stm.executeQuery(sql)){
                    while(r.next()){
                        checkups.put(r.getInt("NumCheckUp"), CheckUpDAO.getInstance().get(r.getInt("NumFicha")));
                    }
                }
                f = new FichaVeiculo(numFicha, matricula, nome, nomeVeic, servicos, checkups);
            }
        } catch (SQLException e) {
            // Database error!
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return f;
    }

    @Override
    public FichaVeiculo put(Integer key, FichaVeiculo value) {
        try (Connection conn = DriverManager.getConnection(ConfigDAO.URL, ConfigDAO.USERNAME, ConfigDAO.PASSWORD);
             Statement stm = conn.createStatement()) {
            try (PreparedStatement pstm = conn.prepareStatement("INSERT INTO fichas (NumFicha,Matricula,NomeDono,NomeVeiculo) VALUES (?, ?, ?, ?, ?)")){
                pstm.setInt(1,value.getNumFicha());
                pstm.setString(2,value.getMatricula());
                pstm.setString(3,value.getNomeCliente());
                pstm.setString(4,value.getNomeVeiculo()); 

                Map<Integer, Servico> servAExecutar = value.getServicos();
                Map<Integer, CheckUp> checkUpsAExecutar = value.getCheckups();
                pstm.executeUpdate();     
            
                try (PreparedStatement pstm1 = conn.prepareStatement("INSERT INTO servicos (NumServico,NumCheckUp,NumFicha,FuncResponsvel,Matricula,CustoServico,Estado,HInicio,HFim,sms,TipoServico) VALUES (?, ?, ?, ?, ?)")){
                    for(Servico s : servAExecutar.values()){
                        pstm1.setInt(1,s.getNumServiço());
                        pstm1.setInt(2,s.getNumCheckUp());
                        pstm1.setInt(3,s.getNumFicha());
                        pstm1.setInt(4,s.getFuncResponsavel());
                        pstm1.setString(5, s.getMatricula());
                        pstm1.setFloat(6, s.getCustServiço());
                        pstm1.setString(7, s.getEstado().toString());
                        pstm1.setTimestamp(8, Timestamp.valueOf(s.getHoraInicio()));
                        pstm1.setTimestamp(9, Timestamp.valueOf(s.getHoraFim()));
                        pstm1.setString(10, s.getSms());
                        pstm1.setString(11, s.getTipoServico().toString());           
                        pstm1.executeUpdate(); 
                    }
                }          
                try(PreparedStatement pstm2 = conn.prepareStatement("INSERT INTO checkups (NumCheckUp,NumFicha,FuncResponsavel,Matricula,DataCheckUp,Estado) VALUES (?, ?, ?, ?, ?)")){
                    for(CheckUp c : checkUpsAExecutar.values()){
                        pstm2.setInt(1,c.getNumCheckUp());
                        pstm2.setInt(2,c.getNumFicha());
                        pstm2.setInt(3,c.getFuncResponsavel());
                        pstm2.setString(4, c.getMatricula());
                        pstm2.setTimestamp(5, Timestamp.valueOf(c.getDataCheckUp()));
                        pstm2.setString(6, c.getEstado().toString()); 
                        pstm2.execute(); 
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
    public FichaVeiculo remove(Object key) {
		FichaVeiculo f = this.get(key);
        try (Connection conn = DriverManager.getConnection(ConfigDAO.URL, ConfigDAO.USERNAME, ConfigDAO.PASSWORD);
             Statement stm = conn.createStatement()){
            conn.setAutoCommit(false);

            stm.executeUpdate("DELETE FROM fichas WHERE NumFicha='"+key+"';");
            stm.executeUpdate("DELETE FROM servicos WHERE NumFicha='"+key+"';");
            stm.executeUpdate("DELETE FROM checkups WHERE NumFicha='"+key+"';");

            conn.commit();
        } catch (Exception e) {
            // Database error!
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return f;
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends FichaVeiculo> m) {
		for(FichaVeiculo f : m.values()) {
            this.put(f.getNumFicha(), f.clone());
        }
    }

    @Override
    public void clear() {
        try (Connection conn = DriverManager.getConnection(ConfigDAO.URL, ConfigDAO.USERNAME, ConfigDAO.PASSWORD);
             Statement stm = conn.createStatement()) {
            stm.executeUpdate("TRUNCATE fichas");
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
             ResultSet rs = stm.executeQuery("SELECT NumFicha FROM fichas")) {
            while (rs.next()) {
                Integer n = rs.getInt("NumFicha");
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
    public Collection<FichaVeiculo> values() {
		Collection<FichaVeiculo> res = new HashSet<>();
        try (Connection conn = DriverManager.getConnection(ConfigDAO.URL, ConfigDAO.USERNAME, ConfigDAO.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT NumFicha FROM fichas")) { 
            while (rs.next()) {
                int n = rs.getInt("NumFicha");
                FichaVeiculo f = this.get(n);
                res.add(f);                              
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return res;
    }

    @Override
    public Set<Entry<Integer, FichaVeiculo>> entrySet() {
        Set<Entry<Integer, FichaVeiculo>> entries = new HashSet<>();
    
        try (Connection conn = DriverManager.getConnection(ConfigDAO.URL, ConfigDAO.USERNAME, ConfigDAO.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT * FROM fichas")) {
    
            while (rs.next()) {
                int n = rs.getInt("NumFicha");
                entries.add(new AbstractMap.SimpleEntry<>(n, this.get(n)));
            }
        } catch (SQLException e) {
            // Handle exception
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
    
        return entries;
    }
}
