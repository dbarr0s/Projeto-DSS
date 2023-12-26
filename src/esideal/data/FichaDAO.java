package esideal.data;

import java.sql.*;
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

    /**
     * Construtor da classe FichaDAO.
     * Inicializa a conexão com o banco de dados e cria as tabelas 'fichas', 'checkups' e 'servicos', se ainda não existirem.
     *
     * @throws NullPointerException Se ocorrer um erro durante a criação das tabelas na base de dados.
     */

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
                        "FOREIGN KEY (NomeDono) REFERENCES clientes(Nome), " +
                        "PRIMARY KEY (NumFicha));";
            stm.executeUpdate(sql);

            sql = "CREATE TABLE IF NOT EXISTS checkups(" + 
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
            
            sql = "CREATE TABLE IF NOT EXISTS servicos(" + 
                "NumServico INT NOT NULL AUTO_INCREMENT, " +
                "NumFicha INT NOT NULL," +
                "FuncResponsavel INT NOT NULL, " +
                "Matricula VARCHAR(50) NOT NULL, " +
                "CustoServico FLOAT NOT NULL, " +
                "Estado VARCHAR(50) NOT NULL, " +
                "HInicio DATETIME NOT NULL, " +
                "HFim DATETIME NOT NULL, " +
                "sms VARCHAR(100) NOT NULL, " +
                "TipoServico VARCHAR(50) NOT NULL, " +
                "FOREIGN KEY (NumFicha) REFERENCES fichas(NumFicha), " +
                "FOREIGN KEY (FuncResponsavel) REFERENCES funcionarios(Cartao), " +
                "FOREIGN KEY (Matricula) REFERENCES veiculos(Matricula), " +
                "PRIMARY KEY (NumServico));";
            stm.executeUpdate(sql);
        }
        catch(SQLException e){
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
    }

    /**
     * Obtém a instância única da classe FichaDAO (Singleton).
     * @return A instância única da classe FichaDAO.
     */

    public static FichaDAO getInstance(){
        if(FichaDAO.singleton == null){
            FichaDAO.singleton = new FichaDAO();
        }
        return FichaDAO.singleton;
    }

    /**
     * Retorna o número de registos na tabela 'fichas'.
     * @return O número de registos na tabela 'fichas'.
     * @throws NullPointerException Se ocorrer um erro ao executar a consulta SQL.
     */

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

    /**
     * Verifica se a tabela 'fichas' está vazia.
     * @return true se a tabela 'fichas' estiver vazia, false caso contrário.
     */

    @Override
    public boolean isEmpty() {
        return this.size() == 0;
    }

    /**
     * Verifica se a tabela 'fichas' contém uma chave específica.
     * @param key A chave a ser verificada na tabela 'fichas'.
     * @return true se a chave estiver presente na tabela 'fichas', false caso contrário.
     * @throws NullPointerException Se ocorrer um erro ao executar a consulta SQL.
     */

    @Override
    public boolean containsKey(Object key) {
        boolean r;
        try (Connection conn = DriverManager.getConnection(ConfigDAO.URL, ConfigDAO.USERNAME, ConfigDAO.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs =
                     stm.executeQuery("SELECT NumFicha FROM fichas WHERE NumFicha='"+key+"'")) {
            r = rs.next();
        } catch (SQLException e) {
            // Database error!
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return r;
    }

    /**
     * Verifica se a tabela 'fichas' contém um determinado valor.
     * @param value O valor a ser verificado na tabela 'fichas'.
     * @return true se o valor estiver presente na tabela 'fichas', false caso contrário.
     * @throws NullPointerException Se ocorrer um erro ao executar a consulta SQL.
     */

    @Override
    public boolean containsValue(Object value) {
        FichaVeiculo f = (FichaVeiculo) value;
        return this.containsKey(f.getNumFicha());
    }

    /**
     * Obtém um objeto FichaVeiculo da tabela 'fichas' com base na chave especificada.
     * @param key A chave do objeto a ser obtido na tabela 'fichas'.
     * @return O objeto FichaVeiculo correspondente à chave especificada ou null se não for encontrado.
     * @throws NullPointerException Se ocorrer um erro ao executar a consulta SQL.
     */

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

    /**
     * Insere um objeto FichaVeiculo na tabela 'fichas'.
     * @param key A chave do objeto FichaVeiculo a ser inserido na tabela 'fichas'.
     * @param value O objeto FichaVeiculo a ser inserido na tabela 'fichas'.
     * @return O objeto FichaVeiculo inserido na tabela 'fichas'.
     * @throws NullPointerException Se ocorrer um erro ao executar a consulta SQL.
     */

    @Override
    public FichaVeiculo put(Integer key, FichaVeiculo value) {
        String fichaQuery = "INSERT INTO fichas (NumFicha,Matricula,NomeDono,NomeVeiculo) "+
                            "VALUES (?, ?, ?, ?)";
    
        try (Connection conn = DriverManager.getConnection(ConfigDAO.URL, ConfigDAO.USERNAME, ConfigDAO.PASSWORD);
             PreparedStatement pstmtFicha = conn.prepareStatement(fichaQuery)) {
    
            pstmtFicha.setInt(1, value.getNumFicha());
            pstmtFicha.setString(2, value.getMatricula());
            pstmtFicha.setString(3, value.getNomeCliente());
            pstmtFicha.setString(4, value.getNomeVeiculo());
            pstmtFicha.executeUpdate();
    
            // Atualizar os serviços
            Map<Integer, Servico> servAExecutar = value.getServicos();
            for (Servico s : servAExecutar.values()) {
                String servicoQuery = "INSERT INTO servicos (NumServico, NumFicha, FuncResponsavel, Matricula, CustoServico, Estado, HInicio, HFim, sms, TipoServico)" +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    
                try (PreparedStatement pstmServicos = conn.prepareStatement(servicoQuery)) {
                    pstmServicos.setInt(1, s.getNumServiço());
                    pstmServicos.setInt(2, s.getNumFicha());
                    pstmServicos.setInt(3, s.getFuncResponsavel());
                    pstmServicos.setString(4, s.getMatricula());
                    pstmServicos.setFloat(5, s.getCustServiço());
                    pstmServicos.setString(6, s.getEstado().toString());
                    pstmServicos.setTimestamp(7, Timestamp.valueOf(s.getHoraInicio()));
                    pstmServicos.setTimestamp(8, Timestamp.valueOf(s.getHoraFim()));
                    pstmServicos.setString(9, s.getSms());
                    pstmServicos.setString(10, s.getTipoServico().toString());
    
                    pstmServicos.executeUpdate();
                }
            }
    
            // Atualizar os check-ups
            Map<Integer, CheckUp> checkUpsAExecutar = value.getCheckups();
            for (CheckUp c : checkUpsAExecutar.values()) {
                String checkupQuery = "INSERT INTO checkups (NumCheckUp,NumFicha,FuncResponsavel,Matricula,DataCheckUp,DataFimCheckUp,Estado) "+
                                      "VALUES (?, ?, ?, ?, ?, ?, ?)";
    
                try (PreparedStatement pstmCheckups = conn.prepareStatement(checkupQuery)) {
                    pstmCheckups.setInt(1, c.getNumCheckUp());
                    pstmCheckups.setInt(2, c.getNumFicha());
                    pstmCheckups.setInt(3, c.getFuncResponsavel());
                    pstmCheckups.setString(4, c.getMatricula());
                    pstmCheckups.setTimestamp(5, Timestamp.valueOf(c.getDataCheckUp()));
                    pstmCheckups.setTimestamp(6, Timestamp.valueOf(c.getDataFim()));
                    pstmCheckups.setString(7, c.getEstado().toString());
                    pstmCheckups.executeUpdate();
                }
            }
        } catch (SQLException e) {
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

    /**
     * Insere todos os elementos da coleção fornecida na tabela 'fichas'.
     * @param m Map contendo as chaves e valores a serem inseridos na tabela 'fichas'.
     * @throws NullPointerException Se ocorrer um erro ao executar a consulta SQL.
     */

    @Override
    public void putAll(Map<? extends Integer, ? extends FichaVeiculo> m) {
		for(FichaVeiculo f : m.values()) {
            this.put(f.getNumFicha(), f.clone());
        }
    }

    /**
     * Remove todos os registos da tabela 'fichas'.
     * @throws NullPointerException Se ocorrer um erro ao executar a consulta SQL.
     */

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

    /**
     * Obtém um conjunto contendo todas as chaves (NumFicha) existentes na tabela 'fichas'.
     * @return Um conjunto de chaves (NumFicha) da tabela 'fichas'.
     * @throws NullPointerException Se ocorrer um erro ao executar a consulta SQL.
     */

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

    /**
     * Obtém uma coleção contendo todos os valores (FichaVeiculo) existentes na tabela 'fichas'.
     * @return Uma coleção de valores (FichaVeiculo) da tabela 'fichas'.
     * @throws RuntimeException Se ocorrer um erro ao executar a consulta SQL.
     */

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

    /**
     * Obtém um conjunto contendo todas as entradas existentes na tabela 'fichas'.
     * @return Um conjunto de entradas da tabela 'fichas'.
     * @throws NullPointerException Se ocorrer um erro ao executar a consulta SQL.
     */

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
