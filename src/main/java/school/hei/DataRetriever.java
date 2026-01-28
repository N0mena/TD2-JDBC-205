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

        Ingredient saveIngredient(Ingredient toSave) {
            throw new RuntimeException(e);
        }
        private void insertIngredientStockMovements(Connection conn, Ingredient ingredient) {

}



}


