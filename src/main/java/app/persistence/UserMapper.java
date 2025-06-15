package app.persistence;

import app.entities.User;
import app.entities.Zip;
import app.exceptions.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class UserMapper {

    public static User login(String email, String password, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "SELECT * FROM users WHERE email=? AND password=?";
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, email);
            ps.setString(2, password);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("users_id");
                    String role = rs.getString("role");
                    String phone = rs.getString("phone");
                    return new User(id, email, password, role, phone);
                } else {
                    throw new DatabaseException("Invalid email or password");
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("DB error during login", e.getMessage());
        }
    }


    public static void createUser(String email, String password, String phone, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "INSERT INTO users (email, password, role, phone) VALUES (?, ?, ?, ?)";
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, email);
            ps.setString(2, password);
            ps.setString(3, "user");
            ps.setString(4, phone);
            int rows = ps.executeUpdate();
            if (rows != 1) {
                throw new DatabaseException("Error when creating user");
            }
        } catch (SQLException e) {
            String msg = "An error occurred, please try again";
            if (e.getMessage().startsWith("ERROR: duplicate key value")) {
                msg = "Email already in use, use another email";
            }
            throw new DatabaseException(msg, e.getMessage());
        }
    }


    public static String getUserEmailByID(int userId, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "SELECT email FROM users WHERE users_id = ?";
        try (Connection conn = connectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("email");
                } else {
                    throw new DatabaseException("User not found with ID " + userId);
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Database error retrieving email for user " + userId, e.getMessage());
        }
    }

    public static User getUserById(int userId, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "SELECT u.*, z.city FROM users u LEFT JOIN zip z ON u.zip = z.zip WHERE u.users_id = ?";
        try (Connection conn = connectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int zip = rs.getInt("zip");
                String city = rs.getString("city");
                return new User(
                        rs.getInt("users_id"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("role"),
                        rs.getString("phone"),
                        rs.getString("address"),
                        zip,
                        city
                );
            } else {
                throw new DatabaseException("User not found");
            }
        } catch (SQLException e) {
            throw new DatabaseException("Could not fetch user", e.getMessage());
        }
    }


    public static List<User> getAllUsers(ConnectionPool connectionPool) throws DatabaseException {
        List<User> users = new ArrayList<>();
        String sql = "SELECT u.*, z.city FROM users u LEFT JOIN zip z ON u.zip = z.zip WHERE u.role = 'user'";
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                int id = rs.getInt("users_id");
                String email = rs.getString("email");
                String password = rs.getString("password");
                String role = rs.getString("role");
                String phone = rs.getString("phone");
                String address = rs.getString("address");
                int zip = rs.getInt("zip");
                String city = rs.getString("city");
                users.add(new User(id, email, password, role, phone, address, zip, city));
            }
        } catch (SQLException e) {
            throw new DatabaseException("DB error during fetching users", e.getMessage());
        }
        return users;
    }


    public static void updateUserAddress(int userId, String address, Zip zip, ConnectionPool connectionPool) throws DatabaseException {
        // Insert zip and city if not exists
        String insertZipSql = "INSERT INTO zip (zip, city) VALUES (?, ?) ON CONFLICT (zip) DO NOTHING";
        String updateUserSql = "UPDATE users SET address = ?, zip = ? WHERE users_id = ?";
        try (Connection conn = connectionPool.getConnection()) {
            // Insert zip if not exists
            try (PreparedStatement psZip = conn.prepareStatement(insertZipSql)) {
                psZip.setInt(1, zip.getZip());
                psZip.setString(2, zip.getCity());
                psZip.executeUpdate();
            }

            try (PreparedStatement psUser = conn.prepareStatement(updateUserSql)) {
                psUser.setString(1, address);
                psUser.setInt(2, zip.getZip());
                psUser.setInt(3, userId);
                int rows = psUser.executeUpdate();
                if (rows != 1) {
                    throw new DatabaseException("Failed to update address for user");
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Database error updating address and zip", e.getMessage());
        }
    }
}
