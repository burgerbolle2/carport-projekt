package app.persistence;

import app.exceptions.DatabaseException;
import app.model.Product;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductMapper {

    /**
     * Finder alle carporte i produkt-tabellen, som har mindst den angivne bredde og længde.
     *
     * @param minWidth   minimumsbredde i meter
     * @param minLength  minimumslængde i meter
     * @param ds         JDBC DataSource (HikariCP-pool)
     * @return liste af matchende Product-objekter
     * @throws DatabaseException hvis der opstår en SQL-fejl
     */
    public List<Product> findCarportsByDimensions(double minWidth,
                                                  double minLength,
                                                  DataSource ds) throws DatabaseException {
        String sql = ""
                + "SELECT id, name, width, length, price "
                + "FROM products "
                + "WHERE category = 'carport' "
                + "  AND width  >= ? "
                + "  AND length >= ? "
                + "ORDER BY width, length";

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
            throw new DatabaseException("Fejl ved søgning på carport-dimensioner", e);
        }
    }

    // Placeholder til andre Product-relaterede mapper-metoder:
    // public Product findById(int id, DataSource ds) { … }
    // public void createProduct(Product p, DataSource ds) { … }
    // public void updateProduct(Product p, DataSource ds) { … }
    // public void deleteProduct(int id, DataSource ds) { … }
}


