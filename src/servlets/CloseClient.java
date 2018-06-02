package src.servlets;

import src.client.Client;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;

public class CloseClient extends Dispatcher {
    public String getServletInfo() {
        return "Close client servlet";
    }

    public void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            ServletContext ctx = getServletContext();
            Client client = (Client) ctx.getAttribute("user");
            client.close();
            ctx.removeAttribute("user");
            ctx.removeAttribute("taskList");
            ctx.removeAttribute("tasks");
            forward("/index.jsp", request, response);
        } catch (Exception e) {
            request.setAttribute("error", "Соединение с сервером прервано");
            forward("/index.jsp", request, response);
        }
    }
}