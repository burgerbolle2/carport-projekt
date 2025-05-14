package app.model;

import java.math.BigDecimal;


public class Product {
    private int id;
    private String name;
    private double width;
    private double length;
    private BigDecimal price;


    public Product() {
    }

    /**
     * Full-args constructor.
     *
     * @param id      the product ID
     * @param name    the product name
     * @param width   the width in meters
     * @param length  the length in meters
     * @param price   the price of the product
     */
    public Product(int id, String name, double width, double length, BigDecimal price) {
        this.id = id;
        this.name = name;
        this.width = width;
        this.length = length;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", width=" + width +
                ", length=" + length +
                ", price=" + price +
                '}';
    }
}

