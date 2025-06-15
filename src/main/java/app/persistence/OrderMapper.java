package app.persistence;

import app.entities.*;
import app.exceptions.DatabaseException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderMapper {

    public static List<Order> getAllOrders(ConnectionPool connectionPool) throws DatabaseException {
        List<Order> orderList = new ArrayList<>();
        String sql = "SELECT * FROM orders inner join users using(users_id)";
        try (
                Connection connection = connectionPool.getConnection();
                var prepareStatement = connection.prepareStatement(sql);
                var rs = prepareStatement.executeQuery()) {
            while (rs.next()) {
                int userId = rs.getInt("users_id");
                String email = rs.getString("email");
                String password = rs.getString("password");
                String role = rs.getString("role");
                String phone = rs.getString("phone");
                int orderId = rs.getInt("order_id");
                int carportWidth = rs.getInt("carport_width");
                int carportLength = rs.getInt("carport_length");
                int status = rs.getInt("status");
                int totalPrice = rs.getInt("total_price");
                User user = new User(userId, email, password, role,phone);
                Order order = new Order(orderId, carportWidth, carportLength, status, totalPrice, user);
                orderList.add(order);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Could not get users from DB", e.getMessage());
        }
        return orderList;
    }

    public static void updateOrderPrice(int orderId, int totalPrice, ConnectionPool pool) throws DatabaseException {
        String sql = "UPDATE orders SET total_price = ? WHERE order_id = ?";
        try (Connection conn = pool.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, totalPrice);
            ps.setInt(2, orderId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Could not update order price", e.getMessage());
        }
    }


    public static Order insertOrder(Order order, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "INSERT INTO orders (carport_width, carport_length, status, users_id, total_price) " + "VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = connectionPool.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, order.getCarportWidth());
                ps.setInt(2, order.getCarportLength());
                ps.setInt(3, 3); // 3 = awaiting confirmation
                ps.setInt(4, order.getUser().getUserId());
                ps.setInt(5, order.getTotalPrice());
                ps.executeUpdate();
                ResultSet keySet = ps.getGeneratedKeys();
                if (keySet.next()) {
                    Order newOrder = new Order(keySet.getInt(1), 1, order.getCarportWidth(), order.getCarportLength()
                            , order.getTotalPrice(), order.getUser());
                    return newOrder;
                } else
                    return null;
            }
        } catch (SQLException e) {
            throw new DatabaseException("Could not insert order", e.getMessage());
        }
    }


    public static List<Order> getPendingPaymentOrders(int userId, ConnectionPool connectionPool) throws DatabaseException {
        List<Order> orderList = new ArrayList<>();
        String sql = "SELECT * FROM orders WHERE status = 1 AND users_id = ?";
        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)
        ) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            User user = UserMapper.getUserById(userId, connectionPool);

            while (rs.next()) {
                int orderId = rs.getInt("order_id");
                int width = rs.getInt("carport_width");
                int length = rs.getInt("carport_length");
                int status = rs.getInt("status");
                int totalPrice = rs.getInt("total_price");


                Order order = new Order(orderId, status, width, length, totalPrice, user);
                orderList.add(order);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Could not get pending orders for user", e.getMessage());
        }

        return orderList;
    }

    public static Order getCompletedOrders(int userId, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "SELECT * FROM orders WHERE status = 2 AND users_id = ? ORDER BY order_id DESC LIMIT 1";
        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)
        ) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            User user = UserMapper.getUserById(userId, connectionPool);
            if (rs.next()) {
                int orderId = rs.getInt("order_id");
                int width = rs.getInt("carport_width");
                int length = rs.getInt("carport_length");
                int status = rs.getInt("status");
                int totalPrice = rs.getInt("total_price");

                return new Order(orderId, status, width, length, totalPrice, user);
            } else {
                return null; // ingen completed orders
            }
        } catch (SQLException e) {
            throw new DatabaseException("Could not get completed orders for user", e.getMessage());
        }
    }



    public static void insertOrderItems(List<OrderItem> orderItems, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "INSERT INTO order_item (order_id, product_variant_id, quantity, description) " + "VALUES (?,?,?,?)";
        try (Connection connection = connectionPool.getConnection()) {
            for (OrderItem orderItem : orderItems) {
                try (PreparedStatement ps = connection.prepareStatement(sql)) {
                    ps.setInt(1, orderItem.getOrder().getOrderId());
                    ps.setInt(2, orderItem.getProductVariant().getProductVariantId());
                    ps.setInt(3, orderItem.getQuantity());
                    ps.setString(4, orderItem.getDescription());
                    ps.executeUpdate();
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Could not insert order items", e.getMessage());
        }
    }


    public static List<OrderItem> getOrderItemByOrderId(int orderId, ConnectionPool connectionPool) throws DatabaseException {
        List<OrderItem> orderItemList = new ArrayList<>();
        String sql = "SELECT * FROM bill_of_materials_view WHERE order_id = ?";
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement prepareStatement = connection.prepareStatement(sql);
        ) {
            prepareStatement.setInt(1, orderId);
            var rs = prepareStatement.executeQuery();
            while (rs.next()) {
                int carportWidth = rs.getInt("carport_width");
                int carportLength = rs.getInt("carport_length");
                int status = rs.getInt("status");
                int totalPrice = rs.getInt("total_price");
                Order order = new Order(orderId, status, carportWidth, carportLength, totalPrice, null);

                int productId = rs.getInt("product_id");
                String name = rs.getString("name");
                String unit = rs.getString("unit");
                int price = rs.getInt("price");
                Product product = new Product(productId, name, unit, price);

                int productVariantId = rs.getInt("product_variant_id");
                String description = rs.getString("description");
                int length = rs.getInt("length");
                ProductVariant productVariant = new ProductVariant(productVariantId, product, length);

                int orderItemId = rs.getInt("order_item_id");
                int quantity = rs.getInt("quantity");
                OrderItem orderItem = new OrderItem(orderItemId, order, productVariant, quantity, description);
                orderItemList.add(orderItem);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Could not get order items", e.getMessage());
        }
        return orderItemList;
    }

    public static void updateOrderStatus(int orderId,int status, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "UPDATE orders SET status = ? WHERE order_id = ?";
        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)
        ) {
            ps.setInt(1, status);
            ps.setInt(2, orderId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Could not update order status", e.getMessage());
        }
    }

    public static Order getOrderById(int orderId, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "SELECT * FROM orders WHERE order_id = ?";
        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)
        ) {
            ps.setInt(1, orderId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int width = rs.getInt("carport_width");
                int length = rs.getInt("carport_length");
                int status = rs.getInt("status");
                int totalPrice = rs.getInt("total_price");

                return new Order(orderId, status, width, length, totalPrice, null);
            } else {
                throw new DatabaseException("Order not found with ID: " + orderId);
            }

        } catch (SQLException e) {
            throw new DatabaseException("Could not fetch order by ID", e.getMessage());
        }
    }

    public static Order getOrderByIdWithUser(int orderId, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "SELECT * FROM orders INNER JOIN users ON orders.users_id = users.users_id WHERE order_id = ?";
        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)
        ) {
            ps.setInt(1, orderId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int width = rs.getInt("carport_width");
                int length = rs.getInt("carport_length");
                int status = rs.getInt("status");
                int totalPrice = rs.getInt("total_price");

                User user = new User(
                        rs.getInt("users_id"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("role"),
                        rs.getString("phone")
                );

                return new Order(orderId, status, width, length, totalPrice, user);
            } else {
                throw new DatabaseException("Order not found with ID: " + orderId);
            }

        } catch (SQLException e) {
            throw new DatabaseException("Could not fetch order by ID with user", e.getMessage());
        }
    }

    public static List<Order> getAllPendingOrders(ConnectionPool pool)
    {
        List<Order> result = new ArrayList<>();
        String sql = "SELECT * FROM orders WHERE status = '3'";
        try (Connection con = pool.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)
        ){
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int orderId = rs.getInt("order_id");
                int width = rs.getInt("carport_width");
                int length = rs.getInt("carport_length");
                int status = rs.getInt("status");
                int totalPrice = rs.getInt("total_price");
                User user = null;
                try {
                    user = UserMapper.getUserById(rs.getInt("users_id"), pool);
                } catch (DatabaseException e){
                    System.out.println("Unable to create User from users_id");
                    System.out.println(e.getMessage());
                }

                result.add(new Order(orderId, status, width, length, totalPrice, user));
            }
        } catch (SQLException ex)
        {
            System.out.println(ex.getMessage());
        }
        return result;
    }

    public static void deleteOrderById(int orderId, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "DELETE FROM orders WHERE order_id = ?";
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Could not delete order", e.getMessage());
        }
    }

    public static void deleteOrderItemsByOrderId(int orderId, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "DELETE FROM order_item WHERE order_id = ?";
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Could not delete order items", e.getMessage());
        }
    }
}