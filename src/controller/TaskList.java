package src.controller;

import src.model.Task;
import src.view.MainFrame;


import java.io.IOException;
import java.io.Serializable;
import java.util.*;


public class TaskList implements Serializable {
    private List<Task> taskList;

    public TaskList(){
        //TODO: Что-то мне это не нравится(как-то переделать?)
        try {
            this.taskList = Controller.readTaskList().getTaskList();
        } catch (IOException e) {
            //e.printStackTrace();
            this.taskList = new ArrayList<Task>();
        } catch (ClassNotFoundException e) {
            this.taskList = new ArrayList<Task>();
        }

        startAlarm();
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
        
        update();
    }

    public void deleteTask(Task task) {
        taskList.remove(task);
        
        update();
    }

    //Откладывание задачи
    public void postpone(Task task, Calendar dateTime){
        editTask(task, task.getName(), task.getInfo(), dateTime, task.getContacts(), true);
    }

    //Ставит задачу в неактивное состояние
    public void complete(Task task){
        editTask(task, task.getName(), task.getInfo(), task.getDateTime(), task.getContacts(), false);
    }

    public Task getActualTask(){
        List<Task> tl = this.getTaskList(true);
        if (tl.size() == 0){ // Возвращает null если список задач пуст
            return null;
        }
        tl.sort(new Task.dateTimeComparator());
        return tl.get(0);
    }

    public void editTask(Task task, String name, String info, Calendar dateTime, String contacts, boolean active){
        task.setName(name);
        task.setInfo(info);
        task.setDateTime(dateTime);
        task.setContacts(contacts);
        task.setActive(active);
        
        update();
    }

    public boolean isExist(Task task){
        if(taskList.contains(task))
            return true;
        for(Task currentTask : taskList)
            if(currentTask.getDateTime().equals(task.getDateTime())
                    && currentTask.getName().equals(task.getName())
                    && currentTask.getInfo().equals(task.getInfo())){
                return true;
            }
        return false;
    }
    
    //По-хоршему нужен интерфейс
    //transient - чтобы не сериализовался
    private transient MainFrame mainFrame;

    public void setChangeListener(MainFrame mainFrame){
        this.mainFrame = mainFrame;
    }

    private void update(){
        if(mainFrame != null)
            mainFrame.update();
    }
}
