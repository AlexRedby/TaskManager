package src.servlets;


import src.client.Client;
import src.common.controller.TaskList;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;


public class CheckUser extends Dispatcher {
    public String getServletInfo() {
        return "Registration servlet";
    }

    public void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        ServletContext ctx = getServletContext();
        boolean newUser = true;
        if (request.getParameter("log_in") != null) {
            newUser = false;
            ctx.setAttribute("newUser", newUser);
        }
        if (request.getParameter("register") != null){
            newUser = true;
            ctx.setAttribute("newUser", newUser);
        }

        try {
            Client client = new Client(request.getParameter("login"), request.getParameter("password"), newUser);
            ctx.setAttribute("user", client);
            ctx.setAttribute("taskList", new TaskList(client.getAllTasks()));
            this.forward("/GetTasks", request, response);
        } catch (Exception e) {
            ctx.setAttribute("error", e.getMessage());
            this.forward("/index.jsp", request, response);
        }
    }

}
