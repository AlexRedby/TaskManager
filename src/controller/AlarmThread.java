package src.controller;

import src.model.Task;
import src.view.AlarmFrame;

import javax.swing.*;
import java.util.Calendar;

public class AlarmThread implements Runnable {
    TaskList taskList;

    public AlarmThread(TaskList taskList){
        this.taskList = taskList;
    }

    @Override
    public void run() {
        while(true){
            try {
                Task task = taskList.getActualTask();
                if(task.getDateTime().compareTo(Calendar.getInstance()) <= 0) {

                    JFrame frame = new AlarmFrame(task, taskList);
                    while (frame.isVisible())
                        Thread.sleep(1000);
                }

                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
