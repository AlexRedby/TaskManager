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
        ServletContext ctx = getServletContext();
        Client client = (Client) ctx.getAttribute("user");
        client.close();
        ctx.removeAttribute("user");
        forward("/index.jsp", request, response);
    }
}