package app.persistence;

import app.exceptions.DatabaseException;
import app.model.Carport;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Mapper for Carport entities: CRUD and search operations against the 'carports' table.
 */
public class CarportMapper {

    /**
     * Finds a carport by its ID.
     *
     * @param id   the carport ID
     * @param pool the ConnectionPool
     * @return the Carport object, or null if not found
     * @throws DatabaseException on SQL errors
     */
    public static Carport findById(int id, ConnectionPool pool) throws DatabaseException {
        String sql = "SELECT carport_id, width, length, height FROM carports WHERE carport_id = ?";
        try (Connection conn = pool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Carport(
                            rs.getInt("carport_id"),
                            rs.getDouble("width"),
                            rs.getDouble("length"),
                            rs.getDouble("height")
                    );
                }
                return null;
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error finding carport with ID " + id, e);
        }
    }

    /**
     * Retrieves all carports.
     *
     * @param pool the ConnectionPool
     * @return list of Carport objects
     * @throws DatabaseException on SQL errors
     */
    public static List<Carport> findAll(ConnectionPool pool) throws DatabaseException {
        String sql = "SELECT carport_id, width, length, height FROM carports";
        List<Carport> list = new ArrayList<>();
        try (Connection conn = pool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new Carport(
                        rs.getInt("carport_id"),
                        rs.getDouble("width"),
                        rs.getDouble("length"),
                        rs.getDouble("height")
                ));
            }
            return list;
        } catch (SQLException e) {
            throw new DatabaseException("Error retrieving all carports", e);
        }
    }

    /**
     * Finds carports matching minimum dimensions.
     *
     * @param minWidth  minimum width in meters
     * @param minLength minimum length in meters
     * @param pool      the ConnectionPool
     * @return list of matching Carport objects
     * @throws DatabaseException on SQL errors
     */
    public static List<Carport> findByDimensions(double minWidth, double minLength, ConnectionPool pool) throws DatabaseException {
        String sql = "SELECT carport_id, width, length, height FROM carports"
                + " WHERE width >= ? AND length >= ? ORDER BY width, length";
        List<Carport> list = new ArrayList<>();
        try (Connection conn = pool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, minWidth);
            ps.setDouble(2, minLength);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new Carport(
                            rs.getInt("carport_id"),
                            rs.getDouble("width"),
                            rs.getDouble("length"),
                            rs.getDouble("height")
                    ));
                }
                return list;
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error searching carports by dimensions", e);
        }
    }

    /**
     * Creates a new carport record.
     *
     * @param width  width in meters
     * @param length length in meters
     * @param height height in meters
     * @param pool   the ConnectionPool
     * @throws DatabaseException on SQL errors
     */
    public static void createCarport(double width, double length, double height, ConnectionPool pool) throws DatabaseException {
        String sql = "INSERT INTO carports (width, length, height) VALUES (?, ?, ?)";
        try (Connection conn = pool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setDouble(1, width);
            ps.setDouble(2, length);
            ps.setDouble(3, height);
            int affected = ps.executeUpdate();
            if (affected != 1) {
                throw new DatabaseException("Failed to create carport");
            }
        } catch (SQLException e) {
            throw new DatabaseException("DB error creating carport", e);
        }
    }

    /**
     * Updates an existing carport.
     *
     * @param id     the carport ID
     * @param width  new width in meters
     * @param length new length in meters
     * @param height new height in meters
     * @param pool   the ConnectionPool
     * @throws DatabaseException on SQL errors
     */
    public static void updateCarport(int id, double width, double length, double height, ConnectionPool pool) throws DatabaseException {
        String sql = "UPDATE carports SET width = ?, length = ?, height = ? WHERE carport_id = ?";
        try (Connection conn = pool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, width);
            ps.setDouble(2, length);
            ps.setDouble(3, height);
            ps.setInt(4, id);
            int affected = ps.executeUpdate();
            if (affected != 1) {
                throw new DatabaseException("Failed to update carport with ID " + id);
            }
        } catch (SQLException e) {
            throw new DatabaseException("DB error updating carport", e);
        }
    }

    /**
     * Deletes a carport by its ID.
     *
     * @param id   the carport ID
     * @param pool the ConnectionPool
     * @throws DatabaseException on SQL errors
     */
    public static void deleteCarport(int id, ConnectionPool pool) throws DatabaseException {
        String sql = "DELETE FROM carports WHERE carport_id = ?";
        try (Connection conn = pool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            int affected = ps.executeUpdate();
            if (affected != 1) {
                throw new DatabaseException("Failed to delete carport with ID " + id);
            }
        } catch (SQLException e) {
            throw new DatabaseException("DB error deleting carport", e);
        }
    }
}
