package app.entities;

public class User {
    private int userId;
    private String email;
    private String password;
    private String role;
    private String phone;
    private String address;
    private int zip;
    private String city;


    public User(int userId, String email, String password, String role, String phone, String address, int zip, String city) {
        this.userId = userId;
        this.email = email;
        this.password = password;
        this.role = role;
        this.phone = phone;
        this.address = address;
        this.zip = zip;
        this.city = city;
    }


    // Overloaded constructor for login and user creation (with phone, no address)
    public User(int userId, String email, String password, String role, String phone) {
        this(userId, email, password, role, phone, null, 0, null);
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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
