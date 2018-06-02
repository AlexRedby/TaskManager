package src.servlets;

import src.common.controller.TaskList;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class GetTasks extends Dispatcher {
    public String getServletInfo() {
        return "Get Tasks servlet";
    }

    public void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        ServletContext ctx = getServletContext();
        TaskList taskList = (TaskList) ctx.getAttribute("taskList");

        try {
            ctx.setAttribute("nearestTask", taskList.getNearestTask());

            if (request.getParameter("active") == null) {
                ctx.setAttribute("tasks", taskList.getTaskList());
                this.forward("/TaskListPage.jsp", request, response);
            } else {
                switch (request.getParameter("active")) {
                    case "a": {
                        ctx.setAttribute("tasks", taskList.getTaskList(true));
                        break;
                    }
                    case "na": {
                        ctx.setAttribute("tasks", taskList.getTaskList(false));
                        break;
                    }
                }
                this.forward("/TaskListPage.jsp", request, response);
            }
        } catch (Exception e) {
            request.setAttribute("edit_error", e.getMessage());
            e.printStackTrace();
            this.forward("/TaskListPage.jsp", request, response);
        }
    }
}
