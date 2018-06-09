package src.servlets;

import src.common.controller.TaskList;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class GetTasks extends Dispatcher {
    static public String getName(){
        return "GetTasks";
    }
    public String getServletInfo() {
        return "Get Tasks servlet";
    }

    public void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        ServletContext context = getServletContext();
        TaskList taskList = (TaskList) context.getAttribute("taskList");

        try {
            context.setAttribute("nearestTask", taskList.getNearestTask());

            if (request.getParameter("active") == null) {
                context.setAttribute("tasks", taskList.getTaskList());
                this.forward(Constants.TASK_LIST_PAGE_ADDRESS, request, response);
            } else {
                switch (request.getParameter("active")) {
                    case "active_tasks": {
                        context.setAttribute("tasks", taskList.getTaskList(true));
                        break;
                    }
                    case "not_active_tasks": {
                        context.setAttribute("tasks", taskList.getTaskList(false));
                        break;
                    }
                }
                this.forward(Constants.TASK_LIST_PAGE_ADDRESS, request, response);
            }
        } catch (Exception e) {
            request.setAttribute("edit_error", e.getMessage());
            e.printStackTrace();
            this.forward(Constants.TASK_LIST_PAGE_ADDRESS, request, response);
        }
    }
}
