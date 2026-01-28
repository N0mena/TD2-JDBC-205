package school.hei;

import java.util.List;
import java.util.Objects;

public class Dish {
    private Integer id;
    private String name;
    private DishTypeEnum dishType;
    private List<DishIngredient> dishIngredients;
    private Double sellingPrice;


    public Dish(){}

    public Dish(int id, String name, DishTypeEnum dishType, List<DishIngredient> dishIngredients, Double sellingPrice) {
        this.id = id;
        this.name = name;
        this.dishType = dishType;
        this.dishIngredients = dishIngredients;
        this.sellingPrice = sellingPrice;

    }

    public Dish(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Dish(int id, String name, DishTypeEnum dishType, Double sellingPrice) {
        this.id = id;
        this.name = name;
        this.dishType = dishType;
        this.sellingPrice = sellingPrice;
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

    public void setDishType(DishTypeEnum dishType) {
        this.dishType = this.dishType;
    }

    public List<DishIngredient> getDishIngredients() {
        return dishIngredients;
    }

    public void setDishIngredients(List<DishIngredient> ingredients) {
        this.dishIngredients = dishIngredients;
    }

    public  Double getSellingPrice() { return this.sellingPrice; }

    public void setSellingPrice(Double sellingPrice) {this.sellingPrice = this.sellingPrice;}




    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Dish dish = (Dish) o;
        return Objects.equals(id, dish.id) && Objects.equals(name, dish.name) && dishType == dish.dishType && Objects.equals(dishIngredients, dish.dishIngredients);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, dishType, dishIngredients, sellingPrice);
    }

    @Override
    public String toString() {
        return "Dish{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", dishType=" + dishType +
                ", dishIngredients=" + dishIngredients +
                ", sellingPrice=" + sellingPrice +
                '}';
    }


    public Double getDishCost() {
        double totalCost = 0.0;

        for (DishIngredient dishIngredient : dishIngredients) {

            if (dishIngredient.getQuantity() == null) {
                throw new RuntimeException(
                        "Quantité nécessaire inconnue pour l'ingrédient : "
                                + dishIngredient.getIngredient()
                );
            }

            totalCost += dishIngredient.getIngredient().getPrice()
                    * dishIngredient.getQuantity();
        }

        return totalCost;
    }

}
