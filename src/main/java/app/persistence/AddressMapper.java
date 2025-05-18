package app.persistence;

import app.exceptions.DatabaseException;
import app.entities.Address;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Mapper for Address entities: CRUD operations against the 'addresses' table.
 */
public class AddressMapper {

    /**
     * Finds an address by its ID.
     *
     * @param id   the address ID
     * @param pool the ConnectionPool
     * @return the Address object, or null if not found
     * @throws DatabaseException on SQL errors
     */
    public static Address findById(int id, ConnectionPool pool) throws DatabaseException {
        String sql = "SELECT address_id, street, city, zipcode FROM addresses WHERE address_id = ?";
        try (Connection conn = pool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Address(
                            rs.getInt("address_id"),
                            rs.getString("street"),
                            rs.getString("city"),
                            rs.getString("zipcode")
                    );
                }
                return null;
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error finding address with ID " + id, e);
        }
    }

    /**
     * Retrieves all addresses.
     *
     * @param pool the ConnectionPool
     * @return list of Address objects
     * @throws DatabaseException on SQL errors
     */
    public static List<Address> findAll(ConnectionPool pool) throws DatabaseException {
        String sql = "SELECT address_id, street, city, zipcode FROM addresses";
        List<Address> list = new ArrayList<>();
        try (Connection conn = pool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new Address(
                        rs.getInt("address_id"),
                        rs.getString("street"),
                        rs.getString("city"),
                        rs.getString("zipcode")
                ));
            }
            return list;
        } catch (SQLException e) {
            throw new DatabaseException("Error retrieving all addresses", e);
        }
    }

    /**
     * Creates a new address record.
     *
     * @param street      street name and number
     * @param city        city name
     * @param zipcode     postal code
     * @param pool        the ConnectionPool
     * @throws DatabaseException on SQL errors
     */
    public static void createAddress(String street, String city, String zipcode, ConnectionPool pool) throws DatabaseException {
        String sql = "INSERT INTO addresses (street, city, zipcode) VALUES (?, ?, ?)";
        try (Connection conn = pool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, street);
            ps.setString(2, city);
            ps.setString(3, zipcode);

            int affected = ps.executeUpdate();
            if (affected != 1) {
                throw new DatabaseException("Failed to create address");
            }
        } catch (SQLException e) {
            throw new DatabaseException("DB error creating address", e);
        }
    }

    /**
     * Updates an existing address.
     *
     * @param id          the address ID
     * @param street      new street name and number
     * @param city        new city name
     * @param zipcode     new postal code
     * @param pool        the ConnectionPool
     * @throws DatabaseException on SQL errors
     */
    public static void updateAddress(int id, String street, String city, String zipcode, ConnectionPool pool) throws DatabaseException {
        String sql = "UPDATE addresses SET street = ?, city = ?, zipcode = ? WHERE address_id = ?";
        try (Connection conn = pool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, street);
            ps.setString(2, city);
            ps.setString(3, zipcode);
            ps.setInt(4, id);

            int affected = ps.executeUpdate();
            if (affected != 1) {
                throw new DatabaseException("Failed to update address with ID " + id);
            }
        } catch (SQLException e) {
            throw new DatabaseException("DB error updating address", e);
        }
    }

    /**
     * Deletes an address by its ID.
     *
     * @param id   the address ID
     * @param pool the ConnectionPool
     * @throws DatabaseException on SQL errors
     */
    public static void deleteAddress(int id, ConnectionPool pool) throws DatabaseException {
        String sql = "DELETE FROM addresses WHERE address_id = ?";
        try (Connection conn = pool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            int affected = ps.executeUpdate();
            if (affected != 1) {
                throw new DatabaseException("Failed to delete address with ID " + id);
            }
        } catch (SQLException e) {
            throw new DatabaseException("DB error deleting address", e);
        }
    }
}

