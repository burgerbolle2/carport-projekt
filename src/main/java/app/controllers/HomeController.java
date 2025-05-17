// File: src/main/java/app/controllers/HomeController.java
package app.controllers;

import app.entities.User;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.UserMapper;
import io.javalin.http.Context;

public class HomeController {
    private final ConnectionPool pool;

    public HomeController(ConnectionPool pool) {
        this.pool = pool;
    }

    public void home(Context ctx) {
        ctx.render("index.html");
    }

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
                ctx.redirect("/find-carport");   // <-- redirect til find-carport
            }
        } catch (DatabaseException e) {
            ctx.attribute("message", e.getMessage());
            ctx.render("index.html");
        }
    }

    public void handleCreateUser(Context ctx) {
        // … din eksisterende createUser-kode, også som instans-metode …
    }
}


