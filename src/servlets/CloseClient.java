package src.servlets;

import src.client.Client;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;

public class CloseClient extends Dispatcher {
    static public String getName(){
        return "CloseClient";
    }
    public String getServletInfo() {
        return "Close client servlet";
    }

    public void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            ServletContext context = getServletContext();
            Client client = (Client) context.getAttribute("user");
            client.close();
            context.removeAttribute("user");
            context.removeAttribute("taskList");
            context.removeAttribute("tasks");
            forward(Constants.LOGIN_PAGE_ADDRESS, request, response);
        } catch (Exception e) {
            request.setAttribute("ServerError", "Соединение с сервером прервано");
            forward(Constants.LOGIN_PAGE_ADDRESS, request, response);
        }
    }
}