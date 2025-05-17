package app.service;

import app.exceptions.DatabaseException;
import app.model.Product;
import app.persistence.ProductMapper;

import javax.sql.DataSource;
import java.util.List;

/**
 * Service layer for product-related operations, such as searching carports by dimensions.
 */
public class ProductService {
    private final ProductMapper mapper;

    public ProductService(ProductMapper mapper) {
        this.mapper = mapper;
    }

    /**
     * Searches for carports meeting the specified minimum width and length.
     *
     * @param minWidth  minimum width in meters
     * @param minLength minimum length in meters
     * @param ds        JDBC DataSource
     * @return list of matching Product objects
     * @throws DatabaseException if a database error occurs
     */
    public List<Product> searchCarports(double minWidth,
                                        double minLength,
                                        DataSource ds) throws DatabaseException {
        return mapper.findCarportsByDimensions(minWidth, minLength, ds);
    }
}


