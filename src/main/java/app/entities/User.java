package app.entities;

public class User {
    private int userId;
    private String email;
    private String password;
    private String role;
    private String phone;

    public User(int userId, String email, String password, String role, String phone) {
        this.userId = userId;
        this.email = email;
        this.password = password;
        this.role = role;
        this.phone = phone;
    }

    // Overloaded constructor for backward compatibility
    public User(int userId, String email, String password, String role) {
        this(userId, email, password, role, null);
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
}
