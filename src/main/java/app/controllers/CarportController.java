package app.controllers;

import app.entities.Order;
import app.entities.User;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.OrderMapper;
import app.services.Calculator;
import app.services.CarportSvg;
import app.util.GmailEmailSenderHTML;
import io.javalin.http.Context;
import jakarta.mail.MessagingException;

import java.util.Locale;
import java.util.Map;

public class CarportController {


    private static void carportRequest(Context ctx, ConnectionPool connectionPool) {
        int width = ctx.sessionAttribute("width");
        int length = ctx.sessionAttribute("length");
        int status = 1; //Hardcoded must be changed later
        int totalPrice = 199999; //this too
        User user = new User(1, "Rasmussenm0@gmail.com", "dad", "user");

        Order order = new Order(0, status, width, length, totalPrice, user);

        try {
            order = OrderMapper.insertOrder(order, connectionPool);

            Calculator calculator = new Calculator(width,length,order,connectionPool);

            ctx.render("confirmationPage.html");
        } catch (DatabaseException e) {
            throw new RuntimeException(e);
        }

    }

    public static void handleSelection(Context ctx, ConnectionPool connectionPool) {
        int width = Integer.parseInt(ctx.formParam("width"));
        int length = Integer.parseInt(ctx.formParam("length"));

        ctx.sessionAttribute("width", width);
        ctx.sessionAttribute("length", length);
        showOrder(ctx);
    }

    public static void mailSender(Context ctx, ConnectionPool connectionPool) {
        GmailEmailSenderHTML sender = new GmailEmailSenderHTML();

        String to = ctx.sessionAttribute("email");   // Erstat med din modtager
        String subject = "Forespørgsel på Carport";

        // Opret en Thymeleaf kontekst med variabler. Tilføj dine egne værdier.
        Map<String, Object> variables = Map.of(
                "title", "Velkommen!",
                "name", to,
                "message", "Her har du din forespørgsel, her finder du også prisen på tilbuddet."
        );

        String html = sender.renderTemplate("email.html", variables); // bruger templates/email.html

        try {
            sender.sendHtmlEmail(to, subject, html);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public static void showOrder(Context ctx) {

        int length = ctx.sessionAttribute("length");
        int width = ctx.sessionAttribute("width");
        CarportSvg svg = new CarportSvg(width, length);
        Locale.setDefault(new Locale("US"));
        ctx.attribute("svg", svg.toString());
        ctx.render("showOrder.html");
    }

}
