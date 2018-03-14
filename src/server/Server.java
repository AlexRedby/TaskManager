package src.server;

import com.google.gson.*;
import src.common.model.packet.*;
import src.server.controller.Controller;
import src.common.controller.TaskList;
import src.common.model.Task;

import java.io.*;
import java.net.Socket;
import java.util.Calendar;
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

    public Server(Socket socket) {
        this.socket = socket;
        taskList = null;
    }

    private void sendAnswer(State answer, ObjectOutputStream writer) throws IOException{
        writer.writeObject(answer);
        writer.flush();
    }

    @Override
    public void run() {

        try (ObjectInputStream reader = new ObjectInputStream(new DataInputStream(socket.getInputStream()));
             ObjectOutputStream writer = new ObjectOutputStream(new DataOutputStream(socket.getOutputStream()))) {
            while (!socket.isClosed()) {
                Action neededAction = (Action) reader.readObject();
                System.out.println("Server: Получили Action.");

                switch (neededAction) {
                    case LOGIN: {
                        //Читаем Login
                        String login = (String) reader.readObject();
                        System.out.println("Server: Получили login.");

                        fileName = login + ".json";

                        //TODO: Если файл уже используется

                        taskList = Controller.readTaskList(fileName);

//                        "Регистрация" - новый login -> новый TaskList
                        if (taskList == null) {
                            taskList = new TaskList();
                        }

                        sendAnswer(State.OK, writer);

                        break;
                    }

                    case ADD_TASK: {
                        Task task = (Task) reader.readObject();
                        System.out.println("Server: Получили Task в виде строки.");

                        taskList.addTask(task);
                        System.out.println("Server: Добавил новый таск");

                        sendAnswer(State.OK, writer);
                        break;
                    }

                    case UPDATE_TASK: {
                        Task task = (Task) reader.readObject();
                        System.out.println("Server: Получили старый Task.");
                        taskList.deleteTask(task);
                        System.out.println("Server: Удалили старый Task.");

                        task = (Task) reader.readObject();
                        System.out.println("Server: Получили новый Task.");
                        taskList.addTask(task);
                        System.out.println("Server: Добавили новый Task.");

                        sendAnswer(State.OK, writer);
                        break;
                    }
                    case DELETE_TASK: {
                        Task task = (Task) reader.readObject();
                        System.out.println("Server: Получили Task.");
                        taskList.deleteTask(task);
                        System.out.println("Server: Удалил Task.");

                        sendAnswer(State.OK, writer);
                        break;
                    }
                    case GET_ALL_TASKS: {
                        List<Task> tasks = taskList.getTaskList();

                        State answer = State.NO_TASKS;
                        if (!tasks.isEmpty()) {
                            answer = State.OK;
                        } else {
                            System.out.println("Server: Тасков нет, ничего не отправил");
                        }

                        sendAnswer(answer, writer);

                        if (answer == State.OK) {
                            writer.writeObject(tasks);
                            writer.flush();
                            System.out.println("Server: Таски отправились");
                        }
                        break;
                    }

                    case COMPLETE_TASK: {
                        Task task = (Task) reader.readObject();
                        System.out.println("Server: Получили Task.");
                        taskList.complete(task);
                        System.out.println("Server: Завершил Task.");

                        sendAnswer(State.OK, writer);
                        break;
                    }

                    case POSTPONE_TASK: {
                        Task task = (Task) reader.readObject();
                        System.out.println("Server: Получили Task.");

                        Calendar newDateTime = (Calendar) reader.readObject();
                        System.out.println("Server: Получили Новое время для таска.");

                        taskList.postpone(task, newDateTime);
                        System.out.println("Server: Отложили таск");

                        sendAnswer(State.OK, writer);
                        break;
                    }

                    case EXIT: {
                        Controller.writeTaskList(taskList, fileName);
                        System.out.println("Server: Таски записаны в файл");
                        socket.close();
                        break;
                    }
                }

            }
            System.out.println("Server: Работа с клиентом " + fileName + " завершена");
        } catch (Exception e) {
            System.out.println("Server: Возникла ошибка: " + e.getMessage());

        }
    }

}
