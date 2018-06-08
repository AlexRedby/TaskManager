package src.common.controller;

import src.common.model.Task;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;

public class TaskList implements Serializable {
    private List<Task> taskList;

    public TaskList() {
        this.taskList = new ArrayList<>();
    }

    public TaskList(List<Task> taskList) {
        this.taskList = taskList;
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
        taskList.remove(task);
        task.setDateTime(dateTime);
        task.setActive(active);
        taskList.add(task);
    }

    public void updateTask(Task task){
        for (Task currentTask: taskList){
            if (currentTask.getId() == task.getId()){
               currentTask.setName(task.getName());
               currentTask.setActive(task.isActive());
               currentTask.setDateTime(task.getDateTime());
               currentTask.setContacts(task.getContacts());
               currentTask.setInfo(task.getInfo());
               return;
            }
        }
        addTask(task);
    }

    public boolean isExist(Task task) {
        return taskList.contains(task);
    }

    public Task getNearestTask(){
        List<Task> tasks = getTaskList(true);

        if(!tasks.isEmpty()) {
            Task nearestTask = tasks.get(0);

            for (Task currentTask : tasks) {
                if (currentTask.compareTo(nearestTask) < 0)
                    nearestTask = currentTask;
            }
            return nearestTask;
        }
        return null;
    }
}
