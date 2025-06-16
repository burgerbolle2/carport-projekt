package app.controllers;

import app.entities.User;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.UserMapper;
import io.javalin.http.Context;
import app.util.GmailEmailSender;

import jakarta.mail.MessagingException;

/**
 * Controller for home, login and registration.
 */

public class HomeController {
    private static ConnectionPool connectionPool;
    private static GmailEmailSender emailSender;

    public HomeController(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    /**
     * Show the login page.
     */

    public void home(Context ctx) throws DatabaseException {
        ctx.render("index.html");
    }

    /**
     * Handle login form submission.
     */

    public static void handleLogin(Context ctx, ConnectionPool connectionPool) {
        String email = ctx.formParam("email");
        String password = ctx.formParam("password");
        try {
            User user = UserMapper.login(email, password, connectionPool);
            ctx.sessionAttribute("users_id", user.getUserId());
            ctx.sessionAttribute("role", user.getRole());
            ctx.sessionAttribute("email", user.getEmail());

            if ("admin".equals(user.getRole())) {
                ctx.redirect("/admin");
            } else {
                ctx.redirect("/find-carport");
            }
        } catch (DatabaseException e) {
            ctx.attribute("message", e.getMessage());
            ctx.render("index.html");
        }
    }

    /**
     * Handle user registration form submission.
     */

    public static void handleCreateUser(Context ctx, ConnectionPool connectionPool) {
        // Retrieve user information from the form
        String email = ctx.formParam("email");
        String password = ctx.formParam("password");
        String phone = ctx.formParam("phone");

        try {
            // Create the new user in the database
            UserMapper.createUser(email, password, phone, connectionPool);
            ctx.attribute("message", "User created successfully!");
            ctx.redirect("/"); // Redirect to login page after successful user creation
        } catch (DatabaseException e) {
            // If the email is already in use, display an error message
            String errorMessage = e.getMessage();
            if (errorMessage.contains("Email already in use")) {
                ctx.attribute("message", "This email is already in use. Please login or use another email.");
            } else {
                ctx.attribute("message", "Error creating user: " + e.getMessage());
            }
            // Stay on the create-user page if there is an error
            ctx.render("/register.html");
        }
    }
}
