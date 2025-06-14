package app.entities;

public class Zip {
    private int zip;
    private String city;

    public Zip(int zip, String city) {
        this.zip = zip;
        this.city = city;
    }
    public int getZip() {
        return zip;
    }
    public void setZip(int zip) {
        this.zip = zip;
    }
    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }
}
