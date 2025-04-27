package app;

import app.config.ThymeleafConfig;
import app.persistence.ConnectionPool;
import app.persistence.UserMapper;
import io.javalin.Javalin;
import io.javalin.rendering.template.JavalinThymeleaf;
import io.javalin.http.staticfiles.Location;

import javax.sql.DataSource;

public class Main {
    public static void main(String[] args) {

        // ✅ Create the connection pool once
        DataSource connectionPool = ConnectionPool.getInstance(
                "postgres",
                "postgres",
                "jdbc:postgresql://localhost:5432/%s?currentSchema=public",
                "FOG"
        );

        // ✅ Set up Javalin app
        Javalin app = Javalin.create(config -> {
            config.staticFiles.add(staticFiles -> {
                staticFiles.hostedPath = "/";
                staticFiles.directory = "public"; // folder "resources/public" for images etc.
                staticFiles.location = Location.CLASSPATH;
            });
            config.fileRenderer(new JavalinThymeleaf(ThymeleafConfig.templateEngine()));
        }).start(7070);

        // ✅ Serve login page
        app.get("/", ctx -> {
            ctx.render("index.html");
        });

        // ✅ Handle login form POST
        app.post("/login", ctx -> {
            String email = ctx.formParam("email");
            String password = ctx.formParam("password");

            boolean validUser = UserMapper.validateUser(email, password, connectionPool);

            if (validUser) {
                ctx.sessionAttribute("userEmail", email); // Save user email in session
                ctx.redirect("/dashboard"); // Redirect to dashboard
            } else {
                ctx.attribute("error", "Invalid email or password");
                ctx.render("index.html"); // Reload login page with error
            }
        });

        // ✅ Serve dashboard page after login
        app.get("/dashboard", ctx -> {
            String userEmail = ctx.sessionAttribute("userEmail");

            if (userEmail == null) {
                ctx.redirect("/"); // If not logged in, go back to login
            } else {
                ctx.result("Welcome, " + userEmail + "!"); // You can replace this with a dashboard.html later
            }
        });
    }
}









