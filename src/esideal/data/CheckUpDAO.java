package esideal.data;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.AbstractMap;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import esideal.station.checkup.CheckUp;
import esideal.station.servico.Estado;

public class CheckUpDAO implements Map<Integer, CheckUp>{
        private static CheckUpDAO singleton = null;

    /**
     * Construtor da classe CheckUpDAO que cria a tabela "checkups" no banco de dados se ela não existir ainda.
     * A tabela é criada com campos específicos e restrições de chave estrangeira.
     * 
     * @throws NullPointerException se ocorrer uma exceção durante a execução da criação da tabela no banco de dados.
     */

    public CheckUpDAO(){
        try{
            Connection connection = DriverManager.getConnection(ConfigDAO.URL, ConfigDAO.USERNAME, ConfigDAO.PASSWORD);
            Statement stm = connection.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS checkups(" + 
                "NumCheckUp INT NOT NULL AUTO_INCREMENT, " +
                "NumFicha INT NOT NULL," +
                "FuncResponsavel INT NOT NULL, " +
                "Matricula VARCHAR(50) NOT NULL, " +
                "DataCheckUp DATETIME NOT NULL, " +
                "DataFimCheckUp DATETIME NOT NULL, " +
                "Estado VARCHAR(50) NOT NULL, " +
                "FOREIGN KEY (NumFicha) REFERENCES fichas(NumFicha), " +
                "FOREIGN KEY (FuncResponsavel) REFERENCES funcionarios(Cartao), " +
                "FOREIGN KEY (Matricula) REFERENCES veiculos(Matricula), " +
                "PRIMARY KEY (NumCheckUp));";
           
            stm.executeUpdate(sql);
        }
        catch(SQLException e){
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
    }

    /**
     * Retorna uma instância única (singleton) da classe CheckUpDAO, garantindo que apenas uma instância
     * da classe seja criada e retornada durante todo o ciclo de vida da aplicação.
     * Se a instância ainda não foi criada, cria uma nova instância e a retorna. Caso contrário,
     * retorna a instância existente.
     * 
     * @return uma instância única (singleton) da classe CheckUpDAO.
     */

    public static CheckUpDAO getInstance(){
        if(CheckUpDAO.singleton == null){
            CheckUpDAO.singleton = new CheckUpDAO();
        }
        return CheckUpDAO.singleton;
    }

    /**
     * Retorna o número de registos (check-ups) armazenados na base de dados.
     * @return o número de registos (check-ups) armazenados.
     */

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

    /**
     * Verifica se a base de dados de check-ups está vazia.
     * @return true se a base de dados de check-ups estiver vazia, false caso contrário.
     */

    @Override
    public boolean isEmpty() {
        return this.size() == 0;
    }

    /**
     * Verifica se a chave especificada está presente na base de dados de check-ups.
     * @param key a chave a ser verificada.
     * @return true se a chave especificada estiver presente, false caso contrário.
     */

    @Override
    public boolean containsKey(Object key) {
        boolean r;
        try (Connection conn = DriverManager.getConnection(ConfigDAO.URL, ConfigDAO.USERNAME, ConfigDAO.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs =
                     stm.executeQuery("SELECT NumCheckUp FROM checkups WHERE NumCheckUp='"+key+"'")) {
            r = rs.next();
        } catch (SQLException e) {
            // Database error!
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return r;
    }

    /**
     * Verifica se um determinado valor (check-up) está presente na base de dados de check-ups.
     * @param value o valor a ser verificado.
     * @return true se o valor especificado estiver presente na base de dados, false caso contrário.
     */

    @Override
    public boolean containsValue(Object value) {
        CheckUp c = (CheckUp) value;
        return this.containsKey(c.getNumCheckUp());
    }

    /**
     * Retorna o check-up associado à chave especificada, se existir na base de dados.
     * @param key a chave cujo valor associado deve ser retornado.
     * @return o check-up associado à chave especificada, ou null se a chave não estiver presente na base de dados.
     * @throws NullPointerException se ocorrer um erro durante a consulta à base de dados.
     */

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
                Timestamp datafim = rs.getTimestamp("DataFimCheckUp");
                String estado = rs.getString("Estado");

                LocalDateTime l = data.toLocalDateTime();
                LocalDateTime l1 = datafim.toLocalDateTime();
                Estado e = Estado.valueOf(estado);

                c = new CheckUp(numCheckUp, numFicha, funcResponsvel, matricula, l, l1,e);
            }
        } catch (SQLException e) {
            // Database error!
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return c;
    }

    /**
     * Insere um novo check-up na base de dados ou atualiza um existente, associando-o à chave especificada.
     * @param key a chave com a qual o check-up será associado na base de dados.
     * @param value o check-up a ser inserido ou atualizado na base de dados.
     * @return o check-up associado à chave especificada, ou null se a inserção não foi realizada com sucesso.
     * @throws IllegalArgumentException se o check-up passado como valor for inválido (nulo ou com campos essenciais nulos).
     * @throws NullPointerException se ocorrer um erro durante a execução da inserção/atualização na base de dados.
     */

    @Override
    public CheckUp put(Integer key, CheckUp value) {
        if (value == null || value.getDataCheckUp() == null || value.getEstado() == null) {
            throw new IllegalArgumentException("Valor de checkup inválido.");
        }
    
        String checkupQuery = "INSERT INTO checkups (NumCheckUp, NumFicha, FuncResponsavel, Matricula, DataCheckUp, DataFimCheckUp, Estado) " + 
                            "VALUES (?, ?, ?, ?, ?, ?, ?)";
    
        try (Connection conn = DriverManager.getConnection(ConfigDAO.URL, ConfigDAO.USERNAME, ConfigDAO.PASSWORD);
             PreparedStatement pstmtCheckup = conn.prepareStatement(checkupQuery)) {
    
            pstmtCheckup.setInt(1, value.getNumCheckUp());
            pstmtCheckup.setInt(2, value.getNumFicha());
            pstmtCheckup.setInt(3, value.getFuncResponsavel());
            pstmtCheckup.setString(4, value.getMatricula());
            pstmtCheckup.setTimestamp(5, Timestamp.valueOf(value.getDataCheckUp()));
            pstmtCheckup.setTimestamp(6, Timestamp.valueOf(value.getDataFim()));
            pstmtCheckup.setString(7, value.getEstado().toString());
    
            pstmtCheckup.executeUpdate();
    
        } catch (SQLException e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return value;
    }
       
    /**
     * Remove o check-up associado à chave especificada da base de dados.
     * @param key a chave do check-up a ser removido.
     * @return o check-up removido da base de dados, ou null se não foi encontrado.
     * @throws NullPointerException se ocorrer um erro durante a remoção da base de dados.
     */

    @Override
    public CheckUp remove(Object key) {
		CheckUp c = this.get(key);
        try (Connection conn = DriverManager.getConnection(ConfigDAO.URL, ConfigDAO.USERNAME, ConfigDAO.PASSWORD);
             Statement stm = conn.createStatement()){
            conn.setAutoCommit(false);

            stm.executeUpdate("DELETE FROM checkups WHERE NumCheckUp='"+key+"';");

            conn.commit();
        } catch (Exception e) {
            // Database error!
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return c;
    }

    /**
     * Atualiza o estado de um check-up na base de dados com o estado do CheckUp fornecido.
     * @param c o check-up com o novo estado a ser atualizado na base de dados.
     */

    public void atualizarEstadoCheckUp(CheckUp c) {
        try (Connection conn = DriverManager.getConnection(ConfigDAO.URL, ConfigDAO.USERNAME, ConfigDAO.PASSWORD);
             PreparedStatement stmt = conn.prepareStatement("UPDATE checkups SET estado = ? WHERE NumCheckUp = ?")) {
            
            stmt.setString(1, c.getEstado().toString());
            stmt.setInt(2, c.getNumCheckUp());
    
            int rowsUpdated = stmt.executeUpdate();
    
            if (rowsUpdated > 0) {
                System.out.println("Estado do serviço atualizado com sucesso!");
            } else {
                System.out.println("Não foi possível atualizar o estado do serviço.");
            }
        } catch (SQLException e) {
            System.out.println("Ocorreu um erro ao atualizar o estado do serviço:");
            e.printStackTrace();
        }
    }

    /**
     * Insere todos os check-ups de um mapa na base de dados.
     * @param m o mapa de check-ups a ser inserido na base de dados.
     */

    @Override
    public void putAll(Map<? extends Integer, ? extends CheckUp> m) {
		for(CheckUp c : m.values()) {
            this.put(c.getNumCheckUp(), c.clone());
        }
    }
    
    /**
     * Remove todos os registos de check-ups da base de dados.
     * @throws NullPointerException se ocorrer um erro durante a limpeza da base de dados.
     */

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

    /**
     * Retorna um conjunto contendo todas as chaves (NumCheckUp) presentes na base de dados.
     * @return um conjunto de chaves (NumCheckUp) da base de dados.
     * @throws NullPointerException se ocorrer um erro durante a obtenção das chaves da base de dados.
     */

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

    /**
     * Retorna uma coleção contendo todos os check-ups presentes na base de dados.
     * @return uma coleção de check-ups da base de dados.
     * @throws RuntimeException se ocorrer um erro durante a obtenção dos check-ups da base de dados.
     */

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

    /**
     * Retorna um conjunto de pares (chave-valor) que representa cada entrada na base de dados.
     * @return um conjunto de pares (chave-valor) que representa cada entrada na base de dados.
     * @throws NullPointerException se ocorrer um erro durante a obtenção das entradas da base de dados.
     */

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
