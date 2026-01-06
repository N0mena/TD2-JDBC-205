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
                    String dishTypeStr = resultSet.getString("dish_type");
                    DishTypeEnum dishType = DishTypeEnum.valueOf(dishTypeStr);

                    dishById = new Dish(dishId, dishName, dishType);


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
               Double ingredientPrice = resultSet.getDouble("price");
               String ingredientCategory = resultSet.getString("category");
               int dishId = resultSet.getInt(5);

                Ingredient dishIngredient = new Ingredient(ingredientId, ingredientName, ingredientPrice, ingredientCategory);
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

            ResultSet resultSet = st.executeQuery();

            while(resultSet.next()){

                int id = resultSet.getInt(1);
                String name = resultSet.getString("name");
                Double price = resultSet.getDouble("price");
                String category = resultSet.getString("category");
                int dishId = resultSet.getInt(5);

                Ingredient ing =  new Ingredient(id, name, price , category);

                ingredientsList.add(ing);

            }
        }catch (SQLException e){
            throw new RuntimeException(e);
        }

        return ingredientsList;
    }


    public List<Ingredient> createIngredients (List<Ingredient> newIngredients) throws SQLException {
        List<Ingredient> ingredientsCreated = new ArrayList<>();
        Connection databaseConnection = dbConnection.getConnection();

            String insertSQL = "Insert into Ingredient (name, price, category, id_dish) values (?, ?, ?,?)";
            String selectSQL = " Select * from ingredient i where i.id = ? ";

        try{
            PreparedStatement preparedStatementSelect = databaseConnection.prepareStatement(selectSQL);
            ResultSet resultSetSelect = preparedStatementSelect.executeQuery();

            for(Ingredient ingredient:newIngredients){
                preparedStatementSelect.setString(1, ingredient.getName());

                if(resultSetSelect.next()){
                    throw new RuntimeException("Ingrédient qui existe déjà: " + ingredient.getName());
                }

                PreparedStatement preparedStatementinsert = databaseConnection.prepareStatement(insertSQL);
                ResultSet resultSetInsert = preparedStatementinsert.executeQuery();

                if(resultSetInsert.next()){
                    ingredientsCreated.add(
                            new Ingredient(resultSetInsert.getInt(1), resultSetInsert.getString(2), resultSetInsert.getDouble(3), resultSetInsert.getString(4))
                    );
                }
            }

            databaseConnection.commit();
            return ingredientsCreated;


        }catch (SQLException e){
            databaseConnection.rollback();
            throw e;
        }


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
                int ingredientId = resultSet.getInt("id");
                String IngredientName = resultSet.getString("name");
                Double ingredientPrice = resultSet.getDouble(3);
                String ingredientCategory = resultSet.getString(4);
                int dishId = resultSet.getInt(5);

                ing = new Ingredient(ingredientId, ingredientName, ingredientPrice, IngredientName );

            }else {
                return null;
            }
        }catch (SQLException e){
            throw new RuntimeException(e);
        }

        try{
            String sql = "Select d.id,  ";
            PreparedStatement preparedStatement = databaseConnection.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();

            while(rs.next()){
                int dishId = rs.getInt("id_dish");
                String dishName = rs.getString("name");
                String dishTypeStr = rs.getString("dish_type");
                DishTypeEnum dishType = DishTypeEnum.valueOf(dishTypeStr);

                Dish dish = new Dish(dishId, dishName, dishType);

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


