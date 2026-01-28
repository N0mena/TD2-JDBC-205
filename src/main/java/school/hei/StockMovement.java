package school.hei;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Objects;

public class StockMovement {
    private Integer id;
    private MovementType movementType;
    private Instant creationDate;
    private StockValue stockValue;

    public StockMovement(Integer id, MovementType movementType, Instant creationDate, StockValue stockValue) {
        this.id = id;
        this.movementType = movementType;
        this.creationDate = creationDate;
        this.stockValue = stockValue;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public MovementType getMovementType() {
        return movementType;
    }

    public void setMovementType(MovementType movementType) {
        this.movementType = movementType;
    }

    public Instant getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Instant creationDate) {
        this.creationDate = creationDate;
    }

    public StockValue getStockValue() {
        return stockValue;
    }

    public void setStockValue(StockValue stockValue) {
        this.stockValue = stockValue;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        StockMovement that = (StockMovement) o;
        return Objects.equals(id, that.id) && movementType == that.movementType && Objects.equals(creationDate, that.creationDate) && Objects.equals(stockValue, that.stockValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, movementType, creationDate, stockValue);
    }

    @Override
    public String
    toString() {
        return "StockMovement{" +
                "id=" + id +
                ", movementType=" + movementType +
                ", creationDate=" + creationDate +
                ", stockValue=" + stockValue +
                '}';
    }
}
