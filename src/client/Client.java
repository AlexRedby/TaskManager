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


public class Client {
    // TODO: Как закрывать Client(socket)?
    // TODO: Продумать как обрабатывать ошибки

    private Socket socket;

    public Client(String login) throws Exception {
        socket = new Socket("localhost", 777);

        login(login);
    }

    public void login(String login) throws Exception{
        try (DataOutputStream serverWriter = new DataOutputStream(socket.getOutputStream());
             DataInputStream serverReader = new DataInputStream(socket.getInputStream())) {

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
    }

    public void addTask(Task task) throws Exception {
        try (DataOutputStream serverWriter = new DataOutputStream(socket.getOutputStream());
             DataInputStream serverReader = new DataInputStream(socket.getInputStream())) {
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
        } catch (IOException ex) { //Как-то сообщить об ошибке
            ex.printStackTrace();
        }
    }

    public void updateTask(Task oldTask, Task newTask) throws Exception{
        try (DataOutputStream serverWriter = new DataOutputStream(socket.getOutputStream());
             DataInputStream serverReader = new DataInputStream(socket.getInputStream())) {

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
    }

    public void deleteTask(Task task){
        try (DataOutputStream serverWriter = new DataOutputStream(socket.getOutputStream());
             DataInputStream serverReader = new DataInputStream(socket.getInputStream())) {

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
        } catch (IOException ex) { //Как-то сообщить об ошибке
            ex.printStackTrace();
        }
    }


    public List<Task> getAllTasks() throws Exception {
        List<Task> tasks = new ArrayList<>();

        try (DataOutputStream serverWriter = new DataOutputStream(socket.getOutputStream());
             DataInputStream serverReader = new DataInputStream(socket.getInputStream())) {
            String jsonOutput = new Gson().toJson(Action.GET_ALL_TASKS);
            System.out.println("Client: Посылаем запрос на все таски");
            serverWriter.writeUTF(jsonOutput);
            serverWriter.flush();
            String jsonInput = serverReader.readUTF();
            State answerFromServer = new Gson().fromJson(jsonInput, State.class);
            if (answerFromServer == State.NO_TASKS) {

                jsonOutput = new Gson().toJson(Action.EXIT);
                //TODO: Зачем сматывать удочки? Всё же хорошо... Это говорит о том, что сервер ничего не вернёт
                System.out.println("Client: Сматываем удочки");
                serverWriter.writeUTF(jsonOutput);
                serverWriter.flush();
                socket.close();
                throw new Exception("Client: Тасков нет!!!");
            }

            //Принемаем массив
            jsonInput = serverReader.readUTF();
            Task[] arrayTasks = new Gson().fromJson(jsonInput, Task[].class);

            tasks = Arrays.asList(arrayTasks);
            System.out.println("Client: Таски приняты");
        } catch (IOException ex) { //Как-то сообщить об ошибке
            ex.printStackTrace();
        }

        return tasks;
    }

}
