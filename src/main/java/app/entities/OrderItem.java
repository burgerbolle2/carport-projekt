package app.entities;

public class OrderItem {
    private int OrderItemId;
    private Order order;
    private ProductVariant productVariant;
    private int Quantity;
    private String Description;

    public OrderItem(int orderItemId, Order order, ProductVariant productVariant, int quantity, String description) {
        OrderItemId = orderItemId;
        this.order = order;
        this.productVariant = productVariant;
        Quantity = quantity;
        Description = description;
    }

    public int getOrderItemId() {
        return OrderItemId;
    }

    public void setOrderItemId(int orderItemId) {
        OrderItemId = orderItemId;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public ProductVariant getProductVariant() {
        return productVariant;
    }

    public void setProductVariant(ProductVariant productVariant) {
        this.productVariant = productVariant;
    }

    public int getQuantity() {
        return Quantity;
    }

    public void setQuantity(int quantity) {
        Quantity = quantity;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }
}
