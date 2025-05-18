package app.persistence;

import app.exceptions.DatabaseException;
import app.entities.Offer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * Mapper for Offer entities: CRUD operations against the 'offers' table.
 */
public class OfferMapper {

    /**
     * Finds an offer by its ID.
     *
     * @param id   the offer ID
     * @param pool the ConnectionPool
     * @return the Offer object, or null if not found
     * @throws DatabaseException on SQL errors
     */
    public static Offer findById(int id, ConnectionPool pool) throws DatabaseException {
        String sql = "SELECT offer_id, created, total_price, status, users_id, carport_id FROM offers WHERE offer_id = ?";
        try (Connection conn = pool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Offer(
                            rs.getInt("offer_id"),
                            rs.getDate("created"),
                            rs.getBigDecimal("total_price"),
                            rs.getString("status"),
                            rs.getInt("users_id"),
                            rs.getInt("carport_id")
                    );
                }
                return null;
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error finding offer with ID " + id, e);
        }
    }

    /**
     * Retrieves all offers.
     *
     * @param pool the ConnectionPool
     * @return list of Offer objects
     * @throws DatabaseException on SQL errors
     */
    public static List<Offer> findAll(ConnectionPool pool) throws DatabaseException {
        String sql = "SELECT offer_id, created, total_price, status, users_id, carport_id FROM offers";
        List<Offer> list = new ArrayList<>();
        try (Connection conn = pool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new Offer(
                        rs.getInt("offer_id"),
                        rs.getDate("created"),
                        rs.getBigDecimal("total_price"),
                        rs.getString("status"),
                        rs.getInt("users_id"),
                        rs.getInt("carport_id")
                ));
            }
            return list;
        } catch (SQLException e) {
            throw new DatabaseException("Error retrieving all offers", e);
        }
    }

    /**
     * Retrieves all offers for a given user.
     *
     * @param userId the user ID
     * @param pool   the ConnectionPool
     * @return list of Offer objects for the user
     * @throws DatabaseException on SQL errors
     */
    public static List<Offer> findByUserId(int userId, ConnectionPool pool) throws DatabaseException {
        String sql = "SELECT offer_id, created, total_price, status, users_id, carport_id FROM offers WHERE users_id = ?";
        List<Offer> list = new ArrayList<>();
        try (Connection conn = pool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new Offer(
                            rs.getInt("offer_id"),
                            rs.getDate("created"),
                            rs.getBigDecimal("total_price"),
                            rs.getString("status"),
                            rs.getInt("users_id"),
                            rs.getInt("carport_id")
                    ));
                }
                return list;
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error finding offers for user ID " + userId, e);
        }
    }

    /**
     * Creates a new offer.
     *
     * @param created    creation date of the offer
     * @param totalPrice total price
     * @param status     status of the offer
     * @param userId     ID of the user who created it
     * @param carportId  ID of the carport
     * @param pool       the ConnectionPool
     * @throws DatabaseException on SQL errors
     */
    public static void createOffer(Date created,
                                   java.math.BigDecimal totalPrice,
                                   String status,
                                   int userId,
                                   int carportId,
                                   ConnectionPool pool) throws DatabaseException {
        String sql = "INSERT INTO offers (created, total_price, status, users_id, carport_id) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = pool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setDate(1, created);
            ps.setBigDecimal(2, totalPrice);
            ps.setString(3, status);
            ps.setInt(4, userId);
            ps.setInt(5, carportId);
            int affected = ps.executeUpdate();
            if (affected != 1) {
                throw new DatabaseException("Failed to create offer");
            }
        } catch (SQLException e) {
            throw new DatabaseException("DB error creating offer", e);
        }
    }

    /**
     * Updates an existing offer.
     *
     * @param id         the offer ID
     * @param created    creation date of the offer
     * @param totalPrice total price
     * @param status     status of the offer
     * @param userId     ID of the user who created it
     * @param carportId  ID of the carport
     * @param pool       the ConnectionPool
     * @throws DatabaseException on SQL errors
     */
    public static void updateOffer(int id,
                                   Date created,
                                   java.math.BigDecimal totalPrice,
                                   String status,
                                   int userId,
                                   int carportId,
                                   ConnectionPool pool) throws DatabaseException {
        String sql = "UPDATE offers SET created=?, total_price=?, status=?, users_id=?, carport_id=? WHERE offer_id=?";
        try (Connection conn = pool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, created);
            ps.setBigDecimal(2, totalPrice);
            ps.setString(3, status);
            ps.setInt(4, userId);
            ps.setInt(5, carportId);
            ps.setInt(6, id);
            int affected = ps.executeUpdate();
            if (affected != 1) {
                throw new DatabaseException("Failed to update offer with ID " + id);
            }
        } catch (SQLException e) {
            throw new DatabaseException("DB error updating offer", e);
        }
    }

    /**
     * Deletes an offer by its ID.
     *
     * @param id   the offer ID
     * @param pool the ConnectionPool
     * @throws DatabaseException on SQL errors
     */
    public static void deleteOffer(int id, ConnectionPool pool) throws DatabaseException {
        String sql = "DELETE FROM offers WHERE offer_id = ?";
        try (Connection conn = pool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            int affected = ps.executeUpdate();
            if (affected != 1) {
                throw new DatabaseException("Failed to delete offer with ID " + id);
            }
        } catch (SQLException e) {
            throw new DatabaseException("DB error deleting offer", e);
        }
    }
}

