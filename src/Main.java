package src;

import src.client.Client;
import src.server.controller.Controller;
import src.server.controller.TaskList;
import src.client.view.MainFrame;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        try {
            new Client("Alex");
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
