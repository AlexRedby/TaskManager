package src;

import src.controller.Controller;
import src.controller.TaskList;
import src.view.MainFrame;

import javax.swing.*;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        TaskList taskList = Controller.readTaskList();
        if (taskList == null)
            taskList = new TaskList();

        JFrame frame = new MainFrame(taskList);
    }
}
