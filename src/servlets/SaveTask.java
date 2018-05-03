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

    public String getServletInfo() {
        return "Save Task servlet";
    }

    public void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        ServletContext ctx = getServletContext();
        TaskList taskList = (TaskList) ctx.getAttribute("taskList");
        String name = request.getParameter("name");
        String info = request.getParameter("info");
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
        formatter.setLenient(false);
        try {
            cal.setTime(formatter.parse(request.getParameter("date")));
        }
        catch (Exception e){
            e.printStackTrace();
        }
        String contacts = request.getParameter("contacts");
        boolean active = false;
        if (request.getParameter("active").equals("a")){
            active = true;
        }
        // todo: Подумать над синхронизацией ID
        Task task = new Task( 0, name, info, cal, contacts, active);
        Client client = (Client) ctx.getAttribute("user");
        try {
            String s =  request.getParameter("save");
            if ( request.getParameter("save").equals("")){
                client.addTask(task);
                taskList.addTask(task);
            }
            else {
                task.setId(Integer.parseInt(request.getParameter("save")));
                client.updateTask(task);
                taskList.updateTask(task);

            }

        }
        catch (Exception e){
            //todo: Сделать обработку исключений
            e.printStackTrace();
        }
        response.sendRedirect("GetTasks");
    }
}
