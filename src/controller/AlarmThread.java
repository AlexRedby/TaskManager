package src.controller;

import src.model.Task;
import src.view.AlarmFrame;
import src.view.MainFrame;

import javax.swing.*;
import java.util.*;

public class AlarmThread extends TimerTask {
    private TaskList taskList;
    private MainFrame mainFrame;
    private Map<Task, JFrame> editingTaskList;

    public AlarmThread(TaskList taskList, MainFrame mainFrame) {
        this.taskList = taskList;
        this.mainFrame = mainFrame;
        editingTaskList = new HashMap<>();
    }

    @Override
    public void run() {
        List<Task> activeTaskList = taskList.getTaskList(true);

        for (Task currentTask : activeTaskList) {
            if (currentTask.getDateTime().before(Calendar.getInstance())) {
                JFrame foundFrame = editingTaskList.get(currentTask);

                if (foundFrame == null || !foundFrame.isVisible()) {
                    JFrame frame = new AlarmFrame(currentTask, taskList, mainFrame);
                    editingTaskList.put(currentTask, frame);
                }
            }
        }

        //Очистка Map
        Collection<JFrame> frames = editingTaskList.values();
        for (JFrame frame : frames)
            if (!frame.isVisible())
                frames.remove(frame);
    }

}
