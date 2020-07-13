package ch.ti8m.azubi.max.pizzashop.web;

import ch.ti8m.azubi.max.pizzashop.dto.Pizza;
import ch.ti8m.azubi.max.pizzashop.service.PizzaService;
import ch.ti8m.azubi.max.pizzashop.service.ServiceRegistry;
import ch.ti8m.azubi.max.pizzashop.template.FreemarkerConfig;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/pizza")
public class PizzaServlet extends HttpServlet {

    private PizzaService pizzaService;
    private Template template;

    @Override
    public void init() throws ServletException {
        pizzaService = ServiceRegistry.getInstance().get(PizzaService.class);
        template = new FreemarkerConfig().loadTemplate("pizza.ftl");
    }

    /**
     * Get the Pizza HTML page with a list of all pizzas.
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        List<Pizza> pizzas;
        try {
            pizzas = pizzaService.list();
        } catch (Exception ex) {
            throw new IOException("Failed to load pizza list", ex);
        }

        PrintWriter writer = resp.getWriter();
        Map<String, Object> model = new HashMap<>();
        model.put("pizzas", pizzas);

        try {
            template.process(model, writer);
        } catch (TemplateException ex) {
            throw new IOException("Could not process template: " + ex.getMessage());
        }
    }
}
