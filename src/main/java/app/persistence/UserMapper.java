package app.persistence;

import app.exceptions.DatabaseException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserMapper {
    public static boolean validateUser(String email, String password, DataSource connectionPool) throws DatabaseException {
        String sql = "SELECT * FROM users WHERE email = ? AND password = ?";
        try (Connection conn = connectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            return rs.next(); // true if found
        } catch (SQLException e) {
            throw new DatabaseException("Error validating user: " + e.getMessage());
        }
    }
}


