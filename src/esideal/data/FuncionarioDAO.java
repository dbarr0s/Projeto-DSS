package esideal.data;

import java.util.AbstractMap;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.sql.*;
import java.time.LocalDateTime;

import esideal.station.checkup.CheckUp;
import esideal.station.funcionario.EstadoTurno;
import esideal.station.funcionario.Funcionario;
import esideal.station.funcionario.TipoFuncionario;
import esideal.station.servico.Servico;
import esideal.station.servico.TipoServico;

public class FuncionarioDAO implements Map<Integer, Funcionario>{
    private static FuncionarioDAO singleton = null;

    public FuncionarioDAO(){
        try{
            Connection connection = DriverManager.getConnection(ConfigDAO.URL, ConfigDAO.USERNAME, ConfigDAO.PASSWORD);
            Statement stm = connection.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS funcionarios(" + 
                        "Cartao INT NOT NULL AUTO_INCREMENT, " +
                        "EstadoTurno VARCHAR(50) NOT NULL, " +
                        "HEntrada DATETIME NOT NULL, " +
                        "HSaida DATETIME NOT NULL, " +
                        "TipoFunc VARCHAR(100) NOT NULL, " +
                        "Posto VARCHAR(100) NOT NULL, " +
                        "PRIMARY KEY (Cartao));";
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

    public static FuncionarioDAO getInstance(){
        if(FuncionarioDAO.singleton == null){
            FuncionarioDAO.singleton = new FuncionarioDAO();
        }
        return FuncionarioDAO.singleton;
    }

    @Override
    public int size() {
        int i = 0;
        try (Connection conn = DriverManager.getConnection(ConfigDAO.URL, ConfigDAO.USERNAME, ConfigDAO.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT count(*) FROM funcionarios;")) {
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
                     stm.executeQuery("SELECT Id FROM funcionarios WHERE Cartao='"+key+"'")) {
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
        Funcionario f = (Funcionario) value;
        return this.containsKey(f.getCartaoFuncionario());
    }

    @Override
    public Funcionario get(Object key) {
        Funcionario f = null;
        try (Connection conn = DriverManager.getConnection(ConfigDAO.URL, ConfigDAO.USERNAME, ConfigDAO.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT * FROM funcionarios WHERE Cartao='"+key+"'")) {
            if(rs.next()){
                int cartao = rs.getInt("Cartao");
                String estadoTurno = rs.getString("EstadoTurno");
                Timestamp entrada = rs.getTimestamp("HEntrada");
                Timestamp saida = rs.getTimestamp("HSaida");
                String tFunc = rs.getString("TipoFunc");
                String posto = rs.getString("Posto");

                EstadoTurno e = EstadoTurno.valueOf(estadoTurno);
                LocalDateTime l = entrada.toLocalDateTime();
                LocalDateTime l1 = saida.toLocalDateTime();
                TipoFuncionario tf = TipoFuncionario.valueOf(tFunc);
                TipoServico e1 = TipoServico.valueOf(posto);

                Map<Integer, Servico> servicos = new HashMap<>();
                String sql = "SELECT * FROM servicos WHERE FuncResponsavel='"+key+"';";

                try(ResultSet r = stm.executeQuery(sql)){
                    while(r.next()){
                        servicos.put(r.getInt("NumServico"), ServicoDAO.getInstance().get(r.getInt("FuncResponsavel")));
                    }
                }

                Map<Integer, CheckUp> checkups = new HashMap<>();
                sql = "SELECT * FROM checkups WHERE FuncResponsavel='"+key+"';";

                try(ResultSet r = stm.executeQuery(sql)){
                    while(r.next()){
                        checkups.put(r.getInt("NumCheckUp"), CheckUpDAO.getInstance().get(r.getInt("FuncResponsavel")));
                    }
                }
                
                
                f = new Funcionario(cartao, e, l, l1, tf, e1, servicos, checkups);
            }
        } catch (SQLException e) {
            // Database error!
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return f;
    }

    @Override
    public Funcionario put(Integer key, Funcionario value) {
        try (Connection conn = DriverManager.getConnection(ConfigDAO.URL, ConfigDAO.USERNAME, ConfigDAO.PASSWORD);
             Statement stm = conn.createStatement()) {
            try (PreparedStatement pstm = conn.prepareStatement("INSERT INTO funcionarios (Cartao,EstadoTurno,HEntrada,HSaida,TipoFunc,Posto) VALUES (?, ?, ?, ?, ?)")){
                pstm.setInt(1,value.getCartaoFuncionario());
                pstm.setString(2,value.getEstadoTurno().toString());
                pstm.setTimestamp(3, Timestamp.valueOf(value.getHoraEntrada()));
                pstm.setTimestamp(4, Timestamp.valueOf(value.getHoraSaida()));
                pstm.setString(5, value.getTipoFuncionario().toString());      
                pstm.setString(6, value.getPostosMecanico().toString());      

                Map<Integer, Servico> servAExecutar = value.getServDoDia();
                Map<Integer, CheckUp> checkUpsAExecutar = value.getCheckUpDoDia();
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
    public Funcionario remove(Object key) {
		Funcionario f = this.get(key);
        try (Connection conn = DriverManager.getConnection(ConfigDAO.URL, ConfigDAO.USERNAME, ConfigDAO.PASSWORD);
             Statement stm = conn.createStatement()){
            conn.setAutoCommit(false);

            stm.executeUpdate("DELETE FROM funcionarios WHERE Cartao='"+key+"';");
            stm.executeUpdate("DELETE FROM servicos WHERE FuncResponsavel='"+key+"';");
            stm.executeUpdate("DELETE FROM checkups WHERE FuncResponsavel='"+key+"';");

            conn.commit();
        } catch (Exception e) {
            // Database error!
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return f;
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends Funcionario> m) {
		for(Funcionario f : m.values()) {
            this.put(f.getCartaoFuncionario(), f.clone());
        }
    }

    @Override
    public void clear() {
        try (Connection conn = DriverManager.getConnection(ConfigDAO.URL, ConfigDAO.USERNAME, ConfigDAO.PASSWORD);
             Statement stm = conn.createStatement()) {
            stm.executeUpdate("TRUNCATE funcionarios");
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
             ResultSet rs = stm.executeQuery("SELECT Cartao FROM funcionarios")) {
            while (rs.next()) {
                Integer n = rs.getInt("Cartao");
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
    public Collection<Funcionario> values() {
		Collection<Funcionario> res = new HashSet<>();
        try (Connection conn = DriverManager.getConnection(ConfigDAO.URL, ConfigDAO.USERNAME, ConfigDAO.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT Cartao FROM funcionarios")) { 
            while (rs.next()) {
                int n = rs.getInt("Cartao");
                Funcionario f = this.get(n);
                res.add(f);                              
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return res;
    }

    @Override
    public Set<Entry<Integer, Funcionario>> entrySet() {
        Set<Entry<Integer, Funcionario>> entries = new HashSet<>();
    
        try (Connection conn = DriverManager.getConnection(ConfigDAO.URL, ConfigDAO.USERNAME, ConfigDAO.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT * FROM funcionarios")) {
    
            while (rs.next()) {
                int n = rs.getInt("Cartao");
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
