package esideal.data;

import java.sql.*;
import java.util.AbstractMap;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import esideal.station.veiculo.TipoMotor;
import esideal.station.veiculo.TipoVeiculo;
import esideal.station.veiculo.Veiculo;

public class VeiculoDAO implements Map<String, Veiculo>{
    private static VeiculoDAO singleton = null;

    /**
     * Cria a tabela 'veiculos' no banco de dados, se não existir, para armazenar informações dos veículos.
     * A tabela possui colunas para Matricula, Dono, NomeVeic, TVeiculo e TMotor.
     * @throws NullPointerException Se ocorrer um erro durante a criação da tabela no banco de dados.
     */

    public VeiculoDAO(){
        try{
            Connection connection = DriverManager.getConnection(ConfigDAO.URL, ConfigDAO.USERNAME, ConfigDAO.PASSWORD);
            Statement stm = connection.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS veiculos(" + 
                        "Matricula VARCHAR(50) NOT NULL, " +
                        "Dono VARCHAR(50) NOT NULL, " +
                        "NomeVeic VARCHAR(50) NOT NULL, " +
                        "TVeiculo VARCHAR(50) NOT NULL, " +
                        "TMotor VARCHAR(50) NOT NULL, " +
                        "FOREIGN KEY (Dono) REFERENCES clientes(Nome), " +
                        "PRIMARY KEY (Matricula));";
            stm.executeUpdate(sql);
        }
        catch(SQLException e){
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
    }

    /**
     * Retorna a instância singleton de VeiculoDAO, se existir; caso contrário, cria uma nova instância.
     * @return A instância singleton de VeiculoDAO.
     */

    public static VeiculoDAO getInstance(){
        if(VeiculoDAO.singleton == null){
            VeiculoDAO.singleton = new VeiculoDAO();
        }
        return VeiculoDAO.singleton;
    }

    /**
     * Retorna o número de registos (veículos) presentes na tabela 'veiculos'.
     * @return O número de registos (veículos) presentes na tabela 'veiculos'.
     */

    @Override
    public int size() {
        int i = 0;
        try (Connection conn = DriverManager.getConnection(ConfigDAO.URL, ConfigDAO.USERNAME, ConfigDAO.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT count(*) FROM veiculos;")) {
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
     * Verifica se a tabela 'veiculos' está vazia.
     * @return true se a tabela 'veiculos' estiver vazia, false caso contrário.
     */

    @Override
    public boolean isEmpty() {
        return this.size() == 0;
    }

    /**
     * Verifica se a chave (Matricula) fornecida está presente na tabela 'veiculos'.
     * @param key A chave (Matricula) a ser verificada na tabela.
     * @return 'true' se a chave estiver presente na tabela; caso contrário, 'false'.
     */

    @Override
    public boolean containsKey(Object key) {
        boolean r;
        try (Connection conn = DriverManager.getConnection(ConfigDAO.URL, ConfigDAO.USERNAME, ConfigDAO.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs =
                     stm.executeQuery("SELECT Matricula FROM veiculos WHERE Matricula='"+key+"'")) {
            r = rs.next();
        } catch (SQLException e) {
            // Database error!
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return r;
    }

    /**
     * Verifica se o valor (Veiculo) fornecido está presente na tabela 'veiculos'.
     * @param value O valor (Veiculo) a ser verificado na tabela.
     * @return 'true' se o valor estiver presente na tabela; caso contrário, 'false'.
     */

    @Override
    public boolean containsValue(Object value) {
        Veiculo v = (Veiculo) value;
        return this.containsKey(v.getMatricula());
    }

    /**
     * Obtém o veículo associado à chave (Matricula) fornecida na tabela 'veiculos'.
     * @param key A chave (Matricula) do veículo a ser recuperado.
     * @return O veículo associado à chave fornecida, ou 'null' se não for encontrado.
     */

    @Override
    public Veiculo get(Object key) {
        Veiculo v = null;
        try (Connection conn = DriverManager.getConnection(ConfigDAO.URL, ConfigDAO.USERNAME, ConfigDAO.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT * FROM veiculos WHERE Matricula='"+key+"'")) {
            if(rs.next()){

                String matricula = rs.getString("Matricula");
                String dono = rs.getString("Dono");
                String nome = rs.getString("NomeVeic");
                String tveiculo = rs.getString("TVeiculo");
                String tmotor = rs.getString("TMotor");

                TipoVeiculo t = TipoVeiculo.valueOf(tveiculo);
                TipoMotor m = TipoMotor.valueOf(tmotor);

                v = new Veiculo(nome, dono, matricula, t, m);
            }
        } catch (SQLException e) {
            // Database error!
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return v;
    }

    /**
     * Adiciona um veículo à tabela 'veiculos' com a chave e valor fornecidos.
     * @param key A chave (Matricula) do veículo a ser adicionado.
     * @param value O veículo a ser adicionado à tabela.
     * @return O veículo adicionado à tabela.
     */

    @Override
    public Veiculo put(String key, Veiculo value) {
        String sql = "INSERT INTO veiculos (Matricula, Dono, Nome, TVeiculo, TMotor) " +
                     "VALUES (?, ?, ?, ?, ?)";
    
        try (Connection conn = DriverManager.getConnection(ConfigDAO.URL, ConfigDAO.USERNAME, ConfigDAO.PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
    
            pstmt.setString(1, value.getMatricula());
            pstmt.setString(2, value.getDono());
            pstmt.setString(3, value.getNome());
            pstmt.setString(4, value.getTipoVeiculo().toString());
            pstmt.setString(5, value.getTipoMotor().toString());
    
            pstmt.executeUpdate();
    
        } catch (SQLException e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return value;
    }

    /**
     * Remove o veículo associado à chave (Matricula) fornecida da tabela 'veiculos'.
     * @param key A chave (Matricula) do veículo a ser removido.
     * @return O veículo removido, ou 'null' se não for encontrado na tabela.
     */

    @Override
    public Veiculo remove(Object key) {
        Veiculo v = null;
        try (Connection conn = DriverManager.getConnection(ConfigDAO.URL, ConfigDAO.USERNAME, ConfigDAO.PASSWORD);
             Statement stm = conn.createStatement()) {
            try (PreparedStatement pstm = conn.prepareStatement("DELETE FROM veiculos WHERE Matricula = ?")){
                v = this.get(key);
                pstm.setString(1,(String)key);
                pstm.executeUpdate(); 
            }
        }catch (SQLException e) {
            // Database error!
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return v;
    }

    /**
     * Adiciona todos os veículos do mapa fornecido à tabela 'veiculos'.
     * @param m O mapa contendo veículos a serem adicionados à tabela.
     */

    @Override
    public void putAll(Map<? extends String, ? extends Veiculo> m) { 
    //Este método recebe (Map) como argumento, onde as chaves são do tipo String e os valores são do tipo Veiculo
        for(Veiculo v : m.values()) {
            this.put(v.getMatricula(), v.clone());
        }
    }

    /**
     * Limpa todos os registos da tabela 'veiculos'.
     * @throws NullPointerException Se ocorrer um erro ao limpar a tabela.
     */

    @Override
    public void clear() {
        try (Connection conn = DriverManager.getConnection(ConfigDAO.URL, ConfigDAO.USERNAME, ConfigDAO.PASSWORD);
             Statement stm = conn.createStatement()) {
            stm.executeUpdate("TRUNCATE veiculos");
        } catch (SQLException e) {
            // Database error!
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
    }

    /**
     * Retorna um conjunto contendo todas as chaves (Matricula) presentes na tabela 'veiculos'.
     * @return Um conjunto de chaves (Matricula) presentes na tabela 'veiculos'.
     * @throws NullPointerException Se ocorrer um erro ao recuperar as chaves.
     */

    @Override
    public Set<String> keySet() {
        Set<String> res = new HashSet<>();
        try (Connection conn = DriverManager.getConnection(ConfigDAO.URL, ConfigDAO.USERNAME, ConfigDAO.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT Matricula FROM veiculos")) {
            while (rs.next()) {
                String idc = rs.getString("Matricula");
                res.add(idc);
            }
        } catch (Exception e) {
            // Database error!
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return res;
    }

    /**
     * Retorna uma coleção contendo todos os valores (Veiculo) presentes na tabela 'veiculos'.
     * @return Uma coleção de valores (Veiculo) presentes na tabela 'veiculos'.
     * @throws RuntimeException Se ocorrer um erro ao recuperar os valores.
     */

    @Override
    public Collection<Veiculo> values() {
        Collection<Veiculo> res = new HashSet<>();
        try (Connection conn = DriverManager.getConnection(ConfigDAO.URL, ConfigDAO.USERNAME, ConfigDAO.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT * FROM veiculos")) { // Seleciona todas as colunas da tabela veiculos
            while (rs.next()) {
                String matricula = rs.getString("Matricula");
                Veiculo v = this.get(matricula);
                res.add(v); 
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return res;
    }
 
    /**
     * Retorna um conjunto contendo todas as entradas (Matricula, Veiculo) presentes na tabela 'veiculos'.
     * @return Um conjunto de entradas (Matricula, Veiculo) presentes na tabela 'veiculos'.
     * @throws NullPointerException Se ocorrer um erro ao recuperar as entradas.
     */

    @Override
    public Set<Entry<String, Veiculo>> entrySet() {
        Set<Entry<String, Veiculo>> entrySet = new HashSet<>();
        try (Connection conn = DriverManager.getConnection(ConfigDAO.URL, ConfigDAO.USERNAME, ConfigDAO.PASSWORD);
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("SELECT * FROM veiculos")) { // Seleciona todas as colunas da tabela veiculos
            while (rs.next()) {
                String matricula = rs.getString("Matricula");
                entrySet.add(new AbstractMap.SimpleEntry<>(matricula, this.get(matricula)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return entrySet;
    }
}
