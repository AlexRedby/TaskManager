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
    public String getServletInfo() {
        return "Add, edit or delete task servlet";
    }

    public void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        ServletContext ctx = getServletContext();
        if (request.getParameter("add") != null) {
            this.forward("/AddTaskPage.jsp", request, response);
        }
        if (request.getParameter("edit") != null) {
            TaskList taskList = (TaskList) ctx.getAttribute("taskList");
            for (Task task : taskList.getTaskList()) {
                if (Integer.toString(task.getId()).equals(request.getParameter("edit"))) {
                    request.setAttribute("oldTask", task);
                }
            }
            this.forward("/AddTaskPage.jsp", request, response);

        }
        if (request.getParameter("del") != null) {
            TaskList taskList = (TaskList) ctx.getAttribute("taskList");
            Client client = (Client) ctx.getAttribute("user");
            for (Task task : taskList.getTaskList()) {
                if (Integer.toString(task.getId()).equals(request.getParameter("del"))) {
                    try {
                        client.deleteTask(task);
                        taskList.deleteTask(task);
                    } catch (Exception e) {
                        //TODO: Обработка
                        e.printStackTrace();
                    }
                    break;
                }
            }
            response.sendRedirect("GetTasks");
        }
        if (request.getParameter("postpone") != null) {
            Client client = (Client) ctx.getAttribute("user");
            TaskList taskList = (TaskList) ctx.getAttribute("taskList");
            Task nearestTask = (Task) ctx.getAttribute("nearestTask");

            Calendar newDate = Calendar.getInstance();
            //Время в минутах, на которое нужно отложить задачу
            int minutes = Integer.parseInt(request.getParameter("postponeValue"));

            //Костыль
            //Продолжение в скрипте TaskListPage
            newDate.add(Calendar.HOUR, 1);

            newDate.add(Calendar.MINUTE, minutes);
            newDate.set(Calendar.SECOND, 0);

            try {
                client.postponeTask(nearestTask, newDate);
                taskList.postpone(nearestTask, newDate);
            } catch (Exception e) {
                //TODO: Обработка
                e.printStackTrace();
            }
            response.sendRedirect("GetTasks");
        }
        if (request.getParameter("complete") != null) {
            Client client = (Client) ctx.getAttribute("user");
            TaskList taskList = (TaskList) ctx.getAttribute("taskList");
            Task nearestTask = (Task) ctx.getAttribute("nearestTask");

            try {
                client.completeTask(nearestTask);
                taskList.complete(nearestTask);
            } catch (Exception e) {
                //TODO: Обработка
                e.printStackTrace();
            }
            response.sendRedirect("GetTasks");
        }

    }
}