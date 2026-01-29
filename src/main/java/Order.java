import java.time.Instant;
import java.util.List;
import java.util.Objects;

public class Order {
    private Integer id;
    private String reference;
    private Instant creationDatetime;
    private List<DishOrder> dishOrderList;
    private PaymentStatusEnum paymentStatus;


    public Order() {}

    public Order(Integer id, String reference, Instant creationDatetime, List<DishOrder> dishOrderList) {
        this.id = id;
        this.reference = reference;
        this.creationDatetime = creationDatetime;
        this.dishOrderList = dishOrderList;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public Instant getCreationDatetime() {
        return creationDatetime;
    }

    public void setCreationDatetime(Instant creationDatetime) {
        this.creationDatetime = creationDatetime;
    }

    public List<DishOrder> getDishOrderList() {
        return dishOrderList;
    }

    public void setDishOrderList(List<DishOrder> dishOrderList) {
        this.dishOrderList = dishOrderList;
    }

    public PaymentStatusEnum getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(PaymentStatusEnum paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", reference='" + reference + '\'' +
                ", creationDatetime=" + creationDatetime +
                ", dishOrderList=" + dishOrderList +
                ", getTotalAmountWithVat()=" + getTotalAmountWithVat() +
                ", paymentStatus=" + paymentStatus +
                '}';
    }

    Double getTotalAmountWithoutVat() {
        Double totalAmount = 0.0;
        List<DishOrder> dishOrderList = getDishOrderList();
        for(DishOrder dishOrder : dishOrderList){
          totalAmount += dishOrder.getDish().getPrice();

        }
        return totalAmount;
    }


    public boolean isPaid() {
        return paymentStatus == PaymentStatusEnum.PAID;
    }

    public void markAsPaid() {
        this.paymentStatus = PaymentStatusEnum.PAID;
    }

    Double getTotalAmountWithVat() {
        Double rate = 0.2;
        return getTotalAmountWithoutVat() * (1+rate);
    }


    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Order order)) return false;
        return Objects.equals(id, order.id) && Objects.equals(reference, order.reference) && Objects.equals(creationDatetime, order.creationDatetime) && Objects.equals(dishOrderList, order.dishOrderList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, reference, creationDatetime, dishOrderList);
    }
}
