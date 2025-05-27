package app.services;

import app.entities.Order;
import app.entities.OrderItem;
import app.entities.ProductVariant;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.ProductMapper;

import app.entities.Product;

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

    private void calcCarport(Order order) throws DatabaseException {
        calcPost(order);
        calcBeams(order);
        calcRafters(order);

    }

    //  Stolper
    private void calcPost(Order order) throws DatabaseException {
        //Antal Stolper
        int quantity = 2 * (2 + length / 310);

        // Længde på stolper - dvs variant
        List<ProductVariant> productVariants = ProductMapper.getVariantByProductIdAndMinLength(0, POSTS, connectionPool);
        ProductVariant productVariant = productVariants.get(0);
        OrderItem orderItem = new OrderItem(0,order,productVariant,quantity,"Stolper nedgraves 90 cm. i jord");
        orderItems.add(orderItem);
    }

    // Remme
    public void calcBeams(Order order) throws DatabaseException {

    }

    // Spær
    public void calcRafters(Order order) throws DatabaseException {

    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }
}
