package src.controller;

import com.google.gson.Gson;

import java.io.*;

public class Controller {
    private static final String FILE_NAME = "TaskList.json";

    public static void writeTaskList(TaskList taskList) throws IOException {
        String outputStr = new Gson().toJson(taskList);

        FileWriter fileWriter = new FileWriter(FILE_NAME);
        fileWriter.write(outputStr);
        fileWriter.close();
    }

    public static TaskList readTaskList() throws IOException {
        FileReader reader = new FileReader(FILE_NAME);
        TaskList taskList = new Gson().fromJson(reader, TaskList.class);
        reader.close();
        return taskList;
    }
}
