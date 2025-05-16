package app.controllers;

import app.entities.User;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.UserMapper;
import io.javalin.http.Context;


public class HomeController {
    private static ConnectionPool connectionPool;

    public HomeController(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    public static void handleLogin(Context ctx, ConnectionPool connectionPool) {
        String email = ctx.formParam("email");
        String password = ctx.formParam("password");

        try {
            User user = UserMapper.login(email, password, connectionPool);

            // Stores user attributes in session
            ctx.sessionAttribute("users_id", user.getUserId());
            ctx.sessionAttribute("role", user.getRole());
            ctx.sessionAttribute("email", user.getEmail());

            if ("admin".equals(user.getRole())) {
                ctx.redirect("/admin");  // Redirect admins to admin panel
            } else {
                ctx.redirect("/shop");
            }
        } catch (DatabaseException e) {
            ctx.attribute("message", e.getMessage());
            ctx.render("index.html");
        }
    }

    public static void handleCreateUser(Context ctx, ConnectionPool connectionPool) {
        // Retrieve user information from the form
        String email = ctx.formParam("email");
        String password = ctx.formParam("password");
        String role = "user"; // Standard role

        try {
            // Create the new user in the database
            UserMapper.createUser(email, password, connectionPool);
            ctx.attribute("message", "User created successfully!");
            ctx.redirect("/login"); // Redirect to login page after successful user creation
        } catch (DatabaseException e) {
            // If the email is already in use, display an error message
            String errorMessage = e.getMessage();
            if (errorMessage.contains("Email already in use")) {
                ctx.attribute("message", "This email is already in use. Please login or use another email.");
            } else {
                ctx.attribute("message", "Error creating user: " + e.getMessage());
            }
            // Stay on the create-user page if there is an error
            ctx.render("/create-user.html");
        }
    }

    public static void home(Context ctx) throws DatabaseException {
        ctx.render("index.html");
    }

//    public static void showPaymentPage(Context ctx, ConnectionPool connectionPool) {
//        try {
//            int userId = ctx.sessionAttribute("users_id");
//            double balance = UserMapper.getUserBalance(userId, connectionPool); // Get user balance
//            int orderId = OrderMapper.getLatestOrderId(userId, connectionPool); // Get latest order ID
//            double totalPrice = OrderMapper.getTotalOrderPrice(orderId, connectionPool); // Sum all orderLines
//            String email =  ctx.sessionAttribute("email");
//
//            ctx.attribute("balance", balance);
//            ctx.attribute("orders_id", orderId);
//            ctx.attribute("total_price", totalPrice);
//            ctx.attribute("email",email);
//
//            ctx.render("payment.html");
//        } catch (DatabaseException e) {
//            ctx.attribute("message", "Error retrieving payment details: " + e.getMessage());
//            ctx.render("error.html");
//        }
}

