package school.hei;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    Connection dbConnection = dbConnection.getConnection();

    try{
        Connection connection = DriverManager.getConnection(System.getEnv("url"),System.getEnv("password"),System.getEnv("port"));
    }catch(SQLException e){
        throw new RuntimeException(e),
    }

}
