package school.hei;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        DataRetriever dataRetriever = new DataRetriever();
        Dish dish = dataRetriever.findDishById(999);
        Dish dish1 = dataRetriever.findDishById(1);
        System.out.println(dish1);
        System.out.println(dish);

        List<Ingredient> ingredients = dataRetriever.findIngredients(2,2);
        List<Ingredient> ingredients1 = dataRetriever.findIngredients(3,5);
        System.out.println(ingredients);
        System.out.println(ingredients1);

        List<Dish> dishList = dataRetriever.findDishsByIngredientName("poulet");
        System.out.println(dishList);




    }

}