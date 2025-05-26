package app.persistence;

import app.entities.Product;
import app.entities.ProductVariant;
import app.exceptions.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductMapper {
    public static List<ProductVariant> getVariantByProductIdAndMinLength(int minLength, int productId, ConnectionPool connectionPool) throws DatabaseException {
        List<ProductVariant> productVariants = new ArrayList<>();
        String sql = "select * from product_variant " + "INNER JOIN product p USING(product_id) " + "WHERE product_id = ? AND length >= ?";
        try (Connection connection = connectionPool.getConnection()) {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, productId);
            ps.setInt(2, minLength);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int variantId = rs.getInt("product_variant_id");
                int product_id = rs.getInt("product_id");
                int length = rs.getInt("length");
                String name = rs.getString("name");
                String unit = rs.getString("unit");
                int price = rs.getInt("price");
                Product product = new Product(product_id, name, unit, price);
                ProductVariant productVariant = new ProductVariant(variantId, product, length);
                productVariants.add(productVariant);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Could not get product from database", e.getMessage());
        }
        return productVariants;
    }
}
