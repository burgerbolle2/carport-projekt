package app;

import app.config.ThymeleafConfig;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.UserMapper;
import app.persistence.ProductMapper;
import app.service.ProductService;
import io.javalin.Javalin;
import io.javalin.rendering.template.JavalinThymeleaf;
import io.javalin.http.staticfiles.Location;

import javax.sql.DataSource;
import java.util.Collections;

public class Main {
    public static void main(String[] args) {

        DataSource connectionPool = ConnectionPool.getInstance(
                "postgres",
                "postgres",
                "jdbc:postgresql://localhost:5432/%s?currentSchema=public",
                "FOG"
        );


        Javalin app = Javalin.create(config -> {
            config.staticFiles.add(staticFiles -> {
                staticFiles.hostedPath = "/";
                staticFiles.directory = "public";
                staticFiles.location   = Location.CLASSPATH;
            });
            config.fileRenderer(new JavalinThymeleaf(ThymeleafConfig.templateEngine()));
        }).start(7070);



        ProductService carportService = new ProductService(new ProductMapper());


        app.get("/", ctx -> ctx.render("index.html"));



        app.post("/login", ctx -> {
            String email    = ctx.formParam("email");
            String password = ctx.formParam("password");

            boolean validUser = UserMapper.validateUser(email, password, connectionPool);
            if (validUser) {
                ctx.sessionAttribute("userEmail", email);
                ctx.redirect("/find-carport");
            } else {
                ctx.attribute("error", "Invalid email eller password");
                ctx.render("index.html");
            }
        });



        app.get("/dashboard", ctx -> {
            String userEmail = ctx.sessionAttribute("userEmail");
            if (userEmail == null) {
                ctx.redirect("/");
            } else {
                ctx.result("Welcome, " + userEmail + "!");
            }
        });


        app.get("/register", ctx -> ctx.render("register.html"));

        app.post("/register", ctx -> {
            String email    = ctx.formParam("email");
            String password = ctx.formParam("password");
            try {
                UserMapper.createUser(email, password, connectionPool);
                ctx.redirect("/");
            } catch (DatabaseException e) {
                ctx.attribute("error", "Kunne ikke oprette konto: " + e.getMessage());
                ctx.render("register.html");
            }
        });


        app.get("/find-carport", ctx -> {
            ctx.attribute("carports", Collections.emptyList());
            ctx.render("find-carport.html");
        });

        app.post("/find-carport", ctx -> {

            double minWidth  = ctx.formParamAsClass("minWidth", Double.class).getOrDefault(0.0);
            double minLength = ctx.formParamAsClass("minLength", Double.class).getOrDefault(0.0);

            try {
                var results = carportService.searchCarports(minWidth, minLength, connectionPool);
                ctx.attribute("carports", results);
            } catch (DatabaseException e) {
                ctx.attribute("error", "Fejl ved søgning: " + e.getMessage());
                ctx.attribute("carports", Collections.emptyList());
            }

            ctx.attribute("minWidth",  minWidth);
            ctx.attribute("minLength", minLength);
            ctx.render("find-carport.html");
        });
    }
}













