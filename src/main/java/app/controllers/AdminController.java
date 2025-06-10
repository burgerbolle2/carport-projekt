package app.controllers;

import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.OrderMapper;
import app.entities.Order;
import io.javalin.http.Context;
import java.sql.Connection;
import java.util.List;

public class AdminController {

    public static void showAdminPage(Context ctx, ConnectionPool connectionPool) throws DatabaseException {
        String email = ctx.sessionAttribute("email");
        List<Order> pending = OrderMapper.getAllPendingOrders(connectionPool);
        //ctx.attribute("email", email);
        ctx.attribute("pending", pending);
        ctx.render("/admin/admin-homepage.html");
    }


}
