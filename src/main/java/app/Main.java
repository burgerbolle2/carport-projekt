package app;

import app.config.ThymeleafConfig;
import app.persistence.ConnectionPool;
import app.persistence.UserMapper;
import io.javalin.Javalin;
import io.javalin.rendering.template.JavalinThymeleaf;
import io.javalin.http.staticfiles.Location;
import app.exceptions.DatabaseException;

import javax.sql.DataSource;
import java.util.logging.Logger;

public class Main {
    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

    private static final String USER = "postgres";
    private static final String PASSWORD = "postgres";
    private static final String URL = "jdbc:postgresql://localhost:5432/%s?currentSchema=public";
    private static final String DB = "FOG";

    public static final ConnectionPool connectionPool = ConnectionPool.getInstance(USER, PASSWORD, URL, DB);


    public static void main(String[] args) throws DatabaseException {
        Javalin app = Javalin.create(config -> {
            config.staticFiles.add(staticFiles -> {
                staticFiles.hostedPath = "/";
                staticFiles.directory = "public";
                staticFiles.location = io.javalin.http.staticfiles.Location.CLASSPATH;
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

            boolean validUser = UserMapper.login(email, password, connectionPool);

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

        app.get("/register", ctx -> {
            ctx.render("register.html");
        });

        app.post("/register", ctx -> {
            String email = ctx.formParam("email");
            String password = ctx.formParam("password");

            try {
                UserMapper.createUser(email, password, connectionPool);
                ctx.redirect("/"); // Gå til login-siden
            } catch (DatabaseException e) {
                ctx.attribute("error", "Kunne ikke oprette konto: " + e.getMessage());
                ctx.render("register.html");
            }
        });


    }
}










