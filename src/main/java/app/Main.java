package app;

import app.config.ThymeleafConfig;
import app.controllers.RoutingController;
import app.persistence.ConnectionPool;
import io.javalin.Javalin;
import io.javalin.rendering.template.JavalinThymeleaf;
import app.exceptions.DatabaseException;

import java.util.logging.Logger;

public class Main {
    private static final String USER = "postgres";
    private static final String PASSWORD = "postgres";
    private static final String URL = "jdbc:postgresql://localhost:5432/%s?currentSchema=public";
    private static final String DB = "carport";

    public static final ConnectionPool connectionPool = ConnectionPool.getInstance(USER, PASSWORD, URL, DB);
    public static final RoutingController routingController = new RoutingController(connectionPool);



    public static void main(String[] args) throws DatabaseException {
        Javalin app = Javalin.create(config -> {
            config.staticFiles.add(staticFiles -> {
                staticFiles.hostedPath = "/";
                staticFiles.directory = "public";
                staticFiles.location = io.javalin.http.staticfiles.Location.CLASSPATH;
            });

            config.fileRenderer(new JavalinThymeleaf(ThymeleafConfig.templateEngine()));
        }).start(7070);

        routingController.registerRoutes(app);
    }
}










