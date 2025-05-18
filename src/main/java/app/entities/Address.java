package app.entities;

/**
 * POJO representing an address entity in the application.
 */
public class Address {
    private int id;
    private String street;
    private String city;
    private String zipcode;

    /**
     * Default constructor.
     */
    public Address() {
    }

    /**
     * Full-args constructor.
     *
     * @param id      the address ID
     * @param street  the street name and number
     * @param city    the city name
     * @param zipcode the postal code
     */
    public Address(int id, String street, String city, String zipcode) {
        this.id = id;
        this.street = street;
        this.city = city;
        this.zipcode = zipcode;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    @Override
    public String toString() {
        return "Address{" +
                "id=" + id +
                ", street='" + street + '\'' +
                ", city='" + city + '\'' +
                ", zipcode='" + zipcode + '\'' +
                '}';
    }
}

