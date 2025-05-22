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
    private final ConnectionPool pool;
    private final GmailEmailSender emailSender;

    public HomeController(ConnectionPool pool) {
        this.pool = pool;
        this.emailSender = new GmailEmailSender();
    }

    /**
     * Show the login page.
     */
    public void home(Context ctx) {
        ctx.render("index.html");
    }

    /**
     * Handle login form submission.
     */
    public void handleLogin(Context ctx) {
        String email    = ctx.formParam("email");
        String password = ctx.formParam("password");
        try {
            User user = UserMapper.login(email, password, pool);
            ctx.sessionAttribute("users_id", user.getUserId());
            ctx.sessionAttribute("role",     user.getRole());
            ctx.sessionAttribute("email",    user.getEmail());

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
    public void handleCreateUser(Context ctx) {
        String email    = ctx.formParam("email");
        String password = ctx.formParam("password");

        try {
            UserMapper.createUser(email, password, pool);

            // Send welcome email
            try {
                String subject = "Velkommen til FOG!";
                String body    = "Hej " + email + ",\n\nTak for din registrering hos FOG.\n\nMvh. FOG-teamet";
                emailSender.sendPlainTextEmail(email, subject, body);
            } catch (MessagingException me) {
                // Log error but do not interrupt user flow
                me.printStackTrace();
            }

            ctx.attribute("message", "Bruger oprettet – se din mail for bekræftelse!");
            ctx.redirect("/");
        } catch (DatabaseException e) {
            ctx.attribute("message", "Fejl: " + e.getMessage());
            ctx.render("register.html");
        }
    }
}




