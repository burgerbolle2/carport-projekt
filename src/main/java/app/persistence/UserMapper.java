package app.persistence;

import app.entities.User;
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
                    return new User(id, email, password, role);
                } else {
                    throw new DatabaseException("Invalid email or password");
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("DB error during login", e.getMessage());
        }
    }


    public static void createUser(String email, String password, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "INSERT INTO users (email, password, role) VALUES (?, ?, ?)";
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, email);
            ps.setString(2, password);
            ps.setString(3, "user");
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
        String sql = "SELECT * FROM users WHERE users_id = ?";
        try (Connection conn = connectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new User(
                        rs.getInt("users_id"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("role")
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
        String sql = "SELECT users_id, email, password, role FROM users";
        try (Connection conn = connectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                users.add(new User(
                        rs.getInt("users_id"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("role")
                ));
            }
            return users;
        } catch (SQLException e) {
            throw new DatabaseException("Error fetching users", e.getMessage());
        }
    }
}

