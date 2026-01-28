package school.hei;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DataRetriever {
    private final DBConnection dbConnection = new DBConnection();

    public Dish findDishById(Integer id) {
        Dish dishById = null;
        List<DishIngredient> dishIngredients = new ArrayList<>();
        List<Ingredient> ingredients = new ArrayList<>();

        Connection databaseConnection = dbConnection.getConnection();

            String sqlDish = "SELECT d.id, d.name, d.dish_type, d.selling_price, i.name FROM dish d inner join dish_ingredient  di on di.id_dish = d.id inner join ingredient i on di.id_ingredient = i.id  WHERE d.id = ? ";
        try (
             PreparedStatement preparedStatement = databaseConnection.prepareStatement(sqlDish)) {

            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                if(dishById == null) {
                dishById = new Dish();
                dishById.setId(resultSet.getInt("id"));
                dishById.setName(resultSet.getString("name"));
                dishById.setDishType(DishTypeEnum.valueOf(resultSet.getString("dishById_type")));
                dishById.setSellingPrice(resultSet.getDouble("selling_price"));
                }

                 Ingredient ingredient = new Ingredient();
                 ingredient.setId(resultSet.getInt("id"));
                 ingredient.setName(resultSet.getString("name"));
                 ingredient.setPrice(resultSet.getObject("price") == null ? null : resultSet.getDouble("price"));
                 ingredient.setCategory(CategoryEnum.valueOf(resultSet.getString("category")));

                DishIngredient dishIngredient = new DishIngredient();
                dishIngredient.setId(resultSet.getInt("id"));
                dishIngredient.setQuantity(resultSet.getObject("required_quatinty") == null ? null : resultSet.getDouble("required_quantity"));
                dishIngredient.setUnitType(UnitType.valueOf(resultSet.getString("unit_type")));
                dishIngredient.setIngredient(ingredient);
                dishIngredient.setDish(dishById);

                dishIngredients.add(dishIngredient);

            }
            if(dishById != null) {
                    dishById.setDishIngredients(dishIngredients);
            }
            return dishById;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            dbConnection.closeConnection();
        }
    }

    public Dish saveDish(Dish toSave) {
        Connection databaseConnection = dbConnection.getConnection();
        String upsertDishSql = """
                    INSERT INTO dish (id, price, name, dish_type,selling_price)
                    VALUES (?, ?, ?, ?::dish_type,?)
                    ON CONFLICT (id) DO UPDATE
                    SET name = EXCLUDED.name,
                        dish_type = EXCLUDED.dish_type,
                        selling_price = EXCLUDED.selling_price,
                    RETURNING id
                """;
        Integer dishId;

        try (PreparedStatement ps = databaseConnection.prepareStatement(upsertDishSql)) {
            if (toSave.getId() != null) {
                ps.setInt(1, toSave.getId());
            } else {
                ps.setInt(1, getNextSerialValue(databaseConnection, "dish", "id"));
            }
            if (toSave.getSellingPrice() != null) {
                ps.setDouble(2, toSave.getSellingPrice());
            } else {
                ps.setNull(2, Types.DOUBLE);
            }
            ps.setString(3, toSave.getName());
            ps.setString(4, toSave.getDishType().name());
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                dishId = rs.getInt(1);
            }
            List<DishIngredient> newIngredients = toSave.getDishIngredients();
            detachIngredients(databaseConnection, dishId, newIngredients);
            attachIngredients(databaseConnection, dishId, newIngredients);

            databaseConnection.commit();
            return findDishById(dishId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            dbConnection.closeConnection();
        }
    }

    private void detachIngredients(Connection conn, Integer dishId, List<DishIngredient> dishIngredients)
            throws SQLException {
        if (dishIngredients != null && !dishIngredients.isEmpty()) { //mivadika delete rehefa hitadetacher rehefa lasa misy DishIngredient
            try (PreparedStatement ps = conn.prepareStatement(
                    "Delete dish_ingredient WHERE id_dish = ?")) {
                ps.setInt(1, dishId);
                ps.executeUpdate();
            }
        }
    }

    private void attachIngredients(Connection conn, Integer dishId,List<DishIngredient> dishIngredients)
            throws SQLException {
        String attachSql = """
                    Insert into dish_ingredient(id_dish, id_ingredient, quantity_required, unit) values (?,?,?,?) 
                """;

        try (PreparedStatement ps = conn.prepareStatement(attachSql)) {
            for (DishIngredient dishIngredient : dishIngredients) {
                ps.setInt(1, dishId);
                ps.setInt(2, dishIngredient.getIngredient().getId());
                ps.setDouble(3, dishIngredient.getQuantity());
                ps.setString(4, dishIngredient.getUnitType().name());
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    private String getSerialSequenceName(Connection conn, String tableName, String columnName)
            throws SQLException {

        String sql = "SELECT pg_get_serial_sequence(?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, tableName);
            ps.setString(2, columnName);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString(1);
                }
            }
        }
        return null;
    }

        private int getNextSerialValue(Connection conn, String tableName, String columnName)
            throws SQLException {

            String sequenceName = getSerialSequenceName(conn, tableName, columnName);
            if (sequenceName == null) {
                throw new IllegalArgumentException(
                        "Any sequence found for " + tableName + "." + columnName
                );
            }
            updateSequenceNextValue(conn, tableName, columnName, sequenceName);

            String nextValSql = "SELECT nextval(?)";

            try (PreparedStatement ps = conn.prepareStatement(nextValSql)) {
                ps.setString(1, sequenceName);
                try (ResultSet rs = ps.executeQuery()) {
                    rs.next();
                    return rs.getInt(1);
                }
            }
        }

            private void updateSequenceNextValue(Connection conn, String tableName, String columnName, String sequenceName) throws SQLException {
                String setValSql = String.format(
                        "SELECT setval('%s', (SELECT COALESCE(MAX(%s), 0) FROM %s))",
                        sequenceName, columnName, tableName
                );

                try (PreparedStatement ps = conn.prepareStatement(setValSql)) {
                    ps.executeQuery();
                }
            }
        }

