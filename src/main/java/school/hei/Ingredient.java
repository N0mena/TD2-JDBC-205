package school.hei;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static java.time.Instant.now;

public class Ingredient {
    private Integer id;
    private String name;
    private Double price;
    private CategoryEnum category;
    private List<StockMovement> stockMovementList;


    public Ingredient() {}

    public Ingredient(int id, String name, double price, CategoryEnum category) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.category = category;

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


    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public CategoryEnum getCategory() {
        return category;
    }

    public void setCategory(CategoryEnum category) {
        this.category = category;
    }



    @Override
    public int hashCode() {
        return Objects.hash(id, name, price, category);
    }

    @Override
    public String toString() {
        return "Ingredient{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", category=" + category +
                ", actualStock=" + getStockValueAt(now()) +
                '}';
    }

    public StockValue getStockValueAt(Instant t) {
        if(stockMovementList == null) return null;


       List<StockMovement> stockMovements =  stockMovementList.stream()
               .filter(StockMovement stockMovement -> !stockMovement.getCreationDate().isAfter(t))
               .toList();





    }


}
