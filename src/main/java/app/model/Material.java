package app.model;

import java.math.BigDecimal;

public class Material {
    private int id;
    private String name;
    private String type;
    private BigDecimal price;
    private String description;

    // default constructor
    public Material() {}

    // fuld constructor
    public Material(int id, String name, String type, BigDecimal price, String description) {
        this.id          = id;
        this.name        = name;
        this.type        = type;
        this.price       = price;
        this.description = description;
    }

    // getters + setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    @Override
    public String toString() {
        return "Material{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", price=" + price +
                ", description='" + description + '\'' +
                '}';
    }
}

