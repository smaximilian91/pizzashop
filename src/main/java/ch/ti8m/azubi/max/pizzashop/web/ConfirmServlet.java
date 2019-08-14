package ch.ti8m.azubi.max.pizzashop.web;

import ch.ti8m.azubi.max.pizzashop.template.FreemarkerConfig;
import freemarker.template.Template;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;


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
        URL url = new URL("http://localhost:3000/");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Content-Type", "applications/json");


        /*try {
            template.process(model, writer);
        } catch (TemplateException ex) {
            throw new IOException("Could not process template: " + ex.getMessage());
        }*/
    }
}
