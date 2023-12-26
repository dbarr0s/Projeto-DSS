package esideal.data;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import esideal.station.turnos.Turnos;

import java.sql.*;
import java.time.LocalDateTime;


public class TurnosDAO implements Map<Integer, Turnos>{
    private static TurnosDAO singleton = null;

    public TurnosDAO(){
        try{
            Connection connection = DriverManager.getConnection(ConfigDAO.URL, ConfigDAO.USERNAME, ConfigDAO.PASSWORD);
            Statement stm = connection.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS turnos(" + 
                        "NumTurno INT NOT NULL AUTO_INCREMENT, " +    
                        "Cartao INT NOT NULL, " +
                        "HEntrada DATETIME NOT NULL, " +
                        "HSaida DATETIME, " +
                        "FOREIGN KEY (Cartao) REFERENCES funcionarios(Cartao), " +
                        "PRIMARY KEY (NumTurno));";
            stm.executeUpdate(sql);
        }
        catch(SQLException e){
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
    }

    public static TurnosDAO getInstance(){
        if(TurnosDAO.singleton == null){
            TurnosDAO.singleton = new TurnosDAO();
        }
        return TurnosDAO.singleton;
    }

    @Override
    public int size() {
        int i = 0;
        try (Connection conn = DriverManager.getConnection(ConfigDAO.URL, ConfigDAO.USERNAME, ConfigDAO.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT count(*) FROM turnos;")) {
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
                     stm.executeQuery("SELECT NumTurno FROM turnos WHERE NumTurno='"+key+"'")) {
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
        Turnos t = (Turnos) value;
        return this.containsKey(t.getCartaoFuncionario());
    }

    @Override
    public Turnos get(Object key) {    
        Turnos t = null;
        String query = "SELECT * FROM turnos WHERE Cartao = ?";
        
        try (Connection conn = DriverManager.getConnection(ConfigDAO.URL, ConfigDAO.USERNAME, ConfigDAO.PASSWORD);
             PreparedStatement stm = conn.prepareStatement(query)) {
    
            stm.setInt(1, (Integer) key);
            try (ResultSet rs = stm.executeQuery()) {
                if (rs.next()) {
                    int numTurno = rs.getInt("NumTurno");
                    int cartao = rs.getInt("Cartao");
                    Timestamp entrada = rs.getTimestamp("HEntrada");
                    Timestamp saida = rs.getTimestamp("HSaida");
                    
                    LocalDateTime horaEntrada = entrada.toLocalDateTime();
                    LocalDateTime horaSaida = saida.toLocalDateTime();
                    
                    t = new Turnos(numTurno, cartao, horaEntrada, horaSaida);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao recuperar dados de turnos: " + e.getMessage());
        }
        return t;
    }

    public List<Turnos> getAllUniqueTurnos() {
        List<Turnos> turnosList = new ArrayList<>();
        String query = "SELECT DISTINCT NumTurno, Cartao, HEntrada, HSaida FROM turnos";
        
        try (Connection conn = DriverManager.getConnection(ConfigDAO.URL, ConfigDAO.USERNAME, ConfigDAO.PASSWORD);
             PreparedStatement stm = conn.prepareStatement(query);
             ResultSet rs = stm.executeQuery()) {
    
            while (rs.next()) {
                int numTurno = rs.getInt("NumTurno");
                int cartao = rs.getInt("Cartao");
                Timestamp entrada = rs.getTimestamp("HEntrada");
                Timestamp saida = rs.getTimestamp("HSaida");
                
                LocalDateTime horaEntrada = entrada.toLocalDateTime();
                LocalDateTime horaSaida = saida != null ? saida.toLocalDateTime() : null;
                
                Turnos turno = new Turnos(numTurno, cartao, horaEntrada, horaSaida);
                turnosList.add(turno);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao recuperar dados de turnos: " + e.getMessage());
        }
        
        return turnosList;
    }    

    @Override
    public Turnos put(Integer key, Turnos value) {
        String funcionarioQuery = "INSERT INTO turnos (NumTurno,Cartao,HEntrada,HSaida) VALUES (?, ?, ?, ?)";
    
        try (Connection conn = DriverManager.getConnection(ConfigDAO.URL, ConfigDAO.USERNAME, ConfigDAO.PASSWORD);
             PreparedStatement pstmtFuncionario = conn.prepareStatement(funcionarioQuery)) {
            pstmtFuncionario.setInt(1, value.getNumTurno());
            pstmtFuncionario.setInt(2, value.getCartaoFuncionario());
            // Verifica se horaEntrada não é nulo antes de converter para Timestamp
            if (value.getHoraEntrada() != null) {
                pstmtFuncionario.setTimestamp(3, Timestamp.valueOf(value.getHoraEntrada()));
            } else {
                pstmtFuncionario.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
            }

            if (value.getHoraSaida() != null) {
                pstmtFuncionario.setTimestamp(4, Timestamp.valueOf(value.getHoraSaida()));
            } else {
                pstmtFuncionario.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));
            }
            pstmtFuncionario.executeUpdate();
    
        } catch (SQLException e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return value;
    }

    public void updateHoraSaida(int cartaoFuncionario, LocalDateTime horaSaida) {
        try (Connection conn = DriverManager.getConnection(ConfigDAO.URL, ConfigDAO.USERNAME, ConfigDAO.PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement("UPDATE turnos SET HSaida = ? WHERE Cartao = ?")) {
            
            pstmt.setTimestamp(1, Timestamp.valueOf(horaSaida));
            pstmt.setInt(2, cartaoFuncionario);
            pstmt.executeUpdate();
    
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao atualizar a hora de saída no banco de dados: " + e.getMessage());
        }
    }
        

    @Override
    public Turnos remove(Object key) {
		Turnos t = this.get(key);
        try (Connection conn = DriverManager.getConnection(ConfigDAO.URL, ConfigDAO.USERNAME, ConfigDAO.PASSWORD);
             Statement stm = conn.createStatement()){
            conn.setAutoCommit(false);

            stm.executeUpdate("DELETE FROM turnos WHERE Cartao='"+key+"';");

            conn.commit();
        } catch (Exception e) {
            // Database error!
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return t;
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends Turnos> m) {
		for(Turnos t : m.values()) {
            this.put(t.getCartaoFuncionario(), t.clone());
        }
    }

    @Override
    public void clear() {
        try (Connection conn = DriverManager.getConnection(ConfigDAO.URL, ConfigDAO.USERNAME, ConfigDAO.PASSWORD);
             Statement stm = conn.createStatement()) {
            stm.executeUpdate("TRUNCATE turnos");
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
             ResultSet rs = stm.executeQuery("SELECT Cartao FROM turnos")) {
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
    public Collection<Turnos> values() {
		Collection<Turnos> res = new HashSet<>();
        try (Connection conn = DriverManager.getConnection(ConfigDAO.URL, ConfigDAO.USERNAME, ConfigDAO.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT Cartao FROM turnos")) { 
            while (rs.next()) {
                int n = rs.getInt("Cartao");
                Turnos t = this.get(n);
                res.add(t);                              
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return res;
    }

    @Override
    public Set<Entry<Integer, Turnos>> entrySet() {
        Set<Entry<Integer, Turnos>> entries = new HashSet<>();
    
        try (Connection conn = DriverManager.getConnection(ConfigDAO.URL, ConfigDAO.USERNAME, ConfigDAO.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT * FROM turnos")) {
    
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
