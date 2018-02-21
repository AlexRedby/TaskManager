package src.controller;

import src.model.Task;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class TaskList implements Serializable {
    private List<Task> taskList;

    public TaskList() {
        this.taskList = new ArrayList<>();
    }

    public List<Task> getTaskList() {
        return taskList;
    }

    public List<Task> getTaskList(boolean active) {
        List<Task> newTaskList = new ArrayList<>();
        for (Task task : taskList) {
            if (task.isActive() == active) {
                newTaskList.add(task);
            }
        }
        return newTaskList;
    }

    public void setTaskList(List<Task> taskList) {
        this.taskList = taskList;
    }

    public void addTask(Task task) {
        taskList.add(task);
    }

    public void deleteTask(Task task) {
        taskList.remove(task);
    }

    //Откладывание задачи
    public void postpone(Task task, Calendar dateTime) {
        editTask(task, dateTime, true);
    }

    //Ставит задачу в неактивное состояние
    public void complete(Task task) {
        editTask(task, task.getDateTime(), false);
    }

    private void editTask(Task task, Calendar dateTime, boolean active) {
        task.setDateTime(dateTime);
        task.setActive(active);
    }

    public boolean isExist(Task task) {
        return taskList.contains(task);
    }
}
