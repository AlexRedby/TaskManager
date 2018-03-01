package src.server;

import com.google.gson.*;
import src.model.packet.*;
import src.server.controller.Controller;
import src.server.controller.TaskList;
import src.model.Task;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

//Сначала серверу необходимо передать Action в виде строки(json).
//Уже написанны: LOGIN,
//               ADD_OR_UPDATE_TASK,
//               DELETE_TASK,
//               GET_ALL_TASK
//После, если необходимо, передать в виде строки необходимую информацию(Task сериализовать(json))
//
//Ответ сервера:
//Сервер всегда возвращает State, говоря о том, как завершилась его работа
//
//Если клиент запросил GET_ALL_TASK и есть хотя бы 1 Task, то после State, сервер вышлет МАСССИВ Task'ов
//Если же ни одного Task'а ещё нет, то State будет = NO_TASKS и после него больше ничего не последует
public class Server implements Runnable {

    private Socket socket;
    private TaskList taskList;
    private String fileName;

    public Server(Socket socket){
        this.socket = socket;
        taskList = null;
    }

    @Override
    public void run() {
        while (!socket.isClosed()) {
            try(DataInputStream reader = new DataInputStream(socket.getInputStream());
                DataOutputStream writer = new DataOutputStream(socket.getOutputStream())){

                String jsonAction = reader.readUTF();
                System.out.println("Server: Получили json c Action.");

                Action neededAction = new Gson().fromJson(jsonAction, Action.class);
                System.out.println("Server: Успешно преобразовали в Action.");

                switch (neededAction) {
                    case LOGIN: {
                        //Читаем Login
                        String login = reader.readUTF();
                        System.out.println("Server: Получили login.");

                        fileName = login + ".json";

                        //TODO: Если файл уже используется
                        taskList = Controller.readTaskList(fileName);
                        State answer = State.OK;

                        //"Регистрация" - новый login -> новый TaskList
                        if (taskList == null) {
                            taskList = new TaskList();
                        }

                        String jsonAnswer = new Gson().toJson(answer);
                        writer.writeUTF(jsonAnswer);
                        writer.flush();
                        break;
                    }
                    //TODO: Нужно проверить(не уверен что будет работать)
                    case ADD_OR_UPDATE_TASK: {
                        String jsonTask = reader.readUTF();
                        System.out.println("Server: Получили Task в виде строки.");

                        Task task = new Gson().fromJson(jsonTask, Task.class);
                        System.out.println("Server: Успешно преобразовали в Task.");

                        taskList.deleteTask(task);
                        taskList.addTask(task);

                        State answer = State.OK;

                        String jsonAnswer = new Gson().toJson(answer);
                        writer.writeUTF(jsonAnswer);
                        writer.flush();
                        break;
                    }
                    case DELETE_TASK: {
                        String jsonTask = reader.readUTF();
                        System.out.println("Server: Получили Task в виде строки.");

                        Task task = new Gson().fromJson(jsonTask, Task.class);
                        System.out.println("Server: Успешно преобразовали в Task.");

                        taskList.deleteTask(task);

                        State answer = State.OK;

                        String jsonAnswer = new Gson().toJson(answer);
                        writer.writeUTF(jsonAnswer);
                        writer.flush();
                        break;
                    }
                    case GET_ALL_TASKS: {
                        List<Task> tasks = taskList.getTaskList();

                        State answer = null;
                        String jsonTasks = null;
                        if(!tasks.isEmpty()) {
                            //Передаю массив, т.к. с списком не получилось
                            jsonTasks = new Gson().toJson(tasks.toArray());

                            answer = State.OK;
                        }
                        else
                            answer = State.NO_TASKS;

                        String jsonAnswer = new Gson().toJson(answer);
                        writer.writeUTF(jsonAnswer);
                        writer.flush();

                        if(answer == State.OK) {
                            writer.writeUTF(jsonTasks);
                            writer.flush();
                        }
                    }
                }

            } catch (Exception e) {
                System.out.println("Server: Возникла ошибка: " + e.getMessage());
            }
        }
    }

}
