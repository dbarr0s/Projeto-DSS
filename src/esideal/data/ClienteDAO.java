package esideal.data;

import java.sql.*;
import java.util.AbstractMap;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import esideal.station.cliente.Cliente;
import esideal.station.veiculo.Veiculo;

public class ClienteDAO implements Map<String, Cliente>{
        private static ClienteDAO singleton = null;

    /**
     * Construtor da classe ClienteDAO. Cria as tabelas "clientes" e "veiculos" na base de dados se não existirem.
     * A tabela "clientes" armazena informações sobre os clientes, como nome, morada, NIF, telefone, email e se possuem voucher.
     * A tabela "veiculos" armazena informações sobre os veículos, incluindo matrícula, proprietário, nome do veículo, tipo de veículo e tipo de motor.
     * @throws NullPointerException se ocorrer um erro durante a criação das tabelas na base de dados.
     */

    public ClienteDAO(){
        try{
            Connection connection = DriverManager.getConnection(ConfigDAO.URL, ConfigDAO.USERNAME, ConfigDAO.PASSWORD);
            Statement stm = connection.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS clientes(" + 
                        "Nome VARCHAR(150) NOT NULL, " +
                        "Morada VARCHAR(100) NOT NULL, " +
                        "NIF INT NOT NULL, " +
                        "Telefone INT NOT NULL, " +
                        "Email VARCHAR(100) NOT NULL, " +
                        "Voucher BIT, " +  //0 = FALSE, 1 = TRUE//
                        "PRIMARY KEY (Nome));";
            stm.executeUpdate(sql);
            sql = "CREATE TABLE IF NOT EXISTS veiculos(" + 
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
     * Retorna uma instância única (singleton) da classe ClienteDAO, garantindo que apenas uma instância
     * da classe seja criada e retornada durante todo o ciclo de vida da aplicação.
     * Se a instância ainda não foi criada, cria uma nova instância e a retorna. Caso contrário,
     * retorna a instância existente.
     * @return uma instância única (singleton) da classe ClienteDAO.
     */

    public static ClienteDAO getInstance(){
        if(ClienteDAO.singleton == null){
            ClienteDAO.singleton = new ClienteDAO();
        }
        return ClienteDAO.singleton;
    }

    /**
     * Retorna o número de registos (clientes) armazenados na base de dados.
     * @return o número de registos (clientes) armazenados.
     */

    @Override
    public int size() {
        int i = 0;
        try (Connection conn = DriverManager.getConnection(ConfigDAO.URL, ConfigDAO.USERNAME, ConfigDAO.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT count(*) FROM clientes;")) {
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
     * Verifica se a base de dados de clientes está vazia.
     * @return true se a base de dados de clientes estiver vazia, false caso contrário.
     */

    @Override
    public boolean isEmpty() {
        return this.size() == 0;
    }

    /**
     * Verifica se a chave especificada está presente na base de dados de clientes.
     * @param key a chave a ser verificada.
     * @return true se a chave especificada estiver presente, false caso contrário.
     */

    @Override
    public boolean containsKey(Object key) {
        boolean r;
        try (Connection conn = DriverManager.getConnection(ConfigDAO.URL, ConfigDAO.USERNAME, ConfigDAO.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs =
                     stm.executeQuery("SELECT Nome FROM clientes WHERE Nome='"+key+"'")) {
            r = rs.next();
        } catch (SQLException e) {
            // Database error!
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return r;
    }

    /**
     * Verifica se um determinado valor está presente na base de dados de clientes.
     * @param value o valor a ser verificado.
     * @return true se o valor especificado estiver presente na base de dados, false caso contrário.
     */

    @Override
    public boolean containsValue(Object value) {
        Cliente c = (Cliente) value;
        return this.containsKey(c.getNome());
    }

    /**
     * Retorna o cliente associado à chave especificada, se existir na base de dados.
     * @param key a chave cujo valor associado deve ser retornado.
     * @return o cliente associado à chave especificada, ou null se a chave não estiver presente na base de dados.
     * @throws NullPointerException se ocorrer um erro durante a consulta à base de dados.
     */

    @Override
    public Cliente get(Object key) {
        Cliente c = null;
        try (Connection conn = DriverManager.getConnection(ConfigDAO.URL, ConfigDAO.USERNAME, ConfigDAO.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT * FROM clientes WHERE Nome='"+key+"'")) {
            if(rs.next()){
                String nome = rs.getString("Nome");
                String morada = rs.getString("Morada");
                int NIF = rs.getInt("NIF");
                int telefone = rs.getInt("Telefone");
                String email = rs.getString("Email");
                boolean voucher = rs.getBoolean("Voucher");

                Map<String, Veiculo> veiculos = new HashMap<>();
                String sql = "SELECT * FROM veiculos WHERE Dono='"+nome+"';";

                try(ResultSet r = stm.executeQuery(sql)){
                    while(r.next()){
                        veiculos.put(r.getString("Matricula"), VeiculoDAO.getInstance().get(r.getString("Matricula")));
                    }
                }
                c = new Cliente(nome, morada, NIF, telefone, email, voucher, veiculos);
            }
        } catch (SQLException e) {
            // Database error!
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return c;
    }

    /**
     * Insere um novo cliente na base de dados ou atualiza um existente, associando-o à chave especificada.
     * @param key a chave com a qual o cliente será associado na base de dados.
     * @param value o cliente a ser inserido ou atualizado na base de dados.
     * @return o cliente associado à chave especificada, ou null se a inserção não foi realizada com sucesso.
     * @throws IllegalArgumentException se o cliente passado como valor for inválido (nulo ou com campos essenciais nulos).
     * @throws NullPointerException se ocorrer um erro durante a execução da inserção/atualização na base de dados.
     */

    @Override
    public Cliente put(String key, Cliente value) {
        String clienteQuery = "INSERT INTO clientes (Nome, Morada, NIF, Telefone, Email, Voucher) "+
                            "VALUES (?, ?, ?, ?, ?, ?)";
    
        try (Connection conn = DriverManager.getConnection(ConfigDAO.URL, ConfigDAO.USERNAME, ConfigDAO.PASSWORD);
             PreparedStatement pstmtCliente = conn.prepareStatement(clienteQuery)) {
    
            pstmtCliente.setString(1, value.getNome());
            pstmtCliente.setString(2, value.getMorada());
            pstmtCliente.setInt(3, value.getNif());
            pstmtCliente.setInt(4, value.getTelefone());
            pstmtCliente.setString(5, value.getEmail());
            pstmtCliente.setBoolean(6, value.getVoucher());
    
            pstmtCliente.executeUpdate();
    
            Map<String, Veiculo> veiculos = value.getVeiculos();
            for(Veiculo v : veiculos.values()){
                String veiculoQuery = "INSERT INTO veiculos (Matricula, Dono, NomeVeic, TVeiculo, TMotor) "+
                                    "VALUES (?, ?, ?, ?, ?) ";
    
                try(PreparedStatement pstmVeiculo = conn.prepareStatement(veiculoQuery)){
    
                    pstmVeiculo.setString(1, v.getMatricula());
                    pstmVeiculo.setString(2, v.getDono());
                    pstmVeiculo.setString(3, v.getNome());
                    pstmVeiculo.setString(4, v.getTipoVeiculo().toString());
                    pstmVeiculo.setString(5, v.getTipoMotor().toString());
    
                    pstmVeiculo.executeUpdate();
                }
            }           
        } catch (SQLException e) {
            // Database error!
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return value;
    }
    
    /**
     * Remove o cliente associado à chave especificada da base de dados.
     * @param key a chave do cliente a ser removido.
     * @return o cliente removido da base de dados, ou null se não foi encontrado.
     * @throws NullPointerException se ocorrer um erro durante a remoção da base de dados.
     */

    @Override
    public Cliente remove(Object key) {
		Cliente c = this.get(key);
        try (Connection conn = DriverManager.getConnection(ConfigDAO.URL, ConfigDAO.USERNAME, ConfigDAO.PASSWORD);
             Statement stm = conn.createStatement()){
            conn.setAutoCommit(false);

            stm.executeUpdate("DELETE FROM clientes WHERE Nome='"+key+"';");
            stm.executeUpdate("DELETE FROM veiculos WHERE Dono='"+c.getNome()+"';");

            conn.commit();
        } catch (Exception e) {
            // Database error!
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return c;
    }

    /**
     * Insere todos os clientes de um mapa na base de dados.
     * @param m o mapa de clientes a ser inserido na base de dados.
     */

    @Override
    public void putAll(Map<? extends String, ? extends Cliente> m) {
		for(Cliente c : m.values()) {
            this.put(c.getNome(), c.clone());
        }
    }
    
        
    /**
     * Remove todos os registos do cliente da base de dados.
     * @throws NullPointerException se ocorrer um erro durante a limpeza da base de dados.
     */

    @Override
    public void clear() {
        try (Connection conn = DriverManager.getConnection(ConfigDAO.URL, ConfigDAO.USERNAME, ConfigDAO.PASSWORD);
             Statement stm = conn.createStatement()) {
            stm.executeUpdate("TRUNCATE clientes");
        } catch (SQLException e) {
            // Database error!
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
    }

    /**
     * Retorna um conjunto contendo todas as chaves (Nome) presentes na base de dados.
     * @return um conjunto de chaves (Nome) da base de dados.
     * @throws NullPointerException se ocorrer um erro durante a obtenção das chaves da base de dados.
     */

    @Override
    public Set<String> keySet() {
        Set<String> res = new HashSet<>();
        try (Connection conn = DriverManager.getConnection(ConfigDAO.URL, ConfigDAO.USERNAME, ConfigDAO.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT Nome FROM clientes")) {
            while (rs.next()) {
                String n = rs.getString("Nome");
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
     * Retorna uma coleção contendo todos os clientes presentes na base de dados.
     * @return uma coleção de clientes da base de dados.
     * @throws RuntimeException se ocorrer um erro durante a obtenção dos clientes da base de dados.
     */

    @Override
    public Collection<Cliente> values() {
		Collection<Cliente> res = new HashSet<>();
        try (Connection conn = DriverManager.getConnection(ConfigDAO.URL, ConfigDAO.USERNAME, ConfigDAO.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT Nome FROM clientes")) { 
            while (rs.next()) {
                String Nome = rs.getString("Nome");
                Cliente c = this.get(Nome);
                res.add(c);                              
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return res;
    }

    /**
     * Retorna um conjunto de pares que representa cada entrada na base de dados.
     * @return um conjunto de pares que representa cada entrada na base de dados.
     * @throws NullPointerException se ocorrer um erro durante a obtenção das entradas da base de dados.
     */

    @Override
    public Set<Entry<String, Cliente>> entrySet() {
        Set<Entry<String, Cliente>> entries = new HashSet<>();
    
        try (Connection conn = DriverManager.getConnection(ConfigDAO.URL, ConfigDAO.USERNAME, ConfigDAO.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT * FROM clientes")) {
    
            while (rs.next()) {
                String Nome = rs.getString("Nome");
                entries.add(new AbstractMap.SimpleEntry<>(Nome, this.get(Nome)));
            }
        } catch (SQLException e) {
            // Handle exception
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return entries;
    }
}