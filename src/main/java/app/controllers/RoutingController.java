package app.controllers;

import app.persistence.ConnectionPool;
import io.javalin.Javalin;

public class RoutingController {
    private final ConnectionPool connectionPool;
    private final HomeController homeController;

    public RoutingController(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
        this.homeController = new HomeController(connectionPool);
    }

    public void registerRoutes(Javalin app) {
        app.get("/", ctx -> homeController.home(ctx));
        app.get("/login", ctx -> ctx.render("login.html"));
        app.post("/login", ctx -> homeController.handleLogin(ctx, connectionPool));
        app.get("/create-user", ctx -> ctx.render("create-user.html"));
        app.post("/create-user", ctx -> homeController.handleCreateUser(ctx, connectionPool));
    }
}
