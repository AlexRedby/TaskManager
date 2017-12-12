package src;

import src.controller.Controller;
import src.controller.TaskList;
import src.model.Task;

import java.io.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class Main {
    public static void main(String[] args) {
        Task task = new Task("Test", "It is test task",
                Calendar.getInstance(), "AlexRedby");

        System.out.println(task);
        Calendar date = new GregorianCalendar(2006, 11, 8);

        Task task2 = new Task("Test2", "It is test task too",
                date, "SvetlanaKlementeva");
        System.out.println(task2);

        Calendar date2 = new GregorianCalendar(2003, 2, 13);
        Task task3 = new Task("Test3", "It is test task too",
                date2, "SvetlanaKlementeva");
        System.out.println(task3);

        TaskList taskList = new TaskList(new ArrayList<>());
        taskList.addTask(task);
        taskList.addTask(task2);
        taskList.addTask(task3);
//        taskList.deleteTask(task);
        System.out.println();
        System.out.print(taskList.getActualTask());

        try {
            Controller.writeTaskList(taskList);
            TaskList newTaskList = Controller.readTaskList();
        } catch (IOException e) {
            System.out.print("Err!!");
        } catch (ClassNotFoundException e) {
            System.out.print("Err2!!");
        }

    }
}
