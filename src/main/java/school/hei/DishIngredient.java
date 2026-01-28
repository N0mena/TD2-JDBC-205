package school.hei;

import java.util.Objects;

public class DishIngredient {
    private Integer id;
    private Dish dish;
    private Ingredient ingredient;
    private Double quantity;
    private UnitType unitType;

    public DishIngredient() {}

    public DishIngredient(Integer id, Dish dish, Ingredient ingredient, Double quantity, UnitType unitType) {
        this.id = id;
        this.dish = dish;
        this.ingredient = ingredient;
        this.quantity = quantity;
        this.unitType = unitType;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Dish getDish() {
        return dish;
    }

    public void setDish(Dish dish) {
        this.dish = dish;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public void setIngredient(Ingredient ingredient) {
        this.ingredient = ingredient;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public UnitType getUnitType() {
        return unitType;
    }

    public void setUnitType(UnitType unitType) {
        this.unitType = unitType;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        DishIngredient that = (DishIngredient) o;
        return Objects.equals(id, that.id) && Objects.equals(dish, that.dish) && Objects.equals(ingredient, that.ingredient) && Objects.equals(quantity, that.quantity) && unitType == that.unitType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, dish, ingredient, quantity, unitType);
    }
}
