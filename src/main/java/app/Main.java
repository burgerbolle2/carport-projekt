package app;

import app.config.ThymeleafConfig;
import app.controllers.RoutingController;
import app.persistence.ConnectionPool;
import io.javalin.Javalin;
import io.javalin.rendering.template.JavalinThymeleaf;
import app.exceptions.DatabaseException;


public class Main {

// Uncomment the following lines to use environment variables for configuration
    private static final String USER = "postgres";
    private static final String PASSWORD = System.getenv("password");
    private static final String URL = "jdbc:postgresql://" +System.getenv("ip") + ":5432/%s?currentSchema=public";
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










