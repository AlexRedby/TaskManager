package src;

import src.client.Client;
import src.server.controller.Controller;
import src.server.controller.TaskList;
import src.client.view.MainFrame;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        /*TaskList taskList = Controller.readTaskList();
        if (taskList == null)
            taskList = new TaskList();

        JFrame frame = new MainFrame(taskList);*/
        try {
            new Client("Alex");

            System.out.println("Alex успешно залогинился!");

            new Client("Sveta");
        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }
}
