package src.servlets;

import src.client.Client;
import src.common.controller.TaskList;
import src.common.model.Task;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class EditTask extends Dispatcher {
    public String getServletInfo() {
        return "Add, edit oe delete task servlet";
    }
    public void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        ServletContext ctx = getServletContext();
        if (request.getParameter("add") != null) {
            this.forward("/AddTaskPage.jsp", request, response);
        }
        if (request.getParameter("edit") != null){
            TaskList taskList = (TaskList) ctx.getAttribute("taskList");
            for (Task task : taskList.getTaskList()){
                if (Integer.toString(task.getId()).equals(request.getParameter("edit"))){
                    request.setAttribute("oldTask", task);
                }
            }
            this.forward("/AddTaskPage.jsp", request, response);

        }
        if (request.getParameter("del") != null){
            TaskList taskList = (TaskList) ctx.getAttribute("taskList");
            Client client = (Client) ctx.getAttribute("user");
            for (Task task : taskList.getTaskList()){
                if (Integer.toString(task.getId()).equals(request.getParameter("del"))){
                    try {
                        client.deleteTask(task);
                        taskList.deleteTask(task);
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
            response.sendRedirect("GetTasks");
        }

    }
}