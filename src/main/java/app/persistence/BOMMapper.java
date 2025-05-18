package app.persistence;

import app.exceptions.DatabaseException;
import app.entities.BOM;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Mapper for BOM (Bill of Materials) entities: CRUD and lookup operations against the 'bom' table.
 */
public class BOMMapper {

    /**
     * Finds a BOM entry by its ID.
     *
     * @param id   the BOM ID
     * @param pool the ConnectionPool
     * @return the BOM object, or null if not found
     * @throws DatabaseException on SQL errors
     */
    public static BOM findById(int id, ConnectionPool pool) throws DatabaseException {
        String sql = "SELECT bom_id, offer_id, material_id, quantity, unit FROM bom WHERE bom_id = ?";
        try (Connection conn = pool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new BOM(
                            rs.getInt("bom_id"),
                            rs.getInt("offer_id"),
                            rs.getInt("material_id"),
                            rs.getInt("quantity"),
                            rs.getString("unit")
                    );
                }
                return null;
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error finding BOM with ID " + id, e);
        }
    }

    /**
     * Retrieves all BOM entries.
     *
     * @param pool the ConnectionPool
     * @return list of BOM objects
     * @throws DatabaseException on SQL errors
     */
    public static List<BOM> findAll(ConnectionPool pool) throws DatabaseException {
        String sql = "SELECT bom_id, offer_id, material_id, quantity, unit FROM bom";
        List<BOM> list = new ArrayList<>();
        try (Connection conn = pool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new BOM(
                        rs.getInt("bom_id"),
                        rs.getInt("offer_id"),
                        rs.getInt("material_id"),
                        rs.getInt("quantity"),
                        rs.getString("unit")
                ));
            }
            return list;
        } catch (SQLException e) {
            throw new DatabaseException("Error retrieving all BOM entries", e);
        }
    }

    /**
     * Retrieves BOM entries for a specific offer.
     *
     * @param offerId the offer ID
     * @param pool    the ConnectionPool
     * @return list of BOM objects for the offer
     * @throws DatabaseException on SQL errors
     */
    public static List<BOM> findByOfferId(int offerId, ConnectionPool pool) throws DatabaseException {
        String sql = "SELECT bom_id, offer_id, material_id, quantity, unit FROM bom WHERE offer_id = ?";
        List<BOM> list = new ArrayList<>();
        try (Connection conn = pool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, offerId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new BOM(
                            rs.getInt("bom_id"),
                            rs.getInt("offer_id"),
                            rs.getInt("material_id"),
                            rs.getInt("quantity"),
                            rs.getString("unit")
                    ));
                }
                return list;
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error finding BOM entries for offer ID " + offerId, e);
        }
    }

    /**
     * Creates a new BOM entry.
     *
     * @param offerId     the offer ID
     * @param materialId  the material ID
     * @param quantity    the quantity
     * @param unit        the unit of measure
     * @param pool        the ConnectionPool
     * @throws DatabaseException on SQL errors
     */
    public static void createBOM(int offerId, int materialId, int quantity, String unit, ConnectionPool pool) throws DatabaseException {
        String sql = "INSERT INTO bom (offer_id, material_id, quantity, unit) VALUES (?, ?, ?, ?)";
        try (Connection conn = pool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, offerId);
            ps.setInt(2, materialId);
            ps.setInt(3, quantity);
            ps.setString(4, unit);
            int affected = ps.executeUpdate();
            if (affected != 1) {
                throw new DatabaseException("Failed to create BOM entry");
            }
        } catch (SQLException e) {
            throw new DatabaseException("DB error creating BOM entry", e);
        }
    }

    /**
     * Updates an existing BOM entry.
     *
     * @param id          the BOM ID
     * @param offerId     the new offer ID
     * @param materialId  the new material ID
     * @param quantity    the new quantity
     * @param unit        the new unit of measure
     * @param pool        the ConnectionPool
     * @throws DatabaseException on SQL errors
     */
    public static void updateBOM(int id, int offerId, int materialId, int quantity, String unit, ConnectionPool pool) throws DatabaseException {
        String sql = "UPDATE bom SET offer_id = ?, material_id = ?, quantity = ?, unit = ? WHERE bom_id = ?";
        try (Connection conn = pool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, offerId);
            ps.setInt(2, materialId);
            ps.setInt(3, quantity);
            ps.setString(4, unit);
            ps.setInt(5, id);
            int affected = ps.executeUpdate();
            if (affected != 1) {
                throw new DatabaseException("Failed to update BOM entry with ID " + id);
            }
        } catch (SQLException e) {
            throw new DatabaseException("DB error updating BOM entry", e);
        }
    }

    /**
     * Deletes a BOM entry by its ID.
     *
     * @param id   the BOM ID
     * @param pool the ConnectionPool
     * @throws DatabaseException on SQL errors
     */
    public static void deleteBOM(int id, ConnectionPool pool) throws DatabaseException {
        String sql = "DELETE FROM bom WHERE bom_id = ?";
        try (Connection conn = pool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            int affected = ps.executeUpdate();
            if (affected != 1) {
                throw new DatabaseException("Failed to delete BOM entry with ID " + id);
            }
        } catch (SQLException e) {
            throw new DatabaseException("DB error deleting BOM entry", e);
        }
    }
}

