package src.controller;

import src.model.Task;

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
                //TODO получить самую ближайшую задачу из taskList
                //??? Написать такой метод в TaskList
                //Task task = taskList.getActualTask();
                //if(task.getDateTime().compareTo(Calendar.getInstance()) <= 0)
                    //TODO создать Frame оповещающий о завершении задачи
                    //??? слушатель
                    ;

                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
