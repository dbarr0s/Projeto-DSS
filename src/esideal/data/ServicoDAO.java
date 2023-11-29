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
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import esideal.station.servico.Estado;
import esideal.station.servico.Servico;
import esideal.station.servico.TipoServico;

public class ServicoDAO implements Map<Integer, Servico>{
        private static ServicoDAO singleton = null;

    public ServicoDAO(){
        try{ Connection connection = DriverManager.getConnection(ConfigDAO.URL, ConfigDAO.USERNAME, ConfigDAO.PASSWORD);
            Statement stm = connection.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS servicos(" + 
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

    public static ServicoDAO getInstance(){
        if(ServicoDAO.singleton == null){
            ServicoDAO.singleton = new ServicoDAO();
        }
        return ServicoDAO.singleton;
    }

    @Override
    public int size() {
        int i = 0;
        try (Connection conn = DriverManager.getConnection(ConfigDAO.URL, ConfigDAO.USERNAME, ConfigDAO.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT count(*) FROM servicos;")) {
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
                     stm.executeQuery("SELECT Id FROM servicos WHERE NumServico='"+key+"'")) {
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
        Servico s = (Servico) value;
        return this.containsKey(s.getNumServiço());
    }

    @Override
    public Servico get(Object key) {
        Servico s = null;
        try (Connection conn = DriverManager.getConnection(ConfigDAO.URL, ConfigDAO.USERNAME, ConfigDAO.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT * FROM servicos WHERE NumServico='"+key+"'")) {
            if(rs.next()){
                int numServico = rs.getInt("NumServico");
                int numCheckUp = rs.getInt("NumCheckUp");
                int numFicha = rs.getInt("NumFicha");
                int funcResponsvel = rs.getInt("FuncResponsavel");
                String matricula = rs.getString("Matricula");
                Float custoServico = rs.getFloat("CustoServico");
                String estado = rs.getString("Estado");
                Timestamp hInicio = rs.getTimestamp("HInicio");
                Timestamp hFim = rs.getTimestamp("HFim");
                String sms = rs.getString("sms");
                String tipoServico = rs.getString("TipoServico");

                Estado e = Estado.valueOf(estado);
                LocalDateTime l = hInicio.toLocalDateTime();
                LocalDateTime l1 = hFim.toLocalDateTime();
                TipoServico t = TipoServico.valueOf(tipoServico);

                s = new Servico(numServico, numCheckUp, numFicha, funcResponsvel, matricula, custoServico, e, l, l1, sms, t);
            }
        } catch (SQLException e) {
            // Database error!
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return s;
    }

    @Override
    public Servico put(Integer key, Servico value) {
        try (Connection conn = DriverManager.getConnection(ConfigDAO.URL, ConfigDAO.USERNAME, ConfigDAO.PASSWORD);
             Statement stm = conn.createStatement()) {
            try (PreparedStatement pstm = conn.prepareStatement("INSERT INTO servicos (NumServico,NumCheckUp,NumFicha,FuncResponsavel,Matricula,CustoServico,Estado,HInicio,HFim,sms,TipoServico) VALUES (?, ?, ?, ?, ?)")){
                pstm.setInt(1,value.getNumServiço());
                pstm.setInt(2,value.getNumCheckUp());
                pstm.setInt(3,value.getNumFicha());
                pstm.setInt(4,value.getFuncResponsavel());
                pstm.setString(5, value.getMatricula());
                pstm.setFloat(6, value.getCustServiço());
                pstm.setString(7, value.getEstado().toString());
                pstm.setTimestamp(8, Timestamp.valueOf(value.getHoraInicio()));
                pstm.setTimestamp(9, Timestamp.valueOf(value.getHoraFim()));
                pstm.setString(10, value.getSms());
                pstm.setString(11, value.getTipoServico().toString());

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
    public Servico remove(Object key) {
        Servico s = null;
        try (Connection conn = DriverManager.getConnection(ConfigDAO.URL, ConfigDAO.USERNAME, ConfigDAO.PASSWORD);
             Statement stm = conn.createStatement()) {
            try (PreparedStatement pstm = conn.prepareStatement("DELETE FROM servicos WHERE NumServico = ?")){
                s = this.get(key);
                pstm.setString(1,(String)key);
                pstm.executeUpdate(); 
            }
        }catch (SQLException e) {
            // Database error!
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return s;
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends Servico> m) {
    //Este método recebe (Map) como argumento, onde as chaves são do tipo String e os valores são do tipo Veiculo
        for(Servico s : m.values()) {
            this.put(s.getNumServiço(), s.clone());
        }
    }

    @Override
    public void clear() {
        try (Connection conn = DriverManager.getConnection(ConfigDAO.URL, ConfigDAO.USERNAME, ConfigDAO.PASSWORD);
             Statement stm = conn.createStatement()) {
            stm.executeUpdate("TRUNCATE servicos");
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
             ResultSet rs = stm.executeQuery("SELECT NumServico FROM servicos")) {
            while (rs.next()) {
                int idc = rs.getInt("NumServico");
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
    public Collection<Servico> values() {
        Collection<Servico> res = new HashSet<>();
        try (Connection conn = DriverManager.getConnection(ConfigDAO.URL, ConfigDAO.USERNAME, ConfigDAO.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT * FROM servicos")) { // Seleciona todas as colunas da tabela veiculos
            while (rs.next()) {
                int numServico = rs.getInt("NumServico");
                Servico s = this.get(numServico);
                res.add(s);  
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return res;
    }

    @Override
    public Set<Entry<Integer, Servico>> entrySet() {
        Set<Entry<Integer, Servico>> entrySet = new HashSet<>();
        try (Connection conn = DriverManager.getConnection(ConfigDAO.URL, ConfigDAO.USERNAME, ConfigDAO.PASSWORD);
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("SELECT * FROM servicos")) { // Seleciona todas as colunas da tabela veiculos
            while (rs.next()) {
                int numServico = rs.getInt("NumServico");
                entrySet.add(new AbstractMap.SimpleEntry<>(numServico, this.get(numServico)));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return entrySet;
    }
}