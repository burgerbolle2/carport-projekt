package app.persistence;

import app.entities.Order;
import app.entities.User;
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
                int orderId = rs.getInt("order_id");
                int carportWidth = rs.getInt("carport_width");
                int carportLength = rs.getInt("carport_length");
                int status = rs.getInt("status");
                int totalPrice = rs.getInt("total_price");
                User user = new User(userId, email, password, role);
                Order order = new Order(orderId, carportWidth, carportLength, status, totalPrice, user);
                orderList.add(order);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Could not get users from DB", e.getMessage());
        }
        return orderList;
    }

    public static Order insertOrder(Order order, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "INSERT INTO orders (carport_width, carport_length, status, users_id, total_price) " + "VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = connectionPool.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, order.getCarportWidth());
                ps.setInt(2, order.getCarportLength());
                ps.setInt(3, 1);
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
        }
        catch (SQLException e) {
            throw new DatabaseException("Could not insert order", e.getMessage());
        }
    }
}
