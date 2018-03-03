package src.client;

import com.google.gson.Gson;
import src.model.Task;
import src.model.packet.*;
import src.model.packet.Action;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Client implements Closeable{
    // TODO: Продумать как обрабатывать ошибки

    private Socket socket;
    private DataOutputStream serverWriter;
    private DataInputStream serverReader;

    public Client(String login) throws Exception {
        socket = new Socket("localhost", 777);
        serverWriter = new DataOutputStream(socket.getOutputStream());
        serverReader = new DataInputStream(socket.getInputStream());

        login(login);
    }

    public void login(String login) throws Exception {
        String jsonOutput = new Gson().toJson(Action.LOGIN);

        serverWriter.writeUTF(jsonOutput);
        serverWriter.flush();

        serverWriter.writeUTF(login);
        serverWriter.flush();

        String jsonInput = serverReader.readUTF();
        State answerFromServer = new Gson().fromJson(jsonInput, State.class);
        if (answerFromServer != State.OK) {
            throw new Exception("Client: Не удалось залогиниться!");
        }
        System.out.println("Client: Регистрация прошла, сервер ответил OK");
    }

    public void addTask(Task task) throws Exception {
        String jsonOutput = new Gson().toJson(Action.ADD_TASK);
        System.out.println("Client: Посылаем запрос на добавление");
        serverWriter.writeUTF(jsonOutput);
        serverWriter.flush();

        jsonOutput = new Gson().toJson(task);
        serverWriter.writeUTF(jsonOutput);
        serverWriter.flush();

        String jsonInput = serverReader.readUTF();
        State answerFromServer = new Gson().fromJson(jsonInput, State.class);
        if (answerFromServer != State.OK) {
            jsonOutput = new Gson().toJson(Action.EXIT);
            System.out.println("Client: Сматываем удочки");
            serverWriter.writeUTF(jsonOutput);
            serverWriter.flush();
            socket.close();
            throw new Exception("Client: Таск не добавился!!!");
        }
        System.out.println("Client: Таск добавился");
    }

    public void updateTask(Task oldTask, Task newTask) throws Exception {
        String jsonOutput = new Gson().toJson(Action.UPDATE_TASK);
        System.out.println("Client: Посылаем запрос на обновление таска");
        serverWriter.writeUTF(jsonOutput);
        serverWriter.flush();

        jsonOutput = new Gson().toJson(oldTask);
        serverWriter.writeUTF(jsonOutput);
        serverWriter.flush();
        jsonOutput = new Gson().toJson(newTask);
        serverWriter.writeUTF(jsonOutput);
        serverWriter.flush();

        String jsonInput = serverReader.readUTF();
        State answerFromServer = new Gson().fromJson(jsonInput, State.class);
        if (answerFromServer == State.OK) {
            System.out.println("Client: Заменили таск " + oldTask + " на " + newTask);
        }
    }

    public void deleteTask(Task task) throws IOException{
        String jsonOutput = new Gson().toJson(Action.DELETE_TASK);
        System.out.println("Client: Посылаем запрос на удаление");
        serverWriter.writeUTF(jsonOutput);
        serverWriter.flush();

        jsonOutput = new Gson().toJson(task);
        serverWriter.writeUTF(jsonOutput);
        serverWriter.flush();

        String jsonInput = serverReader.readUTF();
        State answerFromServer = new Gson().fromJson(jsonInput, State.class);
        if (answerFromServer == State.OK) {
            System.out.println("Client: Удалили таск " + task.toString());
        }
    }


    public List<Task> getAllTasks() throws Exception {
        List<Task> tasks = null;

        String jsonOutput = new Gson().toJson(Action.GET_ALL_TASKS);
        System.out.println("Client: Посылаем запрос на все таски");
        serverWriter.writeUTF(jsonOutput);
        serverWriter.flush();
        String jsonInput = serverReader.readUTF();
        State answerFromServer = new Gson().fromJson(jsonInput, State.class);
        if (answerFromServer == State.OK) {
            //Принемаем массив
            jsonInput = serverReader.readUTF();
            Task[] arrayTasks = new Gson().fromJson(jsonInput, Task[].class);

            tasks = Arrays.asList(arrayTasks);
            System.out.println("Client: Таски приняты");
        }

        return tasks;
    }

    @Override
    public void close() throws IOException {
        String jsonOutput = new Gson().toJson(Action.EXIT);
        System.out.println("Client: Сматываем удочки");
        serverWriter.writeUTF(jsonOutput);
        serverWriter.flush();

        serverReader.close();
        serverWriter.close();
        socket.close();
    }
}
