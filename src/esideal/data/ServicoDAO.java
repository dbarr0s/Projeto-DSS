package esideal.data;

import java.sql.*;
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

    /**
     * Construtor da classe ServicoDAO.
     * Cria a tabela 'servicos' no banco de dados se ela não existir.
     * A tabela 'servicos' possui informações sobre os serviços prestados.
     * @throws NullPointerException Se ocorrer um erro ao criar a tabela ou estabelecer a conexão com o banco de dados.
     */

    public ServicoDAO(){
        try{ Connection connection = DriverManager.getConnection(ConfigDAO.URL, ConfigDAO.USERNAME, ConfigDAO.PASSWORD);
            Statement stm = connection.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS servicos(" + 
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
     * Obtém a instância única da classe ServicoDAO seguindo o padrão Singleton.
     * @return A instância única da classe ServicoDAO.
     */

    public static ServicoDAO getInstance(){
        if(ServicoDAO.singleton == null){
            ServicoDAO.singleton = new ServicoDAO();
        }
        return ServicoDAO.singleton;
    }

    /**
     * Retorna o número de registos na tabela 'servicos'.
     * @return O número de registos na tabela 'servicos'.
     * @throws NullPointerException Se ocorrer um erro ao executar a consulta SQL.
     */

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

    /**
     * Verifica se a tabela 'servicos' está vazia.
     * @return true se a tabela 'servicos' estiver vazia, false caso contrário.
     */

    @Override
    public boolean isEmpty() {
        return this.size() == 0;
    }

    /**
     * Verifica se a chave especificada está presente na tabela 'servicos'.
     * @param key A chave a ser verificada na tabela 'servicos'.
     * @return true se a chave estiver presente na tabela 'servicos', false caso contrário.
     * @throws NullPointerException Se ocorrer um erro ao executar a consulta SQL.
     */

    @Override
    public boolean containsKey(Object key) {
        boolean r;
        try (Connection conn = DriverManager.getConnection(ConfigDAO.URL, ConfigDAO.USERNAME, ConfigDAO.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs =
                     stm.executeQuery("SELECT NumServico FROM servicos WHERE NumServico='"+key+"'")) {
            r = rs.next();
        } catch (SQLException e) {
            // Database error!
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return r;
    }

    /**
     * Verifica se o valor especificado está presente na tabela 'servicos'.
     * @param value O valor a ser verificado na tabela 'servicos'.
     * @return true se o valor estiver presente na tabela 'servicos', false caso contrário.
     */

    @Override
    public boolean containsValue(Object value) {
        Servico s = (Servico) value;
        return this.containsKey(s.getNumServiço());
    }

    /**
     * Obtém o serviço associado à chave especificada da tabela 'servicos'.
     * @param key A chave cujo valor associado será obtido.
     * @return O serviço associado à chave especificada, ou null se a chave não estiver presente.
     * @throws NullPointerException Se ocorrer um erro ao executar a consulta SQL.
     */

    @Override
    public Servico get(Object key) {
        Servico s = null;
        try (Connection conn = DriverManager.getConnection(ConfigDAO.URL, ConfigDAO.USERNAME, ConfigDAO.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT * FROM servicos WHERE NumServico='"+key+"'")) {
            if(rs.next()){
                int numServico = rs.getInt("NumServico");
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

                s = new Servico(numServico, numFicha, funcResponsvel, matricula, custoServico, e, l, l1, sms, t);
            }
        } catch (SQLException e) {
            // Database error!
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return s;
    }    

    /**
     * Insere um novo serviço na tabela 'servicos'.
     * @param key A chave do serviço a ser inserido.
     * @param value O serviço a ser inserido na tabela 'servicos'.
     * @return O serviço inserido.
     */

    @Override
    public Servico put(Integer key, Servico value) {
        String sql = "INSERT INTO servicos " +
                     "(NumServico, NumFicha, FuncResponsavel, Matricula, CustoServico, Estado, HInicio, HFim, sms, TipoServico) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    
        try (Connection conn = DriverManager.getConnection(ConfigDAO.URL, ConfigDAO.USERNAME, ConfigDAO.PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
    
            pstmt.setInt(1, value.getNumServiço());
            pstmt.setInt(2, value.getNumFicha());
            pstmt.setInt(3, value.getFuncResponsavel());
            pstmt.setString(4, value.getMatricula());
            pstmt.setFloat(5, value.getCustServiço());
            pstmt.setString(6, value.getEstado().toString());
            pstmt.setTimestamp(7, Timestamp.valueOf(value.getHoraInicio()));
            pstmt.setTimestamp(8, Timestamp.valueOf(value.getHoraFim()));
            pstmt.setString(9, value.getSms());
            pstmt.setString(10, value.getTipoServico().toString());
    
            pstmt.executeUpdate();
    
        } catch (SQLException e) {
            e.printStackTrace();
           // throw new NullPointerException(e.getMessage());
        }
        return value;
    }

    /**
     * Atualiza o estado de um serviço na tabela 'servicos'.
     * @param servico O serviço que contém o estado a ser atualizado.
     */

    public void atualizarEstadoServico(Servico servico) {
        try (Connection conn = DriverManager.getConnection(ConfigDAO.URL, ConfigDAO.USERNAME, ConfigDAO.PASSWORD);
             PreparedStatement stmt = conn.prepareStatement("UPDATE servicos SET estado = ? WHERE numServico = ?")) {
            
            stmt.setString(1, servico.getEstado().toString());
            stmt.setInt(2, servico.getNumServiço());
    
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
     * Atualiza o SMS de um serviço na tabela 'servicos'.
     * @param servico O serviço que contém o SMS a ser atualizado.
     */
    
    public void atualizarSMSServico(Servico servico) {
        String sql = "UPDATE servicos SET sms = ? WHERE NumServico = ?";

        try (Connection conn = DriverManager.getConnection(ConfigDAO.URL, ConfigDAO.USERNAME, ConfigDAO.PASSWORD);
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, servico.getSms());
            stmt.setInt(2, servico.getNumServiço());

            int rowsUpdated = stmt.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("SMS do serviço atualizado com sucesso!");
            } else {
                System.out.println("Não foi possível atualizar o SMS do serviço.");
            }
        } catch (SQLException e) {
            System.out.println("Ocorreu um erro ao atualizar o SMS do serviço:");
            e.printStackTrace();
        }
    }

    /**
     * Remove um serviço da tabela 'servicos' com base na chave especificada.
     * @param key A chave do serviço a ser removido.
     * @return O serviço removido, ou null se a chave não estiver presente.
     * @throws NullPointerException Se ocorrer um erro ao executar a consulta SQL.
     */
    
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

    /**
     * Adiciona todos os serviços contidos no mapa especificado na tabela 'servicos'.
     * @param m O mapa que contém os serviços a serem adicionados na tabela 'servicos'.
     */

    @Override
    public void putAll(Map<? extends Integer, ? extends Servico> m) {
    //Este método recebe (Map) como argumento, onde as chaves são do tipo String e os valores são do tipo Veiculo
        for(Servico s : m.values()) {
            this.put(s.getNumServiço(), s.clone());
        }
    }

    /**
     * Remove todos os registos da tabela 'servicos'.
     * @throws NullPointerException Se ocorrer um erro ao executar a consulta SQL.
     */

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

    /**
     * Retorna um conjunto que contém as chaves (numServico) da tabela 'servicos'.
     * @return Um conjunto que contém as chaves da tabela 'servicos'.
     * @throws NullPointerException Se ocorrer um erro ao executar a consulta SQL.
     */

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

    /**
     * Retorna uma coleção que contém todos os serviços presentes na tabela 'servicos'.
     * @return Uma coleção que contém todos os serviços da tabela 'servicos'.
     * @throws RuntimeException Se ocorrer um erro ao executar a consulta SQL.
     */

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

    /**
     * Retorna um conjunto de pares chave-valor que representa a entrada de cada serviço na tabela 'servicos'.
     * @return Um conjunto de pares chave-valor que representa a entrada de cada serviço na tabela 'servicos'.
     * @throws RuntimeException Se ocorrer um erro ao executar a consulta SQL.
     */

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