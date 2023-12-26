package esideal.data;

public class ConfigDAO {
    static final String USERNAME = "root";
    static final String PASSWORD = "DiogoBarros7";
    private static final String DATABASE = "esideal";
    private static final String DRIVER = "jdbc:mysql";
    static final String URL = DRIVER + "://localhost:3306/" + DATABASE + "?serverTimezone=UTC";
}
