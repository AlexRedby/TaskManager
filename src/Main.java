package src;

import src.server.controller.Controller;
import src.server.controller.TaskList;
import src.client.view.MainFrame;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        TaskList taskList = Controller.readTaskList();
        if (taskList == null)
            taskList = new TaskList();

        JFrame frame = new MainFrame(taskList);
    }
}
