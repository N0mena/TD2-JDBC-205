package school.hei;

import java.security.Timestamp;
import java.util.List;

public class StockMovement {
    private Integer id;
    private Ingredient ingredient;
    private Double quantity;
    private UnitType unitType;
    private Timestamp creationDate;
    private MovementType movementType;

    public StockMovement(Integer id, Ingredient ingredient, Double quantity, UnitType unitType, Timestamp creationDate, MovementType movementType) {
        this.id = id;
        this.ingredient = ingredient;
        this.quantity = quantity;
        this.unitType = unitType;
        this.creationDate = creationDate;
        this.movementType = movementType;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Timestamp getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Timestamp creationDate) {
        this.creationDate = creationDate;
    }

    public MovementType getMovementType() {
        return movementType;
    }

    public void setMovementType(MovementType movementType) {
        this.movementType = movementType;
    }
}
