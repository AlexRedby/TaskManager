package src.server.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;

public class Controller {

    public static void writeTaskList(TaskList taskList, String fileName) {
        try (FileWriter fileWriter = new FileWriter(fileName)) {
            Gson gson = new GsonBuilder()
                    .setPrettyPrinting()
                    .create();
            String outputStr = gson.toJson(taskList);
            fileWriter.write(outputStr);
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    public static TaskList readTaskList(String fileName) {
        try (FileReader reader = new FileReader(fileName)) {
            return new Gson().fromJson(reader, TaskList.class);
        }
        catch (IOException e){
//            e.printStackTrace();
            return null;
        }
    }
}
