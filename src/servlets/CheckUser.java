package src.servlets;

import src.client.Client;
import src.common.controller.TaskList;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;

public class CheckUser extends Dispatcher {
    static public String getName(){
        return "CheckUser";
    }
    public String getServletInfo() {
        return "Registration servlet";
    }

    public void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        ServletContext context = getServletContext();
        boolean newUser = true;
        if (request.getParameter("log_in") != null) {
            newUser = false;
            request.setAttribute("newUser", false);
        }
        if (request.getParameter("register") != null) {
            newUser = true;
            request.setAttribute("newUser", true);
        }

        try {
            Client client = new Client(request.getParameter("login"), request.getParameter("password"), newUser);
            context.setAttribute("user", client);
            context.setAttribute("taskList", new TaskList(client.getAllTasks()));

            response.sendRedirect(GetTasks.getName());

        } catch (Exception e) {
            request.setAttribute("checkUserError", e.getMessage());
            this.forward(Constants.LOGIN_PAGE_ADDRESS, request, response);
        }
    }

}
