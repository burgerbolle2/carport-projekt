package app.services;

import app.entities.Order;
import app.entities.OrderItem;
import app.entities.ProductVariant;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.ProductMapper;


import java.util.ArrayList;
import java.util.List;

public class Calculator {

    private static final int POSTS = 1;
    private static final int RAFTERS = 2;
    private static final int BEAMS = 2;


    private List<OrderItem> orderItems = new ArrayList<>();
    private int width;
    private int length;
    private ConnectionPool connectionPool;


    public Calculator(int width, int length, ConnectionPool connectionPool) {
        this.width = width;
        this.length = length;
        this.connectionPool = connectionPool;
    }

    public void calcCarport(Order order) throws DatabaseException {
        calcPost(order);
        calcBeams(order);
        calcRafters(order);

    }

    //  Stolper
    private void calcPost(Order order) throws DatabaseException {
        //Antal Stolper
        int quantity = calcPostQuantity();

        // Længde på stolper - dvs variant
        List<ProductVariant> productVariants = ProductMapper.getVariantByProductIdAndMinLength(0, POSTS, connectionPool);
        ProductVariant productVariant = productVariants.get(0);
        OrderItem orderItem = new OrderItem(0, order, productVariant, quantity, "Stolper nedgraves 90 cm. i jord");
        orderItems.add(orderItem);
    }

    public int calcPostQuantity() {
        return 2 * (2 + (length - 130) / 340);
    }

    // Remme
    private void calcBeams(Order order) throws DatabaseException {
        int sides = 2;
        if (length <= 600) {

            // Use the shortest board that fits the length
            List<ProductVariant> productVariants1 = ProductMapper.getVariantByProductIdAndMinLength(length, BEAMS, connectionPool);
            ProductVariant productVariant1 = productVariants1.get(0);
            OrderItem orderItem = new OrderItem(0, order, productVariant1, sides, "Remme i sider, sadles ned i stolper");
            orderItems.add(orderItem);
        } else {

            // 600cm rem til hver side
            List<ProductVariant> maxBoard = ProductMapper.getVariantByProductIdAndMinLength(600, BEAMS, connectionPool);
            ProductVariant maxBoardVariant = maxBoard.get(0);
            OrderItem orderItem600 = new OrderItem(0, order, maxBoardVariant, sides, "Remme i sider, sadles ned i stolper");
            orderItems.add(orderItem600);

            // Rest af længden
            int remainder = length - 600;
            List<ProductVariant> remainderBoard = ProductMapper.getVariantByProductIdAndMinLength(remainder, BEAMS, connectionPool);
            ProductVariant remainderBoardVariant = remainderBoard.get(0);
            OrderItem orderItemRest = new OrderItem(0, order, remainderBoardVariant, sides, "Remme i sider, sadles ned i stolper, deles.");
            orderItems.add(orderItemRest);
        }
    }

    public int getTotalPrice() {
        int total = 0;
        for (OrderItem item : orderItems) {
            int price = item.getProductVariant().getProduct().getPrice();
            int quantity = item.getQuantity();
            total += price * quantity;
        }
        return total;
    }


    // Spær
    public void calcRafters(Order order) throws DatabaseException {
        int quantity = calcRaftQuantity();

        List<ProductVariant> productVariants = ProductMapper.getVariantByProductIdAndMinLength(width, RAFTERS, connectionPool);
        ProductVariant productVariant = productVariants.get(0);
        OrderItem orderItem = new OrderItem(0, order, productVariant, quantity, "Spær monteres med 55 cm. afstand på rem");
        orderItems.add(orderItem);
    }

    public int calcRaftQuantity() {
        return (length / 55) + 1; // 55 cm afstand mellem spærene
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }
}
