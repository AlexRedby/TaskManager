package src;

import src.controller.Controller;
import src.controller.TaskList;
import src.model.Task;
import src.view.MainFrame;


import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class Main {
    public static void main(String[] args) {
        TaskList taskList = null;
        try {
            taskList = Controller.readTaskList();
        } catch (IOException e) {
            taskList = new TaskList();
        }
        JFrame frame = new MainFrame(taskList);
    }
}
