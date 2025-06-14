package app.controllers;

import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.OrderMapper;
import app.entities.Order;
import app.entities.User;
import app.persistence.UserMapper;
import io.javalin.http.Context;
import java.util.List;

public class AdminController {

    public static void showAdminPage(Context ctx, ConnectionPool connectionPool) throws DatabaseException {
        String email = ctx.sessionAttribute("email");
        List<Order> pending = OrderMapper.getAllPendingOrders(connectionPool);
        //ctx.attribute("email", email);
        ctx.attribute("pending", pending);
        ctx.render("admin/admin-homepage.html");
    }

    public static void sendMailToUser(Context ctx, ConnectionPool connectionPool) throws DatabaseException {
        int orderId = Integer.parseInt(ctx.formParam("orderId"));

        Order order = OrderMapper.getOrderByIdWithUser(orderId, connectionPool);
        if (order == null) {
            throw new DatabaseException("Ordre ikke fundet.");
        }

        User user = order.getUser();
        if (user == null) {
            throw new DatabaseException("Bruger tilknyttet ordren blev ikke hentet.");
        }

        String email = user.getEmail();
        CarportController.mailSenderRequest(ctx, email);
        OrderMapper.updateOrderStatus(orderId, 1, connectionPool); // Status 1 = waiting payment
        ctx.redirect("/admin");
    }

    public static void cancelOrder(Context ctx, ConnectionPool connectionPool) throws DatabaseException {
        int orderId = Integer.parseInt(ctx.formParam("orderId"));
        OrderMapper.deleteOrderItemsByOrderId(orderId, connectionPool);
        OrderMapper.deleteOrderById(orderId, connectionPool);
        ctx.redirect("/admin");
    }

    public static void showCustomers(Context ctx, ConnectionPool connectionPool) throws DatabaseException {
        List<User> users = UserMapper.getAllUsers(connectionPool);
        ctx.attribute("users", users);
        ctx.render("admin/customers.html");
    }

    public static void showContactCustomer(Context ctx, ConnectionPool connectionPool) throws DatabaseException {
        int userId = Integer.parseInt(ctx.pathParam("userId"));
        User user = UserMapper.getUserById(userId, connectionPool);
        ctx.attribute("user", user);
        ctx.render("admin/contact-customer.html");
    }
}
