package app.persistence;

import app.exceptions.DatabaseException;
import app.entities.Product;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Mapper for Product entities, specifically for searching carports by dimensions.
 */
public class ProductMapper {

    /**
     * Finds all carports in the 'carports' table that meet the minimum width and length.
     *
     * @param minWidth  minimum width in meters
     * @param minLength minimum length in meters
     * @param ds        JDBC DataSource
     * @return list of matching Product objects
     * @throws DatabaseException if a database error occurs
     */
    public List<Product> findCarportsByDimensions(double minWidth,
                                                  double minLength,
                                                  DataSource ds) throws DatabaseException {
        String sql = "SELECT carport_id AS id, " +
                "       'Carport ' || carport_id AS name, " +
                "       width, length, height AS price " +
                "FROM carports " +
                "WHERE width >= ? AND length >= ? " +
                "ORDER BY width, length";

        try (Connection conn = ds.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDouble(1, minWidth);
            ps.setDouble(2, minLength);

            try (ResultSet rs = ps.executeQuery()) {
                List<Product> results = new ArrayList<>();
                while (rs.next()) {
                    Product p = new Product();
                    p.setId(rs.getInt("id"));
                    p.setName(rs.getString("name"));
                    p.setWidth(rs.getDouble("width"));
                    p.setLength(rs.getDouble("length"));
                    p.setPrice(rs.getBigDecimal("price"));
                    results.add(p);
                }
                return results;
            }

        } catch (SQLException e) {
            throw new DatabaseException("Fejl ved søgning af carporte efter dimensioner", e);
        }
    }

    // TODO: Add other product-related mapper methods (e.g., findById, create, update, delete) here
}
