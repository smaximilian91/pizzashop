package ch.ti8m.azubi.max.pizzashop.web;

import ch.ti8m.azubi.max.pizzashop.dto.Order;
import ch.ti8m.azubi.max.pizzashop.dto.PizzaOrder;
import ch.ti8m.azubi.max.pizzashop.template.FreemarkerConfig;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/confirm")
public class ConfirmServlet extends HttpServlet {

    private Template template;
    private Logger logger = LoggerFactory.getLogger(getClass().getName());

    @Override
    public void init() throws ServletException {
        template = new FreemarkerConfig().loadTemplate("confirm.ftl");
    }

    /**
     * Get the Pizza HTML page, with a list of notes and a form to submit new notes.
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        HttpSession session = req.getSession();
        Order order = (Order) session.getAttribute("order");
        List<PizzaOrder> pizzaOrders = order.getPizzaOrders();


        PrintWriter writer = resp.getWriter();
        Map<String, Object> model = new HashMap<>();
        model.put("pizzaOrders", pizzaOrders);
        model.put("order", order);

        try {
            template.process(model, writer);
        } catch (TemplateException ex) {
            throw new IOException("Could not process template: " + ex.getMessage());
        }
    }
}
