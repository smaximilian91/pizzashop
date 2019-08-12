package ch.ti8m.azubi.max.pizzashop.web;

import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

@WebServlet("/welcome")
public class WelcomeServlet extends HttpServlet {

    /**
     * Get the Pizza HTML page, with a list of notes and a form to submit new notes.
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        InputStream in = getClass().getResourceAsStream("/templates/welcome.ftl");
        ServletOutputStream out = resp.getOutputStream();

        byte[] buffer = new byte[0xFFF];
        int read;
        while ((read = in.read(buffer)) >= 0) {
            out.write(buffer, 0, read);
        }
        out.flush();
    }
}
