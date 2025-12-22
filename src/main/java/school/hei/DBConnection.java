package school.hei;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    Connection connection ;

    public Connection getConnection(){

        try{
            String url = System.getenv("jdbc:mysql://localhost:3306/school");
            String user = System.getenv("postgres");
            String password = System.getenv("12345");

            Connection connection = DriverManager.getConnection(url,user,password);
        }catch(SQLException e){
            throw new RuntimeException(e);
        }
        return null;
    }

    public void closeConnection (){
        try{
            if(connection !=null && !connection.isClosed() ){
                    connection.close();
            }
        }catch(SQLException e){
            throw new RuntimeException(e);
        }
    }
}
