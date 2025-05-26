package app.controllers;

import app.persistence.ConnectionPool;
import io.javalin.Javalin;

public class RoutingController {
    private final ConnectionPool connectionPool;
    private final HomeController homeController;
    private final CarportController carportController;

    public RoutingController(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
        this.homeController = new HomeController(connectionPool);
        this.carportController = new CarportController();
    }

    public void registerRoutes(Javalin app) {
        // Hjem + login/registrer
        app.get("/", ctx -> homeController.home(ctx));
        app.get("/login", ctx -> ctx.render("find-carport.html"));
        app.post("/login", ctx -> homeController.handleLogin(ctx, connectionPool));
        app.get("/register", ctx -> ctx.render("register.html"));
        app.post("/register", ctx -> homeController.handleCreateUser(ctx, connectionPool));

        // Find-carport
        app.get("/find-carport", ctx -> ctx.render("find-carport.html"));
        app.post("/find-carport", ctx -> carportController.handleSelection(ctx,connectionPool));

    }
}



