import java.time.Instant;
import java.time.LocalDateTime;

public class Sale {

    private Integer id;
    private Order order;
    private Instant saleDate;

    public Sale(Integer id, Order order, Instant saleDate) {

        this.id = id;
        this.order = order;
        this.saleDate = saleDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public LocalDateTime getSaleDate() {
        return saleDate;
    }

    public void setSaleDate(LocalDateTime saleDate) {
        this.saleDate = saleDate;
    }

    @Override
    public String toString() {
        return "Sale{" +
                "id=" + id +
                ", order=" + order +
                ", saleDate=" + saleDate +
                '}';
    }
}
