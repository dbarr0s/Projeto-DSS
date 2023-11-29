package esideal.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.AbstractMap;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import esideal.station.checkup.CheckUp;
import esideal.station.servico.Estado;
import esideal.station.servico.Servico;

public class CheckUpDAO implements Map<Integer, CheckUp>{
        private static CheckUpDAO singleton = null;

    public CheckUpDAO(){
        try{
            Connection connection = DriverManager.getConnection(ConfigDAO.URL, ConfigDAO.USERNAME, ConfigDAO.PASSWORD);
            Statement stm = connection.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS checkups(" + 
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

    public static CheckUpDAO getInstance(){
        if(CheckUpDAO.singleton == null){
            CheckUpDAO.singleton = new CheckUpDAO();
        }
        return CheckUpDAO.singleton;
    }

    @Override
    public int size() {
        int i = 0;
        try (Connection conn = DriverManager.getConnection(ConfigDAO.URL, ConfigDAO.USERNAME, ConfigDAO.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT count(*) FROM checkups;")) {
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
                     stm.executeQuery("SELECT Id FROM checkups WHERE NumCheckUp='"+key+"'")) {
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
        CheckUp c = (CheckUp) value;
        return this.containsKey(c.getNumCheckUp());
    }

    @Override
    public CheckUp get(Object key) {
        CheckUp c = null;
        try (Connection conn = DriverManager.getConnection(ConfigDAO.URL, ConfigDAO.USERNAME, ConfigDAO.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT * FROM checkups WHERE NumCheckUp='"+key+"'")) {
            if(rs.next()){
                int numCheckUp = rs.getInt("NumCheckUp");
                int numFicha = rs.getInt("NumFicha");
                int funcResponsvel = rs.getInt("FuncResponsavel");
                String matricula = rs.getString("Matricula");
                Timestamp data = rs.getTimestamp("DataCheckUp");
                String estado = rs.getString("Estado");

                LocalDateTime l = data.toLocalDateTime();
                Estado e = Estado.valueOf(estado);

                Map<Integer, Servico> servAExecutar = new HashMap<>();
                String sql = "SELECT * FROM servicos WHERE NumCheckUp='"+key+"';";

                try(ResultSet r = stm.executeQuery(sql)){
                    while(r.next()){
                        servAExecutar.put(r.getInt("NumServico"), ServicoDAO.getInstance().get(r.getInt("NumCheckUp")));
                    }
                }
                c = new CheckUp(numCheckUp, numFicha, funcResponsvel, matricula, l, e, servAExecutar);
            }
        } catch (SQLException e) {
            // Database error!
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return c;
    }

    @Override
    public CheckUp put(Integer key, CheckUp value) {
        try (Connection conn = DriverManager.getConnection(ConfigDAO.URL, ConfigDAO.USERNAME, ConfigDAO.PASSWORD);
             Statement stm = conn.createStatement()) {
            try (PreparedStatement pstm = conn.prepareStatement("INSERT INTO checkups (NumCheckUp,NumFicha,FuncResponsavel,Matricula,DataCheckUp,Estado) VALUES (?, ?, ?, ?, ?)")){
                pstm.setInt(1,value.getNumCheckUp());
                pstm.setInt(2,value.getNumFicha());
                pstm.setInt(3,value.getFuncResponsavel());
                pstm.setString(4, value.getMatricula());
                pstm.setTimestamp(5, Timestamp.valueOf(value.getDataCheckUp()));
                pstm.setString(6, value.getEstado().toString());   

                Map<Integer, Servico> servAExecutar = value.getServAExecutar();
                pstm.executeUpdate(); 

                String sql = "INSERT INTO servicos (NumServico,NumCheckUp,NumFicha,FuncResponsvel,Matricula,CustoServico,Estado,HInicio,HFim,sms,TipoServico) VALUES (?, ?, ?, ?, ?)";
                
                try(PreparedStatement pstm1 = conn.prepareStatement(sql)){
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
                        pstm1.execute();
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
    public CheckUp remove(Object key) {
		CheckUp c = this.get(key);
        try (Connection conn = DriverManager.getConnection(ConfigDAO.URL, ConfigDAO.USERNAME, ConfigDAO.PASSWORD);
             Statement stm = conn.createStatement()){
            conn.setAutoCommit(false);

            stm.executeUpdate("DELETE FROM checkups WHERE NumCheckUp='"+key+"';");
            stm.executeUpdate("DELETE FROM servicos WHERE NumCheckUp='"+key+"';");

            conn.commit();
        } catch (Exception e) {
            // Database error!
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return c;
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends CheckUp> m) {
		for(CheckUp c : m.values()) {
            this.put(c.getNumCheckUp(), c.clone());
        }
    }

    @Override
    public void clear() {
        try (Connection conn = DriverManager.getConnection(ConfigDAO.URL, ConfigDAO.USERNAME, ConfigDAO.PASSWORD);
             Statement stm = conn.createStatement()) {
            stm.executeUpdate("TRUNCATE checkups");
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
             ResultSet rs = stm.executeQuery("SELECT NumCheckUp FROM checkups")) {
            while (rs.next()) {
                Integer n = rs.getInt("NumCheckUp");
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
    public Collection<CheckUp> values() {
		Collection<CheckUp> res = new HashSet<>();
        try (Connection conn = DriverManager.getConnection(ConfigDAO.URL, ConfigDAO.USERNAME, ConfigDAO.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT NumCheckUp FROM checkups")) { 
            while (rs.next()) {
                int numCheckUp = rs.getInt("NumCheckUp");
                CheckUp c = this.get(numCheckUp);
                res.add(c);                              
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return res;
    }

    @Override
    public Set<Entry<Integer, CheckUp>> entrySet() {
        Set<Entry<Integer, CheckUp>> entries = new HashSet<>();
    
        try (Connection conn = DriverManager.getConnection(ConfigDAO.URL, ConfigDAO.USERNAME, ConfigDAO.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT * FROM checkups")) {
    
            while (rs.next()) {
                int numCheckUp = rs.getInt("NumCheckUp");
                entries.add(new AbstractMap.SimpleEntry<>(numCheckUp, this.get(numCheckUp)));
            }
        } catch (SQLException e) {
            // Handle exception
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
    
        return entries;
    }
}
