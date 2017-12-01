package src.controller;

import src.model.Task;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class TaskList implements Serializable {
    private List<Task> taskList;

    public TaskList(List<Task> taskList){
        this.taskList = taskList;
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

    public void setTaskList(List<Task> taskList) {
        this.taskList = taskList;
    }

    public void addTask(Task task){
        taskList.add(task);
    }

    public void deleteTask(Task task) {
        taskList.remove(task);
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
