package app.service;

import app.exceptions.DatabaseException;
import app.model.Product;
import javax.sql.DataSource;
import java.util.List;

public class ProductService {
    private final ProductMapper mapper;

    public ProductService(ProductMapper mapper) {
        this.mapper = mapper;
    }

    public List<Product> searchCarports(double minWidth,
                                        double minLength,
                                        DataSource ds) throws DatabaseException {
        return mapper.findCarportsByDimensions(minWidth, minLength, ds);
    }


}

