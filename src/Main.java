package src;

import src.client.Client;
import src.model.Task;
import src.server.controller.Controller;
import src.server.controller.TaskList;
import src.client.view.MainFrame;

import javax.swing.*;
import java.util.Calendar;

public class Main {
    public static void main(String[] args) {
        try {
            Client client = new Client("Alex");
            client.addTask(new Task());
            client.addTask(new Task("123", "123", Calendar.getInstance(), "123", true));
            System.out.println("Alex успешно отработал!");
        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
        try {
            new Client("Sveta");
            System.out.println("Sveta успешно отработал!");
        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
        }

    }
}
