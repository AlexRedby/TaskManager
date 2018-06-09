package src.servlets;

import src.client.Client;
import src.common.controller.TaskList;
import src.common.model.Task;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Calendar;

public class EditTask extends Dispatcher {
    static public String getName(){
        return "EditTask";
    }
    public String getServletInfo() {
        return "Add, edit or delete task servlet";
    }

    public void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        ServletContext context = getServletContext();
        if (request.getParameter("add_task") != null) {
            this.forward(Constants.ADD_TASK_PAGE_ADDRESS, request, response);
        }
        if (request.getParameter("edit_task") != null) {
            TaskList taskList = (TaskList) context.getAttribute("taskList");
            for (Task task : taskList.getTaskList()) {
                if (Integer.toString(task.getId()).equals(request.getParameter("edit_task"))) {
                    request.setAttribute("oldTask", task);
                    break;
                }
            }
            this.forward(Constants.ADD_TASK_PAGE_ADDRESS, request, response);
        }
        if (request.getParameter("delete_task") != null) {
            String parameter = request.getParameter("delete_task");
            String[] ids = parameter.split(",");

            TaskList taskList = (TaskList) context.getAttribute("taskList");
            Client client = (Client) context.getAttribute("user");
            for (String id: ids) {
                for (Task task : taskList.getTaskList()) {
                    if (Integer.toString(task.getId()).equals(id)){
                        try {
                            client.deleteTask(task);
                            taskList.deleteTask(task);
                        } catch (Exception e) {
                            request.setAttribute("edit_error", e.getMessage());
                            e.printStackTrace();
                            this.forward(Constants.TASK_LIST_PAGE_ADDRESS, request, response);
                        }
                        break;
                    }
                }
            }
            response.sendRedirect(GetTasks.getName());
        }
        if (request.getParameter("postpone") != null) {
            Client client = (Client) context.getAttribute("user");
            TaskList taskList = (TaskList) context.getAttribute("taskList");
            Task nearestTask = (Task) context.getAttribute("nearestTask");

            Calendar newDate = Calendar.getInstance();
            //Время в минутах, на которое нужно отложить задачу
            int minutes = Integer.parseInt(request.getParameter("postponeValue"));

            newDate.add(Calendar.MINUTE, minutes);
            newDate.set(Calendar.SECOND, 0);

            try {
                client.postponeTask(nearestTask, newDate);
                taskList.postpone(nearestTask, newDate);
            } catch (Exception e) {
                request.setAttribute("edit_error", e.getMessage());
                e.printStackTrace();
                this.forward(Constants.TASK_LIST_PAGE_ADDRESS, request, response);
            }
            response.sendRedirect(GetTasks.getName());
        }
        if (request.getParameter("complete") != null) {
            Client client = (Client) context.getAttribute("user");
            TaskList taskList = (TaskList) context.getAttribute("taskList");
            Task nearestTask = (Task) context.getAttribute("nearestTask");

            try {
                client.completeTask(nearestTask);
                taskList.complete(nearestTask);
            } catch (Exception e) {
                request.setAttribute("edit_error", e.getMessage());
                e.printStackTrace();
                this.forward(Constants.TASK_LIST_PAGE_ADDRESS, request, response);
            }
            response.sendRedirect(GetTasks.getName());
        }

    }
}