//    private List<Ingredient> getIngredientsById (int id)  {
//        List<Ingredient> ingredients = new ArrayList<>();
//        Connection databaseConnection = dbConnection.getConnection();
//        String sqlIngredient =  "SELECT id, name , price, category , id_dish FROM ingredient WHERE id_dish = ?";
//        try{
//            PreparedStatement preparedStatement = databaseConnection.prepareStatement(sqlIngredient);
//            preparedStatement.setInt(1, id);
//            ResultSet resultSet = preparedStatement.executeQuery();
//
//            while (resultSet.next()) {
//                Ingredient dishIngredient = new Ingredient(
//                        resultSet.getInt(1),
//                        resultSet.getString("name"),
//                        resultSet.getDouble("price"),
//                        CategoryEnum.valueOf(resultSet.getString("category")
//                        );
//
//                ingredients.add(dishIngredient);
//            }
//            return ingredients;
//
//        }catch (SQLException e){
//            throw new RuntimeException(e);
//        }
//    }
//
//    private List<Ingredient> findIngredientByDishId(Integer idDish) {
//        DBConnection dbConnection = new DBConnection();
//        Connection connection = dbConnection.getConnection();
//        List<Ingredient> ingredients = new ArrayList<>();
//        try {
//            PreparedStatement preparedStatement = connection.prepareStatement(
//                    """
//                            select ingredient.id, ingredient.name, ingredient.price, ingredient.category, ingredient.required_quantity
//                            from ingredient where id_dish = ?;
//                            """);
//            preparedStatement.setInt(1, idDish);
//            ResultSet resultSet = preparedStatement.executeQuery();
//            while (resultSet.next()) {
//                Ingredient ingredient = new Ingredient();
//                ingredient.setId(resultSet.getInt("id"));
//                ingredient.setName(resultSet.getString("name"));
//                ingredient.setPrice(resultSet.getDouble("price"));
//                ingredient.setCategory(CategoryEnum.valueOf(resultSet.getString("category")));
//                Object requiredQuantity = resultSet.getObject("required_quantity");
//                ingredient.setQuantity(requiredQuantity == null ? null : resultSet.getDouble("required_quantity"));
//                ingredients.add(ingredient);
//            }
//            dbConnection.closeConnection(connection);
//            return ingredients;
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//
//    private String getSerialSequenceName(Connection conn, String tableName, String columnName)
//            throws SQLException {
//
//        String sql = "SELECT pg_get_serial_sequence(?, ?)";
//
//        try (PreparedStatement ps = conn.prepareStatement(sql)) {
//            ps.setString(1, tableName);
//            ps.setString(2, columnName);
//
//            try (ResultSet rs = ps.executeQuery()) {
//                if (rs.next()) {
//                    return rs.getString(1);
//                }
//            }
//        }
//        return null;
//    }
//
//    private int getNextSerialValue(Connection conn, String tableName, String columnName)
//            throws SQLException {
//
//        String sequenceName = getSerialSequenceName(conn, tableName, columnName);
//        if (sequenceName == null) {
//            throw new IllegalArgumentException(
//                    "Any sequence found for " + tableName + "." + columnName
//            );
//        }
//        updateSequenceNextValue(conn, tableName, columnName, sequenceName);
//
//        String nextValSql = "SELECT nextval(?)";
//
//        try (PreparedStatement ps = conn.prepareStatement(nextValSql)) {
//            ps.setString(1, sequenceName);
//            try (ResultSet rs = ps.executeQuery()) {
//                rs.next();
//                return rs.getInt(1);
//            }
//        }
//    }
//
//
//
//    public List<Ingredient> findIngredients(int page, int size){
//        List<Ingredient> ingredientsList = new ArrayList<>();
//        Connection databaseConnection = dbConnection.getConnection();
//
//        try{
//
//            int offset = (page - 1) * size;
//            String sql_query = "SELECT id, name , price, category , id_dish from ingredient order by id limit ? offset ? ";
//            PreparedStatement st = databaseConnection.prepareStatement(sql_query);
//
//            st.setInt(1, offset);
//            st.setInt(2, size);
//
//            ResultSet resultSet = st.executeQuery();
//
//            while(resultSet.next()){
//                Dish dish = new Dish(resultSet.getInt("id"),
//                        resultSet.getString("name")
//                );
//
//                Ingredient dishIngredient = new Ingredient(
//                        resultSet.getInt(1),
//                        resultSet.getString("name"),
//                        resultSet.getDouble("price"),
//                        CategoryEnum.valueOf(resultSet.getString("category")),
//                        dish
//                );
//
//
//                ingredientsList.add(dishIngredient);
//
//            }
//            return ingredientsList;
//        }catch (SQLException e){
//            throw new RuntimeException(e);
//        }  finally {
//            dbConnection.closeConnection();
//        }
//
//    }
//
//
//    public List<Ingredient> createIngredients (List<Ingredient> newIngredients) throws SQLException {
//        List<Ingredient> ingredientsCreated = new ArrayList<>();
//        Connection databaseConnection = dbConnection.getConnection();
//
//            String insertSQL = "Insert into Ingredient (name, price, category, id_dish) values (?, ?, ?,?)";
//            String selectSQL = " Select * from ingredient i where i.id = ? ";
//
//        try{
//            PreparedStatement preparedStatementSelect = databaseConnection.prepareStatement(selectSQL);
//            ResultSet resultSetSelect = preparedStatementSelect.executeQuery();
//
//            for(Ingredient ingredient:newIngredients){
//                preparedStatementSelect.setString(1, ingredient.getName());
//
//                if(resultSetSelect.next()){
//                    throw new RuntimeException("Ingrédient qui existe déjà: " + ingredient.getName());
//                }
//
//                PreparedStatement preparedStatementinsert = databaseConnection.prepareStatement(insertSQL);
//                ResultSet resultSetInsert = preparedStatementinsert.executeQuery();
//
//                if(resultSetInsert.next()){
//                    ingredientsCreated.add(
//                            new Ingredient(resultSetInsert.getInt(1), resultSetInsert.getString("name"), resultSetInsert.getDouble("price"), CategoryEnum.valueOf(resultSetInsert.getString("category")))
//                    );
//                }
//            }
//
//            databaseConnection.commit();
//            return ingredientsCreated;
//
//        }catch (SQLException e){
//            databaseConnection.rollback();
//            throw e;
//        } finally {
//            dbConnection.closeConnection();
//        }
//
//    }
//
//
//
//    Dish saveDish(Dish toSave) {
//        String upsertDishSql = """
//                    INSERT INTO dish (id, price, name, dish_type)
//                    VALUES (?, ?, ?, ?::dish_type)
//                    ON CONFLICT (id) DO UPDATE
//                    SET name = EXCLUDED.name,
//                        dish_type = EXCLUDED.dish_type
//                    RETURNING id
//                """;
//
//        try (Connection conn = new DBConnection().getConnection()) {
//            conn.setAutoCommit(false);
//            Integer dishId;
//            try (PreparedStatement ps = conn.prepareStatement(upsertDishSql)) {
//                if (toSave.getId() != null) {
//                    ps.setInt(1, toSave.getId());
//                } else {
//                    ps.setInt(1, getNextSerialValue(conn, "dish", "id"));
//                }
//                if (toSave.getPrice() != null) {
//                    ps.setDouble(2, toSave.getPrice());
//                } else {
//                    ps.setNull(2, Types.DOUBLE);
//                }
//                ps.setString(3, toSave.getName());
//                ps.setString(4, toSave.getDishType().name());
//                try (ResultSet rs = ps.executeQuery()) {
//                    rs.next();
//                    dishId = rs.getInt(1);
//                }
//            }
//
//            List<Ingredient> newIngredients = toSave.getIngredients();
//            detachIngredients(conn, dishId, newIngredients);
//            attachIngredients(conn, dishId, newIngredients);
//
//            conn.commit();
//            return findDishById(dishId);
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    public List<Dish> findDishsByIngredientName (String ingredientName){
//        Map<Integer, Dish> dishMap = new HashMap<>();
//        List<Dish> dishList = new ArrayList<>(dishMap.values());
//        Connection databaseConnection = dbConnection.getConnection();
//
//            String sql = " SELECT d.id   AS dish_id d.name AS dish_name ,d.dish_type FROM ingredient i JOIN dish d ON d.id = i.id_dish WHERE i.name ILIKE ? ";
//        try{
//
//            PreparedStatement preparedStatement = databaseConnection.prepareStatement(sql);
//            preparedStatement.setString(1,  "%" + ingredientName + "%" );
//            ResultSet resultSet = preparedStatement.executeQuery();
//
//            while(resultSet.next()){
//                int dishId = resultSet.getInt("dish_id");
//                        new Dish(
//                                resultSet.getInt("id"),
//                                resultSet.getString("name")
//                        );
//
//            }
//            return dishList;
//        }catch (SQLException e){
//            throw new RuntimeException(e);
//        } finally {
//            dbConnection.closeConnection();
//        }
//
//    }
//
//
//
//    public List<Ingredient> findIngredientsByCriteria
//            (String ingredientName, CategoryEnum category, String dishName, int page, int size){
//
//        List<Ingredient> ingredients = new ArrayList<>();
//
//        Connection databaseConnection = dbConnection.getConnection();
//
//        StringBuilder sql = new StringBuilder(
//                "SELECT i.id, i.name, i.category, d.id AS dish_id, d.name AS dish_name " +
//                        "FROM ingredient i " +
//                        "JOIN dish d ON d.id = i.dish_id " +
//                        "WHERE 1=1 "
//        );
//
//        List<Object> params = new ArrayList<>();
//
//        if (ingredientName != null && !ingredientName.isBlank()) {
//            sql.append("AND i.name ILIKE ? ");
//            params.add("%" + ingredientName + "%");
//        }
//
//        if (category != null) {
//            sql.append("AND i.category = ? ");
//            params.add(category.name());
//        }
//
//        if (dishName != null && !dishName.isBlank()) {
//            sql.append("AND d.name ILIKE ? ");
//            params.add("%" + dishName + "%");
//        }
//
//        sql.append("ORDER BY i.id ");
//        sql.append("LIMIT ? OFFSET ? ");
//
//        int offset = page * size;
//        params.add(size);
//        params.add(offset);
//
//        try (
//             PreparedStatement preparedStatement = databaseConnection.prepareStatement(sql.toString())) {
//
//            for (int i = 0; i < params.size(); i++) {
//                preparedStatement.setObject(i + 1, params.get(i));
//            }
//
//            ResultSet resultSet = preparedStatement.executeQuery();
//
//            while (resultSet.next()) {
//                Ingredient ingredient = new Ingredient(resultSet.getInt(1), resultSet.getString("name"), resultSet.getDouble("price"), CategoryEnum.valueOf(resultSet.getString("category")));
//
//                Dish dish = new Dish(
//                        resultSet.getInt("id"),
//                        resultSet.getString("name")
//                );
//
//
//                ingredient.setDish(dish);
//
//                ingredients.add(ingredient);
//            }
//
//            return ingredients;
//        } catch (SQLException e) {
//            throw new RuntimeException("Error while fetching ingredients", e);
//        } finally {
//            dbConnection.closeConnection();
//        }
//
//    }
//
//    public Ingredient saveOrUpdateIngredient(Ingredient ingredient) {
//
//        Connection databaseConnection = dbConnection.getConnection();
//
//        if (ingredient.getId() == null) {
//            // ===== INSERT =====
//            String sql = """
//            INSERT INTO ingredient (name, price, required_quantity)
//            VALUES (?, ?, ?)
//        """;
//
//            try (PreparedStatement ps = databaseConnection.prepareStatement(sql)) {
//                ps.setString(1, ingredient.getName());
//                ps.setObject(2, ingredient.getPrice());
//
//                if (ingredient.getRequiredQuantity() != null) {
//                    ps.setDouble(3, ingredient.getRequiredQuantity());
//                } else {
//                    ps.setNull(3, Types.NUMERIC);
//                }
//
//                ResultSet rs = ps.executeQuery();
//                if (rs.next()) {
//                    ingredient.setId(rs.getInt("id"));
//                }
//
//            } catch (SQLException e) {
//                throw new RuntimeException(e);
//            }
//
//        } else {
//            // ===== UPDATE =====
//            String sql = """
//            UPDATE ingredient
//            SET name = ?, price = ?, required_quantity = ?
//            WHERE id = ?
//        """;
//
//            try (PreparedStatement ps = databaseConnection.prepareStatement(sql)) {
//                ps.setString(1, ingredient.getName());
//                ps.setDouble(2, ingredient.getPrice());
//
//                if (ingredient.getRequiredQuantity() != null) {
//                    ps.setDouble(3, ingredient.getRequiredQuantity());
//                } else {
//                    ps.setNull(3, Types.NUMERIC);
//                }
//
//                ps.setInt(4, ingredient.getId());
//
//                ps.executeUpdate();
//
//            } catch (SQLException e) {
//                throw new RuntimeException(e);
//            }
//        }
//
//        return ingredient;
//    }



}


