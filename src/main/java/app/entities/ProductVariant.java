package app.entities;

public class ProductVariant {
    private int productVariantId;
    private Product product;
    private int length;

    public ProductVariant(int productVariantId, Product product, int length) {
        this.productVariantId = productVariantId;
        this.product = product;
        this.length = length;
    }

    public int getProductVariantId() {
        return productVariantId;
    }

    public void setProductVariantId(int productVariantId) {
        this.productVariantId = productVariantId;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }
}
