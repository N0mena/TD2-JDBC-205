package school.hei;

import java.util.List;
import java.util.Objects;

public class Dish {
    private Integer id;
    private String name;
    private DishTypeEnum dishType;
    private List<Ingredient> ingredients;
    private Double cost;

    public Dish(int id, String name, DishTypeEnum dishType, List<Ingredient> ingredients, Double cost) {
        this.id = id;
        this.name = name;
        this.dishType = dishType;
        this.ingredients = ingredients;
        this.cost = getDishCost();
    }

    public Dish(int id, String name) {
        this.id = id;
        this.name = name;
    }
    public Dish(int id, String name, DishTypeEnum dishType, List<Ingredient> ingredients) {
        this.id = id;
        this.name = name;
        this.dishType = dishType;
        this.ingredients = ingredients;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DishTypeEnum getDishType() {
        return dishType;
    }

    public void setDishType() {
        this.dishType = dishType;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Dish dish = (Dish) o;
        return Objects.equals(id, dish.id) && Objects.equals(name, dish.name) && dishType == dish.dishType && Objects.equals(ingredients, dish.ingredients);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, dishType, ingredients);
    }

    @Override
    public String toString() {
        return "Dish{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", dishType=" + dishType +
                ", ingredients=" + ingredients +
                '}';
    }


    public Double getDishCost() {
        double totalCost = 0.0;

        for (Ingredient ingredient : ingredients) {

            if (ingredient.getRequiredQuantity() == null) {
                throw new RuntimeException(
                        "Quantité nécessaire inconnue pour l'ingrédient : "
                                + ingredient.getName()
                );
            }

            totalCost += ingredient.getPrice()
                    * ingredient.getRequiredQuantity();
        }

        return totalCost;
    }

}
