package src.client.controller;

import src.client.view.AlarmFrame;
import src.client.view.MainFrame;
import src.common.model.Task;
import src.common.controller.TaskList;

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

    public void update(JFrame frame) {
        editingTaskList.values().remove(frame);
    }

    @Override
    public void run() {
        List<Task> activeTaskList = taskList.getTaskList(true);

        for (Task currentTask : activeTaskList) {
            if (currentTask.getDateTime().before(Calendar.getInstance())) {
                JFrame foundFrame = editingTaskList.get(currentTask);

                if (foundFrame == null || !foundFrame.isVisible()) {
                    JFrame frame = new AlarmFrame(currentTask, taskList, this,  mainFrame, mainFrame.getClient());
                    editingTaskList.put(currentTask, frame);
                }
            }
        }
    }

}
