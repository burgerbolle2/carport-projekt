package app.controllers;

import app.persistence.ConnectionPool;
import app.util.GmailEmailSenderHTML;
import io.javalin.http.Context;
import jakarta.mail.MessagingException;

import java.util.Map;

public class CarportController {


    private static void carportRequest(Context ctx, ConnectionPool connectionPool){
        int width = ctx.sessionAttribute("width");
        int length = ctx.sessionAttribute("length");
        int status = 1; //Hardcoded must be changed later
        int totalPrice = 199999; //this too

    }

    public static void handleSelection(Context ctx, ConnectionPool connectionPool){
        int width = ctx.sessionAttribute("width");
        int length = ctx.sessionAttribute("length");
    }

    public static void mailSender(Context ctx, ConnectionPool connectionPool){
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
}
