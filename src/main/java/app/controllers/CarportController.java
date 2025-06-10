package app.controllers;

import app.entities.Order;
import app.entities.OrderItem;
import app.entities.User;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.OrderMapper;
import app.persistence.UserMapper;
import app.services.Calculator;
import app.services.CarportSvg;
import app.util.GmailEmailSenderHTML;
import io.javalin.http.Context;
import jakarta.mail.MessagingException;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Locale;
import java.util.Map;

public class CarportController {


    public static void carportRequest(Context ctx, ConnectionPool connectionPool) throws DatabaseException {
        Integer width = ctx.sessionAttribute("width");
        Integer length = ctx.sessionAttribute("length");
        Integer userId = ctx.sessionAttribute("users_id");
        int status = 3; // Status 3 = awaiting confirmation

        if (userId == null) {
            ctx.redirect("/login"); // Or render an error page if preferred
            return;
        }
        if (width == null || length == null) {
            ctx.result("Missing carport dimensions in session. Please start your order again.");
            return;
        }

        User user = UserMapper.getUserById(userId, connectionPool);

        Order order = new Order(0, status, width, length, 0, user); //totalprice 0 temporarily

        try {
            order = OrderMapper.insertOrder(order, connectionPool);

            Calculator calculator = new Calculator(width, length, connectionPool);
            calculator.calcCarport(order);

            int totalPrice = calculator.getTotalPrice();
            order.setTotalPrice(totalPrice); // Gemmer i order
            OrderMapper.updateOrderPrice(order.getOrderId(), totalPrice, connectionPool);


            OrderMapper.insertOrderItems(calculator.getOrderItems(), connectionPool);


            ctx.render("confirmation-page.html");
        } catch (DatabaseException e) {
            throw new RuntimeException(e);
        }
    }

    public static void handleSelection(Context ctx, ConnectionPool connectionPool) throws DatabaseException {
        int width = Integer.parseInt(ctx.formParam("width"));
        int length = Integer.parseInt(ctx.formParam("length"));

        ctx.sessionAttribute("width", width);
        ctx.sessionAttribute("length", length);
        showOrder(ctx);

    }


    public static void mailSender(Context ctx, ConnectionPool connectionPool) throws DatabaseException {
        GmailEmailSenderHTML sender = new GmailEmailSenderHTML();

        int userId = ctx.sessionAttribute("users_id");
        String to = ctx.sessionAttribute("email");
        String subject = "Ordrebekræftelse og stykliste på Carport";

        int orderId = Integer.parseInt(ctx.formParam("orderId"));
        List<OrderItem> orderItemList = OrderMapper.getOrderItemByOrderId(orderId, connectionPool);
        Order order = OrderMapper.getCompletedOrders(userId, connectionPool);
        System.out.println(orderItemList.size());
        Map<String, Object> variables = Map.of(
                "title", "Ordrebekræftelse",
                "name", to,
                "message", "Tak for din bestilling. Herunder finder du stykliste og detaljer.",
                "order", order,
                "orderItems", orderItemList
        );

        String html = sender.renderTemplate("confirmation-email.html", variables);

        try {
            sender.sendHtmlEmail(to, subject, html);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }



    public static void mailSenderRequest(Context ctx, String to) {
        GmailEmailSenderHTML sender = new GmailEmailSenderHTML();
        String subject = "Forespørgsel på Carport";
        int length = ctx.sessionAttribute("length");
        int width = ctx.sessionAttribute("width");
        Map<String, Object> variables = Map.of(
                "title", "Din Carport-forespørgsel",
                "name", to,
                "message", "Her er en opsummering af din forespørgsel.",
                "length", length,
                "width", width,
                "payLink", "http://codeforschool.dk/checkout",
                "editLink", "http://codeforschool.dk/edit"
        );
        String html = sender.renderTemplate("email.html", variables);
        try {
            sender.sendHtmlEmail(to, subject, html);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    // Keep the old method for backwards compatibility, but delegate to the new one
    public static void mailSenderRequest(Context ctx) {
        String to = ctx.sessionAttribute("email");
        mailSenderRequest(ctx, to);
    }


    public static void showOrder(Context ctx) {

        int length = ctx.sessionAttribute("length");
        int width = ctx.sessionAttribute("width");
        CarportSvg svg = new CarportSvg(width, length);
        Locale.setDefault(new Locale("US"));
        ctx.attribute("length", length);
        ctx.attribute("width", width);
        ctx.attribute("svg", svg.toString());

        ctx.render("showOrder.html");
    }



    public static void checkoutHandler(Context ctx, ConnectionPool connectionPool) throws DatabaseException {
        Integer userId = ctx.sessionAttribute("users_id");

        if (userId == null) {
            ctx.redirect("/login");
            return;
        }

        List<Order> pendingOrders = OrderMapper.getPendingPaymentOrders(userId, connectionPool);

        if (pendingOrders.isEmpty()) {
            ctx.attribute("message", "Du har ingen ubetalte ordrer.");
        } else {
            ctx.attribute("orders", pendingOrders);
        }
        ctx.render("checkout.html");
    }

    public static void paymentHandler(Context ctx, ConnectionPool connectionPool) throws DatabaseException {
        Integer userId = ctx.sessionAttribute("users_id");
        if (userId == null) {
            ctx.redirect("/login");
            return;
        }

        int orderId = Integer.parseInt(ctx.formParam("orderId"));
        Order order = OrderMapper.getOrderById(orderId, connectionPool);
        ctx.attribute("order", order);


        ctx.render("payment.html");
    }

    public static void finalizePayment(Context ctx, ConnectionPool connectionPool) throws DatabaseException {
        Integer userId = ctx.sessionAttribute("users_id");
        if (userId == null) {
            ctx.redirect("/login");
            return;
        }

        int orderId = Integer.parseInt(ctx.formParam("orderId"));

        // Opdater status til 2 = betalt
        OrderMapper.updateOrderStatus(orderId, connectionPool);
        mailSender(ctx,connectionPool);
        ctx.render("payment-confirmation.html");
    }
}
