package src.servlets;

import src.client.Client;
import src.common.controller.TaskList;
import src.common.model.Task;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class SaveTask extends Dispatcher {
    static public String getName(){
        return "SaveTask";
    }
    public String getServletInfo() {
        return "Save Task servlet";
    }

    public void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //Поддержка русских букв
        request.setCharacterEncoding("UTF-8");
        ServletContext context = getServletContext();
        TaskList taskList = (TaskList) context.getAttribute("taskList");
        String name = request.getParameter("name");
        String info = request.getParameter("info");
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
        formatter.setLenient(false);
        try {
            cal.setTime(formatter.parse(request.getParameter("date")));
        } catch (Exception e) {
            request.setAttribute("edit_error", e.getMessage());
            e.printStackTrace();
            response.sendRedirect(GetTasks.getName());
        }
        String contacts = request.getParameter("contacts");
        boolean active = false;
        if (request.getParameter("active").equals("active")) {
            active = true;
        }

        Task task = new Task(name, info, cal, contacts, active);
        Client client = (Client) context.getAttribute("user");
        try {
            if (request.getParameter("save").equals("")) {
                client.addTask(task);
                taskList.addTask(task);
            } else {
                task.setId(Integer.parseInt(request.getParameter("save")));
                client.updateTask(task);
                taskList.updateTask(task);
            }

        } catch (Exception e) {
            request.setAttribute("edit_error", e.getMessage());
            e.printStackTrace();
            this.forward(Constants.ADD_TASK_PAGE_ADDRESS, request, response);
        }
        response.sendRedirect(GetTasks.getName());
    }
}
