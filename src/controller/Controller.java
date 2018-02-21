package src.controller;

import com.google.gson.Gson;

import java.io.*;

public class Controller {
    private static final String FILE_NAME = "TaskList.json";

    public static void writeTaskList(TaskList taskList) throws IOException {
        try (FileWriter fileWriter = new FileWriter(FILE_NAME)) {
            String outputStr = new Gson().toJson(taskList);
            fileWriter.write(outputStr);
        }
    }

    public static TaskList readTaskList() throws IOException {
        try (FileReader reader = new FileReader(FILE_NAME)) {
            return new Gson().fromJson(reader, TaskList.class);
        }
    }
}
