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

        //Admin routes
        app.get("/admin",ctx -> AdminController.showAdminPage(ctx, connectionPool));
        app.post("/admin/send-mail", ctx -> AdminController.sendMailToUser(ctx, connectionPool));
        app.post("/admin/cancel-order", ctx -> AdminController.cancelOrder(ctx, connectionPool));
        app.get("/admin/customers", ctx -> AdminController.showCustomers(ctx, connectionPool));
        app.get("/admin/contact-customer/{userId}", ctx -> AdminController.showContactCustomer(ctx, connectionPool));


        // Carport
        app.get("/find-carport", ctx -> ctx.render("find-carport.html"));
        app.post("/find-carport", ctx -> carportController.handleSelection(ctx, connectionPool));
        app.get("/forespoergsel", ctx -> ctx.render("confirmation-page.html"));
        app.post("/forespoergsel", ctx -> carportController.carportRequest(ctx, connectionPool));

        // Payment and checkout
        app.get("/checkout", ctx -> carportController.checkoutHandler(ctx,connectionPool));
        app.post("/payment",ctx -> carportController.paymentHandler(ctx,connectionPool));
        app.post("/confirmation",ctx -> carportController.finalizePayment(ctx,connectionPool));


    }
}
