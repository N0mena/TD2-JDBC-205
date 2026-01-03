package school.hei;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DataRetriever {
    private final DBConnection dbConnection = new DBConnection();

    public Dish findDishById(Integer id) {

        Dish dishById = null;
        List<Ingredient> ingredients = new ArrayList<>();
        Connection databaseConnection = dbConnection.getConnection();

        String sqlDish = "SELECT id, name, dish_type FROM dish WHERE id = ?";
        String sqlIngredient =  "SELECT id, name FROM ingredient WHERE id_dish = ?";

        try (
             PreparedStatement preparedStatement = databaseConnection.prepareStatement(sqlDish)) {

            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int dishId = resultSet.getInt("id");
                String dishName = resultSet.getString("name");
                String dishType = resultSet.getString("dish_type");


                dishById = new Dish(dishId, dishName, dishType);

            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        try{
            PreparedStatement preparedStatement = databaseConnection.prepareStatement(sqlIngredient);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {

;              int ingredientId = resultSet.getInt(1);
               String ingredientName = resultSet.getString("name");

                Ingredient dishIngredient = new Ingredient(ingredientId, ingredientName);
                ingredients.add(dishIngredient);
            }

        }catch (SQLException e){
            throw new RuntimeException(e);
        }

        if(dishById != null){
        dishById.setIngredients(ingredients);
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

    public List<Ingredient> createIngredients (List<Ingredient> newIngredients){
        List<Ingredient> ingredientsCreated = new ArrayList<>();
        Connection databaseConnection = dbConnection.getConnection();


        return ingredientsCreated;
    }

    public Dish saveDish(Dish dishToSave){
        throw new RuntimeException("not yet implemented");
    }

    public List<Dish> findDishsByIngredientName (String ingredientName){
        throw new RuntimeException("not yet implemented");
    }

    public List<Ingredient> findIngredientsByCriteria
            (String ingredientName, CategoryEnum category, String dishName, int page, int size){
        throw new RuntimeException("not yet implemented");
    }
}


