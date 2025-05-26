//package app.services;
//
//import app.persistence.ConnectionPool;
//import app.persistence.ProductMapper;
//
//import app.entities.Order;
//import app.entities.Product;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class Calculator {
//
//    private static final int POSTS = 1;
//    private static final int RAFTER = 2;
//    private static final int BEAMS = 2;
//
//
//    private List<Order> Order = new ArrayList<>();
//    private int width;
//    private int lenght;
//    private ConnectionPool connectionPool;
//
//    public Calculator(int width, int lenght, ConnectionPool connectionPool) {
//        this.width = width;
//        this.lenght = lenght;
//        this.connectionPool = connectionPool;
//
//    }
//
//    public void calcCarport() {
//        calcPost();
//        calcBeams();
//        calcRafters();
//
//    }
//
//    //  Stolper
//    private void calcPost() {
////Antal Stolper
//        int quantity = 6;
//
//        // Længde på stolper - dvs variant
//        List<Product> products = ProductMapper.getVariantsByProductIdAndMinLength(0, POSTS, connectionPool);
//        Order order = new Order()
//    }
//
//    // Remme
//    private void calcBeams() {
//
//    }
//
//    // Spær
//    private void calcRafters() {
//
//    }
//
//    public List<Order> getOrder() {
//        return Order;
//    }
//}
