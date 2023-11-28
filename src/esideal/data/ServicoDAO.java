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
        try{
            Connection connection = DriverManager.getConnection(ConfigDAO.URL, ConfigDAO.USERNAME, ConfigDAO.PASSWORD);
            Statement stm = connection.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS servicos(" + 
                        "NumServico INT NOT NULL AUTO_INCREMENT, " +
                        "FuncResponsvel INT NOT NULL, " +
                        "Matricula VARCHAR(50) NOT NULL, " +
                        "CustoServico FLOAT NOT NULL, " +
                        "Estado VARCHAR(50) NOT NULL, " +
                        "HInicio DATETIME NOT NULL, " +
                        "HFim DATETIME NOT NULL, " +
                        "sms VARCHAR(100) NOT NULL, " +
                        "TipoServico VARCHAR(50) NOT NULL, " +
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
                int funcResponsvel = rs.getInt("FuncResponsvel");
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

                s = new Servico(numServico, funcResponsvel, matricula, custoServico, e, l, l1, sms, t);
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
            try (PreparedStatement pstm = conn.prepareStatement("INSERT INTO servicos (NumServico,FuncResponsvel,Matricula,CustoServico,Estado,HInicio,HFim,sms,TipoServico) VALUES (?, ?, ?, ?, ?)")){
                pstm.setInt(1,value.getNumServiço());
                pstm.setInt(2,value.getFuncResponsavel());
                pstm.setString(3, value.getMatricula());
                pstm.setFloat(4, value.getCustServiço());
                pstm.setString(5, value.getEstado().toString());
                pstm.setTimestamp(6, Timestamp.valueOf(value.getHoraInicio()));
                pstm.setTimestamp(7, Timestamp.valueOf(value.getHoraFim()));
                pstm.setString(8, value.getSms());
                pstm.setString(9, value.getTipoServico().toString());
                
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
                int funcResponsvel = rs.getInt("FuncResponsvel");
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

                Servico s = new Servico(numServico, funcResponsvel, matricula, custoServico, e, l, l1, sms, t);
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
                int funcResponsvel = rs.getInt("FuncResponsvel");
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

                Servico s = new Servico(numServico, funcResponsvel, matricula, custoServico, e, l, l1, sms, t);
                Entry<Integer, Servico> entry = new AbstractMap.SimpleEntry<>(numServico, s);
                entrySet.add(entry);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return entrySet;
    }
}