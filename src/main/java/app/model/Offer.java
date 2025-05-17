package app.model;

import java.math.BigDecimal;
import java.sql.Date;

/**
 * POJO representing an offer entity in the application.
 */
public class Offer {
    private int id;
    private Date created;
    private BigDecimal totalPrice;
    private String status;
    private int userId;
    private int carportId;

    /**
     * Default constructor.
     */
    public Offer() {
    }

    /**
     * Full-args constructor.
     *
     * @param id         the offer ID
     * @param created    creation date of the offer
     * @param totalPrice total price of the offer
     * @param status     status of the offer
     * @param userId     ID of the user who created the offer
     * @param carportId  ID of the associated carport
     */
    public Offer(int id, Date created, BigDecimal totalPrice, String status, int userId, int carportId) {
        this.id = id;
        this.created = created;
        this.totalPrice = totalPrice;
        this.status = status;
        this.userId = userId;
        this.carportId = carportId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
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

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getCarportId() {
        return carportId;
    }

    public void setCarportId(int carportId) {
        this.carportId = carportId;
    }

    @Override
    public String toString() {
        return "Offer{" +
                "id=" + id +
                ", created=" + created +
                ", totalPrice=" + totalPrice +
                ", status='" + status + '\'' +
                ", userId=" + userId +
                ", carportId=" + carportId +
                '}';
    }
}

