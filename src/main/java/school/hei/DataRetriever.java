package school.hei;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DataRetriever {
    private final DBConnection dbConnection = new DBConnection();

    public Dish findDishById(Integer id){
        Dish dishById = new Dish();
        Connection databaseConnection = dbConnection.getConnection();

        try{
            PreparedStatement preparedStatement = databaseConnection.prepareStatement("SELECT d.is, d.name, d.dish_type FROM dish d WHERE id = ?");
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                dishById.setId(resultSet.getInt("id"));
                dishById.setName(resultSet.getString("name"));
                dishById.setDishType(DishTypeEnum.valueOf(resultSet.getString("dish_type")));
            }

        } catch (SQLException e){
            throw new RuntimeException(e);
        }

        return dishById;
    }

    public List<Ingredient> findIngredients(int page, int size){
        List<Ingredient> ingredients = new ArrayList<>();
        Connection databaseConnection = dbConnection.getConnection();

        try{
            int offset = (page - 1) * size;
            String sql_query = "select i.id , i.name from ingredient i order by id limit ? offset ? ";
            PreparedStatement st = databaseConnection.prepareStatement(sql_query);
            ResultSet rs = st.executeQuery();
            while(rs.next()){
                rs.getInt("id");
                rs.getString("name");
            }
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
        return ingredients;
    }
}
