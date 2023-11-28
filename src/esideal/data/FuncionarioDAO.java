package esideal.data;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.sql.*;

import esideal.station.funcionario.Funcionario;

public class FuncionarioDAO implements Map<Integer, Funcionario>{
    private static FuncionarioDAO singleton = null;

    public FuncionarioDAO(){
        try{
            Connection connection = DriverManager.getConnection(ConfigDAO.URL, ConfigDAO.USERNAME, ConfigDAO.PASSWORD);
            Statement stm = connection.createStatement();
            String sql = "ESCREVER NOVA TABELA";
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
        return null;
    }
     /*       Funcionario funcionario = null;
        try (Connection conn = DriverManager.getConnection(ConfigDAO.URL, ConfigDAO.USERNAME, ConfigDAO.PASSWORD);
                Statement stm = conn.createStatement();
                ResultSet rs = stm.executeQuery("SELECT * FROM Funcionarios WHERE Cartao='" + key + "'")) {
    
            if (rs.next()) {
                // Recuperar os dados do funcionário a partir do ResultSet
                int cartaoFuncionario = rs.getInt("Cartao");
                LocalDateTime horaEntrada = rs.getObject("HoraEntrada", LocalDateTime.class);
                LocalDateTime horaSaida = rs.getObject("HoraSaida", LocalDateTime.class);
                String tipoFuncionario = rs.getString("TipoFuncionario");
                int postosMecanico = rs.getInt("PostosMecanico");
    
                // Recuperar a lista de serviços do dia do funcionário (implemente este método)
                //Map<Integer, Servico> servDoDia = obterServicosDoDia(cartaoFuncionario); // Implemente este método

                //funcionario = new Funcionario(cartaoFuncionario, horaEntrada, horaSaida, tipoFuncionario, postosMecanico, servDoDia);
            }
        } catch (SQLException e) {
            // Tratar erros de base de dados
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return funcionario;
    }
/*    public Map<Integer, Servico> obterServicosDoDia(int cartaoFuncionario) {
        Map<Integer, Servico> servicosDoDia = new HashMap<>(); // Map para armazenar os serviços do dia
    
        try (Connection conn = DriverManager.getConnection(ConfigDAO.URL, ConfigDAO.USERNAME, ConfigDAO.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT  FROM funcionarios WHERE CartaoFuncionario='" + cartaoFuncionario + "'")) {
    
            while (rs.next()) {
                int numServico = rs.getInt("Número");
                Servico servico = new Servico(numServico, ...); 
                servicosDoDia.put(numServico, servico);
            }
        } catch (SQLException e) {
            // Tratar erros de base de dados
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return servicosDoDia;
    }
    */

    @Override
    public Funcionario put(Integer key, Funcionario value) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'put'");
    }

    @Override
    public Funcionario remove(Object key) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'remove'");
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends Funcionario> m) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'putAll'");
    }

    @Override
    public void clear() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'clear'");
    }

    @Override
    public Set<Integer> keySet() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'keySet'");
    }

    @Override
    public Collection<Funcionario> values() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'values'");
    }

    @Override
    public Set<Entry<Integer, Funcionario>> entrySet() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'entrySet'");
    }

}
