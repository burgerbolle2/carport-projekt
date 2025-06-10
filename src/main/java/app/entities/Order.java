package app.entities;

public class Order {
    private int OrderId;
    private int orderStatusId;
    private int carportWidth;
    private int carportLength;
    private int totalPrice;
    private User user;
    private String OrderStatusString;
    private Integer userId; // Add this field

    public Order(int orderId, int orderStatusId, int carportWidth, int carportLength, int totalPrice, User user) {
        this.OrderId = orderId;
        this.orderStatusId = orderStatusId;
        getOrderStatusString();
        this.carportWidth = carportWidth;
        this.carportLength = carportLength;
        this.totalPrice = totalPrice;
        this.user = user;
    }

    public int getOrderId() {
        return OrderId;
    }

    public String getOrderStatusString() {
        return switch (orderStatusId) {
            case 1 -> "Payment pending";
            case 2 -> "Payment completed";
            case 3 -> "Awaiting confirmation";
            default -> "Unknown status";
        };
    }

    public String OrderStatusString(){
        return OrderStatusString;
    }

    public void setOrderId(int orderId) {
        OrderId = orderId;
    }

    public int getOrderStatusId() {
        return orderStatusId;
    }

    public void setOrderStatusId(int orderStatusId) {
        this.orderStatusId = orderStatusId;
    }

    public int getCarportWidth() {
        return carportWidth;
    }

    public void setCarportWidth(int carportWidth) {
        this.carportWidth = carportWidth;
    }

    public int getCarportLength() {
        return carportLength;
    }

    public void setCarportLength(int carportLength) {
        this.carportLength = carportLength;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
