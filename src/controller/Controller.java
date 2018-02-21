package src.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import src.model.Constants;

import java.io.*;

public class Controller {

    public static void writeTaskList(TaskList taskList) {
        try (FileWriter fileWriter = new FileWriter(Constants.FILE_NAME)) {
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

    public static TaskList readTaskList() {
        try (FileReader reader = new FileReader(Constants.FILE_NAME)) {
            return new Gson().fromJson(reader, TaskList.class);
        }
        catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }
}
