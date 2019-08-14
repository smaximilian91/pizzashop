package ch.ti8m.azubi.max.pizzashop.web;

import ch.ti8m.azubi.max.pizzashop.dto.Order;
import ch.ti8m.azubi.max.pizzashop.dto.Pizza;
import ch.ti8m.azubi.max.pizzashop.dto.PizzaOrder;
import ch.ti8m.azubi.max.pizzashop.service.OrderService;
import ch.ti8m.azubi.max.pizzashop.service.PizzaService;
import ch.ti8m.azubi.max.pizzashop.service.ServiceRegistry;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/order")
public class OrderServlet extends HttpServlet {

    private Logger logger = LoggerFactory.getLogger(getClass().getName());

    private PizzaService pizzaService;
    private OrderService orderService;
    private Template template;

    @Override
    public void init() throws ServletException {
        pizzaService = ServiceRegistry.getInstance().get(PizzaService.class);
        orderService = ServiceRegistry.getInstance().get(OrderService.class);
        template = new FreemarkerConfig().loadTemplate("order.ftl");
    }

    /**
     * Get the Pizza HTML page, with a list of notes and a form to submit new notes.
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        List<Pizza> pizzas = null;
        try {
            pizzas = pizzaService.list();
        } catch (Exception e) {
            e.printStackTrace();
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

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        try {


            String address = req.getParameter("address");
            String phone = req.getParameter("phone");

            logger.info("Creating order");
            logger.info("Address: " + address);
            logger.info("Phone: " + phone);

            if (!address.isEmpty() && !phone.isEmpty() && address != null && phone != null) {
                boolean anyAmountIsNotNull = false;
                List<PizzaOrder> pizzaOrderList = new ArrayList<>();
                List<Pizza> pizzas = pizzaService.list();
                for (int i = 0; i < pizzas.size(); i++) {

                    String parameterName = "anzahl-" + pizzas.get(i).getId();
                    String parameterValue = req.getParameter(parameterName);
                    logger.info(parameterName + " = " + parameterValue);
                    if (parameterValue != null) {
                        int amount = Integer.parseInt(parameterValue);
                        if (amount != 0) {
                            anyAmountIsNotNull = true;
                            PizzaOrder pizzaOrder = new PizzaOrder(amount, pizzas.get(i));
                            pizzaOrderList.add(pizzaOrder);
                        }
                    }
                }
                if (anyAmountIsNotNull) {
                    logger.info("Creating Order in Database");
                    Order order = new Order(address, phone, pizzaOrderList);
                    logger.info(order.toString());
                    orderService.create(order);
                    HttpSession session = req.getSession();
                    session.setAttribute("order", order);
                    resp.sendRedirect("confirm");
                }
            } else {
                resp.sendRedirect("order");
            }
        } catch (Exception ex) {
            throw new IOException("Request failed", ex);
        }
    }
}
