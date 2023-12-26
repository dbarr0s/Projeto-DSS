package esideal.data;

import java.util.AbstractMap;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.sql.*;
import java.time.LocalDateTime;

import esideal.station.funcionario.Funcionario;
import esideal.station.funcionario.TipoFuncionario;
import esideal.station.servico.TipoServico;

public class FuncionarioDAO implements Map<Integer, Funcionario>{
    private static FuncionarioDAO singleton = null;

    /**
     * Construtor da classe FuncionarioDAO. Cria a tabela 'funcionarios' se não existir no banco de dados.
     * @throws NullPointerException Se ocorrer um erro ao executar a criação da tabela ou ao se conectar ao banco de dados.
     */

    public FuncionarioDAO(){
        try{
            Connection connection = DriverManager.getConnection(ConfigDAO.URL, ConfigDAO.USERNAME, ConfigDAO.PASSWORD);
            Statement stm = connection.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS funcionarios(" + 
                        "Cartao INT NOT NULL AUTO_INCREMENT, " +
                        "HEntrada DATETIME NOT NULL, " +
                        "HSaida DATETIME NOT NULL, " +
                        "TipoFunc VARCHAR(100) NOT NULL, " +
                        "Posto VARCHAR(100) NOT NULL, " +
                        "PRIMARY KEY (Cartao));";
            stm.executeUpdate(sql);
        }
        catch(SQLException e){
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
    }

    /**
     * Obtém a instância única (singleton) da classe FuncionarioDAO.
     * @return A instância única da classe FuncionarioDAO.
     */

    public static FuncionarioDAO getInstance(){
        if(FuncionarioDAO.singleton == null){
            FuncionarioDAO.singleton = new FuncionarioDAO();
        }
        return FuncionarioDAO.singleton;
    }

    /**
     * Obtém o número de funcionários na tabela 'funcionarios'.
     * @return O número de funcionários na tabela 'funcionarios'.
     * @throws NullPointerException Se ocorrer um erro ao executar a consulta SQL.
     */

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

    /**
     * Verifica se a tabela 'funcionarios' está vazia.
     * @return true se a tabela 'funcionarios' estiver vazia, false caso contrário.
     */

    @Override
    public boolean isEmpty() {
        return this.size() == 0;
    }

    /**
     * Verifica se um determinado cartão está presente na tabela 'funcionarios'.
     * @param key O cartão a ser verificado.
     * @return true se o cartão estiver na tabela 'funcionarios', caso contrário, retorna false.
     * @throws NullPointerException Se ocorrer um erro ao executar a consulta SQL.
     */

    @Override
    public boolean containsKey(Object key) {
        boolean r;
        try (Connection conn = DriverManager.getConnection(ConfigDAO.URL, ConfigDAO.USERNAME, ConfigDAO.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs =
                     stm.executeQuery("SELECT Cartao FROM funcionarios WHERE Cartao='"+key+"'")) {
            r = rs.next();
        } catch (SQLException e) {
            // Database error!
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return r;
    }

    /**
     * Verifica se um determinado funcionário está presente na tabela 'funcionarios'.
     * @param value O funcionário a ser verificado.
     * @return true se o funcionário estiver na tabela 'funcionarios', caso contrário, retorna false.
     * @throws NullPointerException Se ocorrer um erro ao executar a consulta SQL.
     */

    @Override
    public boolean containsValue(Object value) {
        Funcionario f = (Funcionario) value;
        return this.containsKey(f.getCartaoFuncionario());
    }

    /**
     * Obtém um funcionário da tabela 'funcionarios' com base no cartão.
     * @param key O cartão do funcionário a ser obtido.
     * @return O funcionário correspondente ao cartão especificado, ou null se não encontrado.
     * @throws NullPointerException Se ocorrer um erro ao executar a consulta SQL.
     */

    @Override
    public Funcionario get(Object key) {
        Funcionario f = null;
        try (Connection conn = DriverManager.getConnection(ConfigDAO.URL, ConfigDAO.USERNAME, ConfigDAO.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT * FROM funcionarios WHERE Cartao='"+key+"'")) {
            if(rs.next()){
                int cartao = rs.getInt("Cartao");
                Timestamp entrada = rs.getTimestamp("HEntrada");
                Timestamp saida = rs.getTimestamp("HSaida");
                String tFunc = rs.getString("TipoFunc");
                String posto = rs.getString("Posto");

                LocalDateTime l = entrada.toLocalDateTime();
                LocalDateTime l1 = saida.toLocalDateTime();
                TipoFuncionario tf = TipoFuncionario.valueOf(tFunc);
                TipoServico e1 = TipoServico.valueOf(posto);
                
                f = new Funcionario(cartao, l, l1, tf, e1);
            }
        } catch (SQLException e) {
            // Database error!
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return f;
    }

    /**
     * Adiciona um novo funcionário à tabela 'funcionarios'.
     * @param key O cartão do funcionário a ser adicionado.
     * @param value O funcionário a ser adicionado.
     * @return O funcionário adicionado.
     * @throws NullPointerException Se ocorrer um erro ao executar a inserção SQL.
     */

    @Override
    public Funcionario put(Integer key, Funcionario value) {
        String funcionarioQuery = "INSERT INTO funcionarios (Cartao,HEntrada,HSaida,TipoFunc,Posto) " +
                "VALUES (?, ?, ?, ?, ?)";
    
        try (Connection conn = DriverManager.getConnection(ConfigDAO.URL, ConfigDAO.USERNAME, ConfigDAO.PASSWORD);
             PreparedStatement pstmtFuncionario = conn.prepareStatement(funcionarioQuery)) {
    
            pstmtFuncionario.setInt(1, value.getCartaoFuncionario());
            pstmtFuncionario.setTimestamp(2, Timestamp.valueOf(value.getHoraEntrada()));
            pstmtFuncionario.setTimestamp(4, Timestamp.valueOf(value.getHoraSaida()));
            pstmtFuncionario.setString(5, value.getTipoFuncionario().toString());
            pstmtFuncionario.setString(6, value.getPostosMecanico().toString());
            pstmtFuncionario.executeUpdate();
    
        } catch (SQLException e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return value;
    }

    /**
     * Remove um funcionário da tabela 'funcionarios' com base no cartão.
     * @param key O cartão do funcionário a ser removido.
     * @return O funcionário removido, ou null se não encontrado.
     * @throws NullPointerException Se ocorrer um erro ao executar a remoção SQL.
     */
    
    @Override
    public Funcionario remove(Object key) {
		Funcionario f = this.get(key);
        try (Connection conn = DriverManager.getConnection(ConfigDAO.URL, ConfigDAO.USERNAME, ConfigDAO.PASSWORD);
             Statement stm = conn.createStatement()){
            conn.setAutoCommit(false);

            stm.executeUpdate("DELETE FROM funcionarios WHERE Cartao='"+key+"';");

            conn.commit();
        } catch (Exception e) {
            // Database error!
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return f;
    }

    /**
     * Adiciona vários funcionários à tabela 'funcionarios'.
     * @param m O mapa que tem os funcionários a serem adicionados.
     * @throws NullPointerException Se ocorrer um erro ao executar a inserção SQL.
     */

    @Override
    public void putAll(Map<? extends Integer, ? extends Funcionario> m) {
		for(Funcionario f : m.values()) {
            this.put(f.getCartaoFuncionario(), f.clone());
        }
    }

    /**
     * Remove todos os registos da tabela 'funcionarios'.
     * @throws NullPointerException Se ocorrer um erro ao executar a remoção SQL.
     */

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

    /**
     * Retorna um conjunto contendo todos os cartões dos funcionários na tabela 'funcionarios'.
     * @return Um conjunto contendo todos os cartões dos funcionários na tabela 'funcionarios'.
     * @throws NullPointerException Se ocorrer um erro ao executar a consulta SQL.
     */

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

    /**
     * Retorna uma coleção que contém todos os funcionários na tabela 'funcionarios'.
     * @return Uma coleção que contém todos os funcionários na tabela 'funcionarios'.
     * @throws RuntimeException Se ocorrer um erro ao executar a consulta SQL.
     */

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

    /**
     * Retorna um conjunto de pares (chave, valor) representando os cartões e os funcionários correspondentes na tabela 'funcionarios'.
     * @return Um conjunto de pares (chave, valor) representando os cartões e os funcionários correspondentes na tabela 'funcionarios'.
     * @throws NullPointerException Se ocorrer um erro ao executar a consulta SQL.
     */

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