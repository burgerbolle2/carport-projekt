package app.persistence;

import app.exceptions.DatabaseException;
import app.model.Order;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * Mapper for Order entities: CRUD operations against the 'orders' table.
 */
public class OrderMapper {

    /**
     * Finds an order by its ID.
     *
     * @param id   the order ID
     * @param pool the ConnectionPool
     * @return the Order object, or null if not found
     * @throws DatabaseException on SQL errors
     */
    public static Order findById(int id, ConnectionPool pool) throws DatabaseException {
        String sql = "SELECT order_id, total_price, status, date, users_id, offer_id FROM orders WHERE order_id = ?";
        try (Connection conn = pool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Order(
                            rs.getInt("order_id"),
                            rs.getBigDecimal("total_price"),
                            rs.getString("status"),
                            rs.getDate("date"),
                            rs.getInt("users_id"),
                            rs.getInt("offer_id")
                    );
                }
                return null;
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error finding order with ID " + id, e);
        }
    }

    /**
     * Retrieves all orders.
     *
     * @param pool the ConnectionPool
     * @return list of Order objects
     * @throws DatabaseException on SQL errors
     */
    public static List<Order> findAll(ConnectionPool pool) throws DatabaseException {
        String sql = "SELECT order_id, total_price, status, date, users_id, offer_id FROM orders";
        List<Order> list = new ArrayList<>();
        try (Connection conn = pool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new Order(
                        rs.getInt("order_id"),
                        rs.getBigDecimal("total_price"),
                        rs.getString("status"),
                        rs.getDate("date"),
                        rs.getInt("users_id"),
                        rs.getInt("offer_id")
                ));
            }
            return list;
        } catch (SQLException e) {
            throw new DatabaseException("Error retrieving all orders", e);
        }
    }

    /**
     * Retrieves orders for a specific user.
     *
     * @param userId the user ID
     * @param pool   the ConnectionPool
     * @return list of Order objects for the user
     * @throws DatabaseException on SQL errors
     */
    public static List<Order> findByUserId(int userId, ConnectionPool pool) throws DatabaseException {
        String sql = "SELECT order_id, total_price, status, date, users_id, offer_id FROM orders WHERE users_id = ?";
        List<Order> list = new ArrayList<>();
        try (Connection conn = pool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new Order(
                            rs.getInt("order_id"),
                            rs.getBigDecimal("total_price"),
                            rs.getString("status"),
                            rs.getDate("date"),
                            rs.getInt("users_id"),
                            rs.getInt("offer_id")
                    ));
                }
                return list;
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error finding orders for user ID " + userId, e);
        }
    }

    /**
     * Retrieves orders for a specific offer.
     *
     * @param offerId the offer ID
     * @param pool    the ConnectionPool
     * @return list of Order objects for the offer
     * @throws DatabaseException on SQL errors
     */
    public static List<Order> findByOfferId(int offerId, ConnectionPool pool) throws DatabaseException {
        String sql = "SELECT order_id, total_price, status, date, users_id, offer_id FROM orders WHERE offer_id = ?";
        List<Order> list = new ArrayList<>();
        try (Connection conn = pool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, offerId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new Order(
                            rs.getInt("order_id"),
                            rs.getBigDecimal("total_price"),
                            rs.getString("status"),
                            rs.getDate("date"),
                            rs.getInt("users_id"),
                            rs.getInt("offer_id")
                    ));
                }
                return list;
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error finding orders for offer ID " + offerId, e);
        }
    }

    /**
     * Creates a new order.
     *
     * @param totalPrice total price of the order
     * @param status     status of the order
     * @param date       the order date
     * @param userId     ID of the user
     * @param offerId    ID of the offer
     * @param pool       the ConnectionPool
     * @throws DatabaseException on SQL errors
     */
    public static void createOrder(java.math.BigDecimal totalPrice,
                                   String status,
                                   Date date,
                                   int userId,
                                   int offerId,
                                   ConnectionPool pool) throws DatabaseException {
        String sql = "INSERT INTO orders (total_price, status, date, users_id, offer_id) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = pool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setBigDecimal(1, totalPrice);
            ps.setString(2, status);
            ps.setDate(3, date);
            ps.setInt(4, userId);
            ps.setInt(5, offerId);
            int affected = ps.executeUpdate();
            if (affected != 1) {
                throw new DatabaseException("Failed to create order");
            }
        } catch (SQLException e) {
            throw new DatabaseException("DB error creating order", e);
        }
    }

    /**
     * Updates an existing order.
     *
     * @param id         the order ID
     * @param totalPrice new total price
     * @param status     new status
     * @param date       new order date
     * @param userId     new user ID
     * @param offerId    new offer ID
     * @param pool       the ConnectionPool
     * @throws DatabaseException on SQL errors
     */
    public static void updateOrder(int id,
                                   java.math.BigDecimal totalPrice,
                                   String status,
                                   Date date,
                                   int userId,
                                   int offerId,
                                   ConnectionPool pool) throws DatabaseException {
        String sql = "UPDATE orders SET total_price=?, status=?, date=?, users_id=?, offer_id=? WHERE order_id=?";
        try (Connection conn = pool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBigDecimal(1, totalPrice);
            ps.setString(2, status);
            ps.setDate(3, date);
            ps.setInt(4, userId);
            ps.setInt(5, offerId);
            ps.setInt(6, id);
            int affected = ps.executeUpdate();
            if (affected != 1) {
                throw new DatabaseException("Failed to update order with ID " + id);
            }
        } catch (SQLException e) {
            throw new DatabaseException("DB error updating order", e);
        }
    }

    /**
     * Deletes an order by its ID.
     *
     * @param id   the order ID
     * @param pool the ConnectionPool
     * @throws DatabaseException on SQL errors
     */
    public static void deleteOrder(int id, ConnectionPool pool) throws DatabaseException {
        String sql = "DELETE FROM orders WHERE order_id = ?";
        try (Connection conn = pool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            int affected = ps.executeUpdate();
            if (affected != 1) {
                throw new DatabaseException("Failed to delete order with ID " + id);
            }
        } catch (SQLException e) {
            throw new DatabaseException("DB error deleting order", e);
        }
    }
}

