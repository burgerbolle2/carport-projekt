package app.entities;

/**
 * POJO representing a carport entity in the application.
 */
public class Carport {
    private int id;
    private double width;
    private double length;
    private double height;

    /**
     * Default constructor.
     */
    public Carport() {
    }

    /**
     * Full-args constructor.
     *
     * @param id      the carport ID
     * @param width   the width in meters
     * @param length  the length in meters
     * @param height  the height in meters
     */
    public Carport(int id, double width, double length, double height) {
        this.id = id;
        this.width = width;
        this.length = length;
        this.height = height;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    @Override
    public String toString() {
        return "Carport{" +
                "id=" + id +
                ", width=" + width +
                ", length=" + length +
                ", height=" + height +
                '}';
    }
}

