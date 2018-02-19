package src.controller;

import java.io.*;

public class Controller {

    public static void writeTaskList(TaskList taskList) throws IOException {
        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("TaskLog.bin"));
        out.writeObject(taskList);
        out.close();
    }

    public static TaskList readTaskList() throws IOException, ClassNotFoundException {
        ObjectInputStream in = new ObjectInputStream(new FileInputStream("TaskLog.bin"));
        TaskList readedTaskList = (TaskList) in.readObject();
        in.close();
        return readedTaskList;
    }
}
