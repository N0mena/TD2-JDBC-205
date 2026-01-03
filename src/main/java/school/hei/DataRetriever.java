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

        Dish dishById ;
        List<Ingredient> ingredients = new ArrayList<>();
        Connection databaseConnection = dbConnection.getConnection();

        String sqlDish = "SELECT d.id, d.name, d.dish_type FROM dish d WHERE d.id = ?";
        String sqlIngredient =  "SELECT id, name , price, category , id_dish FROM ingredient WHERE id_dish = ?";

        try (
             PreparedStatement preparedStatement = databaseConnection.prepareStatement(sqlDish)) {

            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {

                    int dishId = resultSet.getInt(1);
                    String dishName = resultSet.getString("name");
                    String dishType = resultSet.getString("dish_type");

                     dishById = new Dish(dishId, dishName);

            } else {
                return null;
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
               Double ingredientPrice = resultSet.getDouble(3);
               String ingredientCategory = resultSet.getString(4);
               int dishId = resultSet.getInt(5);

                Ingredient dishIngredient = new Ingredient(ingredientId, ingredientName);
                ingredients.add(dishIngredient);
            }

        }catch (SQLException e){
            throw new RuntimeException(e);
        }

        dishById.setIngredients(ingredients);

        return dishById;
    }


    public List<Ingredient> findIngredients(int page, int size){
        List<Ingredient> ingredientsList = new ArrayList<>();
        Connection databaseConnection = dbConnection.getConnection();

        try{

            int offset = (page - 1) * size;
            String sql_query = "SELECT id, name , price, category , id_dish from ingredient order by id limit ? offset ? ";
            PreparedStatement st = databaseConnection.prepareStatement(sql_query);

            st.setInt(1, offset);
            st.setInt(2, size);

            ResultSet rs = st.executeQuery();

            while(rs.next()){

                int ingredientId = rs.getInt(1);
                String ingredientName = rs.getString(2);
                Double ingredientPrice = rs.getDouble(3);
                String ingredientCategory = rs.getString(4);
                int dishId = rs.getInt(5);

                Ingredient ing =  new Ingredient(ingredientId, ingredientName);

                ingredientsList.add(ing);

            }
        }catch (SQLException e){
            throw new RuntimeException(e);
        }

        return ingredientsList;
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
        Ingredient ing ;
        List<Dish> dishList = new ArrayList<>();
        Connection databaseConnection = dbConnection.getConnection();

        try{

            String sql = "select i.id i.name, i.id_dish from ingredient i  where i.name ilike ? ";
            PreparedStatement preparedStatement = databaseConnection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next()){
                int ingId = resultSet.getInt("id");
                String ingName = resultSet.getString("name");

                ing = new Ingredient(ingId, ingName );

            }else {
                return null;
            }
        }catch (SQLException e){
            throw new RuntimeException(e);
        }

        try{
            String sql = "Select d.id, ";
            PreparedStatement preparedStatement = databaseConnection.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();

            while(rs.next()){
                int dishId = rs.getInt("id_dish");
                String dishName = rs.getString("name");

                Dish dish = new Dish(dishId, dishName);

                dishList.add(dish);
            }
        }catch (SQLException e){
            throw new RuntimeException(e);
        }


       return dishList;
    }

    public List<Ingredient> findIngredientsByCriteria
            (String ingredientName, CategoryEnum category, String dishName, int page, int size){
        throw new RuntimeException("not yet implemented");
    }
}


