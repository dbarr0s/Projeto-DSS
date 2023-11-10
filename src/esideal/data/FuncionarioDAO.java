package esideal.data;

import java.util.Map;
import java.sql.*;

import esideal.station.funcionario.Funcionario;
import esideal.data.ConfigDAO;

public class FuncionarioDAO implements Map<Integer, Funcionario>{
    private static FuncionarioDAO singleton = null;

    public FuncionarioDAO(){
        try{
            Connection connection = DriverManager.getConnection(ConfigDAO.URL, ConfigDAO.USERNAME, ConfigDAO.PASSWORD);
            Statement stm = connection.createStatement();
            String sql;
        }
        catch(SQLException e){

        }
    }

    public static FuncionarioDAO getInstance(){
        if(FuncionarioDAO.singleton == null){
            FuncionarioDAO.singleton = new FuncionarioDAO();
        }
        return FuncionarioDAO.singleton;
    }
}
