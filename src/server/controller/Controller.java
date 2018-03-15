package src.server.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import src.common.controller.TaskList;
import src.common.model.Constants;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

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
            return null;
        }
    }

    public static void writeUsers(Map users) {
        try (FileWriter fileWriter = new FileWriter(Constants.USERS_FILE_NAME)) {
            Gson gson = new GsonBuilder()
                    .setPrettyPrinting()
                    .create();
            String outputStr = gson.toJson(users);
            fileWriter.write(outputStr);
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    public static HashMap readUsers() {
        try (FileReader reader = new FileReader(Constants.USERS_FILE_NAME)) {
            return new Gson().fromJson(reader, HashMap.class);
        }
        catch (IOException e){
            return null;
        }
    }

}
