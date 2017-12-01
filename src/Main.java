package src;

import src.controller.Controller;
import src.controller.TaskList;
import src.model.Task;

import java.io.*;
import java.util.ArrayList;
import java.util.Calendar;

public class Main {
    public static void main(String[] args) {
        Task task = new Task("Test", "It is test task",
                Calendar.getInstance(), "AlexRedby");

        System.out.println(task);

        Task task2 = new Task("Test2", "It is test task too",
                Calendar.getInstance(), "SvetlanaKlementeva");
        System.out.println(task2);

        TaskList taskList = new TaskList(new ArrayList<>());
        taskList.addTask(task);
        taskList.addTask(task2);
//        taskList.deleteTask(task);

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
