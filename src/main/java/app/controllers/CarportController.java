// File: src/main/java/app/controllers/CarportController.java
package app.controllers;

import app.exceptions.DatabaseException;
import app.entities.Product;
import app.service.ProductService;
import io.javalin.http.Context;

import javax.sql.DataSource;
import java.util.Collections;
import java.util.List;

public class CarportController {
    private final ProductService productService;
    private final DataSource ds;

    public CarportController(ProductService productService, DataSource ds) {
        this.productService = productService;
        this.ds             = ds;
    }

    // GET /find-carport
    public void showForm(Context ctx) {
        ctx.attribute("carports",  Collections.emptyList());
        ctx.attribute("minWidth",  0.0);
        ctx.attribute("minLength", 0.0);
        ctx.render("find-carport.html");
    }

    // POST /find-carport
    public void handleSearch(Context ctx) {
        double minWidth  = ctx.formParamAsClass("minWidth",  Double.class).getOrDefault(0.0);
        double minLength = ctx.formParamAsClass("minLength", Double.class).getOrDefault(0.0);

        List<Product> results;
        String message = null;

        try {
            // 1) Forsøg at finde carporte ≥ input-dimensioner
            results = productService.searchCarports(minWidth, minLength, ds);

            if (results.isEmpty()) {
                // 2) Ingen match → hent *alle* og tag den mindste
                List<Product> all = productService.searchCarports(0.0, 0.0, ds);
                if (!all.isEmpty()) {
                    Product fallback = all.get(0); // forudsætter at mapper’en returnerer sorteret liste
                    results = List.of(fallback);
                    message = "Ingen carporte matcher de angivne mål; viser i stedet mindste carport.";
                }
            }
        } catch (DatabaseException e) {
            message = "Fejl ved søgning: " + e.getMessage();
            results = Collections.emptyList();
        }

        // 3) Giv Thymeleaf både listen og evt. besked
        ctx.attribute("carports",   results);
        ctx.attribute("minWidth",   minWidth);
        ctx.attribute("minLength",  minLength);
        if (message != null) {
            ctx.attribute("message", message);
        }
        ctx.render("find-carport.html");
    }
}



