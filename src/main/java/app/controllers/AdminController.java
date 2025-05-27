package app.controllers;

import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import io.javalin.http.Context;

public class AdminController {

    public static void showAdminPage(Context ctx, ConnectionPool connectionPool) throws DatabaseException {
        String email = ctx.sessionAttribute("email");
        ctx.attribute("email", email);
        ctx.render("/admin/admin-homepage.html");
    }
}
