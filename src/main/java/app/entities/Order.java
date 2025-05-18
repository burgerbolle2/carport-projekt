package app.entities;

import java.math.BigDecimal;
import java.sql.Date;

/**
 * POJO representing an order entity in the application.
 */
public class Order {
    private int id;
    private BigDecimal totalPrice;
    private String status;
    private Date date;
    private int userId;
    private int offerId;

    /**
     * Default constructor.
     */
    public Order() {
    }

    /**
     * Full-args constructor.
     *
     * @param id          the order ID
     * @param totalPrice  total price of the order
     * @param status      status of the order
     * @param date        date of the order
     * @param userId      ID of the user who placed the order
     * @param offerId     ID of the related offer
     */
    public Order(int id, BigDecimal totalPrice, String status, Date date, int userId, int offerId) {
        this.id = id;
        this.totalPrice = totalPrice;
        this.status = status;
        this.date = date;
        this.userId = userId;
        this.offerId = offerId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getOfferId() {
        return offerId;
    }

    public void setOfferId(int offerId) {
        this.offerId = offerId;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", totalPrice=" + totalPrice +
                ", status='" + status + '\'' +
                ", date=" + date +
                ", userId=" + userId +
                ", offerId=" + offerId +
                '}';
    }
}

