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

    private void sendAnswer(State answer, DataOutputStream writer) throws IOException{
        String jsonAnswer = new Gson().toJson(answer);
        writer.writeUTF(jsonAnswer);
        writer.flush();
    }

    @Override
    public void run() {

        try (DataInputStream reader = new DataInputStream(socket.getInputStream());
             DataOutputStream writer = new DataOutputStream(socket.getOutputStream())) {
            while (!socket.isClosed()) {
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

//                        "Регистрация" - новый login -> новый TaskList
                        if (taskList == null) {
                            taskList = new TaskList();
                        }

                        sendAnswer(State.OK, writer);

                        break;
                    }

                    case ADD_TASK: {
                        String jsonTask = reader.readUTF();
                        System.out.println("Server: Получили Task в виде строки.");

                        Task task = new Gson().fromJson(jsonTask, Task.class);

                        taskList.addTask(task);

                        sendAnswer(State.OK, writer);

                        System.out.println("Server: Добавил новый таск");
                        break;
                    }

                    case UPDATE_TASK: {
                        String jsonTask = reader.readUTF();
                        System.out.println("Server: Получили Task на обновление в виде строки.");

                        Task task = new Gson().fromJson(jsonTask, Task.class);
//                        System.out.println("Server: Успешно преобразовали в Task.");

                        taskList.deleteTask(task);
                        jsonTask = reader.readUTF();
                        System.out.println("Server: Получили новый Task в виде строки.");

                        task = new Gson().fromJson(jsonTask, Task.class);
//                        System.out.println("Server: Успешно преобразовали в Task.");
                        taskList.addTask(task);

                        sendAnswer(State.OK, writer);

                        break;
                    }
                    case DELETE_TASK: {
                        String jsonTask = reader.readUTF();
                        System.out.println("Server: Получили Task в виде строки.");

                        Task task = new Gson().fromJson(jsonTask, Task.class);
//                        int i = taskList.getTaskList().indexOf(task);
//                        taskList.deleteTask(taskList.getTaskList().get(i));
                        taskList.deleteTask(task);
                        System.out.println("Server: Удалил таск");

                        sendAnswer(State.OK, writer);

                        break;
                    }
                    case GET_ALL_TASKS: {
                        List<Task> tasks = taskList.getTaskList();

                        State answer = State.NO_TASKS;
                        String jsonTasks;
                        if (!tasks.isEmpty()) {
                            answer = State.OK;
                        } else {
                            System.out.println("Server: Тасков нет, ничего не отправил");
                        }

                        sendAnswer(answer, writer);

                        if (answer == State.OK) {
                                //jsonTasks = new Gson().toJson(tasks.toArray(), Task[].class);
                                //writer.writeUTF(jsonTasks);
                                //writer.flush();
                            ObjectOutputStream oos = new ObjectOutputStream(writer);
                            oos.writeObject(tasks);
                            oos.flush();
                            System.out.println("Server: Таски отправились");
                        }
                        break;
                    }

                    case COMPLETE_TASK: {
                        String jsonTask = reader.readUTF();
                        System.out.println("Server: Получили Task в виде строки.");

                        Task task = new Gson().fromJson(jsonTask, Task.class);
                        int i = taskList.getTaskList().indexOf(task);
                        taskList.complete(taskList.getTaskList().get(i));
                        System.out.println("Server: Завершил таск");

                        sendAnswer(State.OK, writer);

                        break;
                    }

                    case POSTPONE_TASK: {
                        String jsonTask = reader.readUTF();
                        System.out.println("Server: Получили Task в виде строки.");

                        Task task = new Gson().fromJson(jsonTask, Task.class);
//                        System.out.println("Server: Успешно преобразовали в Task.");

                        String jsonNewDateTime = reader.readUTF();
                        System.out.println("Server: Получили Новое время для таска.");

                        Calendar newDateTime = new Gson().fromJson(jsonNewDateTime, Calendar.class);
                        int i = taskList.getTaskList().indexOf(task);
                        taskList.postpone(taskList.getTaskList().get(i), newDateTime);
                        System.out.println("Server:  Отложили таск");

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
