package app.controllers;

import app.controllers.HomeController;
import app.controllers.CarportController;
import app.persistence.ConnectionPool;
import app.persistence.ProductMapper;
import app.service.ProductService;
import io.javalin.Javalin;

public class RoutingController {
    private final HomeController    homeController;
    private final CarportController carportController;

    public RoutingController(ConnectionPool pool) {
        this.homeController    = new HomeController(pool);
        this.carportController = new CarportController(
                new ProductService(new ProductMapper()),
                pool.getDataSource()    // Husk at ConnectionPool har en public DataSource getDataSource()
        );
    }

    public void registerRoutes(Javalin app) {
        // Hjem + login/registrer
        app.get("/",               homeController::home);
        app.get("/login",          ctx -> ctx.render("index.html"));
        app.post("/login",         homeController::handleLogin);
        app.get("/register",       ctx -> ctx.render("register.html"));
        app.post("/register",      homeController::handleCreateUser);

        // Find-carport
        app.get("/find-carport",   carportController::showForm);
        app.post("/find-carport",  carportController::handleSearch);
    }
}



