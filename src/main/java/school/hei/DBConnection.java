package school.hei;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    Connection connection ;

    public Connection getConnection(){
        try{
            Connection connection = DriverManager.getConnection(System.getenv("url"),System.getenv("password"),System.getenv("port"));
        }catch(SQLException e){
            throw new RuntimeException(e);
        }
    }


}
