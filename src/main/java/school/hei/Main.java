package school.hei;

import java.sql.Connection;
import java.util.Collections;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        DataRetriever dataRetriever = new DataRetriever();
        List<Ingredient> dish = dataRetriever.findIngredientsByCriteria("cho",null,"gâteau",1,10);
        System.out.println(dish);

    }
}