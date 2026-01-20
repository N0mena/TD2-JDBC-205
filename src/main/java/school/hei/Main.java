package school.hei;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static school.hei.CategoryEnum.VEGETABLE;

public class Main {
    public static void main(String[] args) {

        DataRetriever dataRetriever = new DataRetriever();
        Dish dish1 = dataRetriever.findDishById(1);
        System.out.println(dish1 + "cost :" + dish1.getDishCost());





    }

}