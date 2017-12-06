package src.controller;

import src.model.Task;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class TaskList implements Serializable {
    private List<Task> taskList;

    public TaskList(){
        this(new ArrayList<Task>());
    }

    public TaskList(List<Task> taskList){
        this.taskList = taskList;
        startAlarm();
    }

    public List<Task> getTaskList() {
        return taskList;
    }
    public List<Task> getTaskList(boolean active) {
        List<Task> newTaskList = new ArrayList<>();
        for ( Task task: taskList){
            if (task.isActive() == active){
                newTaskList.add(task);
            }
        }
        return newTaskList;
    }

    private void startAlarm(){
        new Thread(new AlarmThread(this)).start();
    }

    public void setTaskList(List<Task> taskList) {
        this.taskList = taskList;
    }

    public void addTask(Task task){
        taskList.add(task);
    }

    public void deleteTask(Task task) {
        taskList.remove(task);
    }

    //Откладывание задачи
    public void postpone(Task task, Calendar dateTime){
        editTask(task, task.getName(), task.getInfo(), dateTime, task.getContacts(), true);
    }

    //Ставит задачу в неактивное состояние
    public void complete(Task task){
        editTask(task, task.getName(), task.getInfo(), task.getDateTime(), task.getContacts(), false);
    }

    public void editTask(Task task, String name, String info, Calendar dateTime, String contacts, boolean active){
        task.setName(name);
        task.setInfo(info);
        task.setDateTime(dateTime);
        task.setContacts(contacts);
        task.setActive(active);
    }

    public boolean isExist(Task task){
        return taskList.contains(task);
    }
}
