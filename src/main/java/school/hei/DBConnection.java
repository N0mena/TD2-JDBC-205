package school.hei;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    Connection connection ;

    public Connection getConnection(){

        try{
            String url = "jdbc:postgresql://localhost:5432/postgres";
            String user = "mini_dish_db_manager";
            String password = "123456";

            return DriverManager.getConnection(url,user,password);
        }catch(SQLException e){
            throw new RuntimeException(e);
        }
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
