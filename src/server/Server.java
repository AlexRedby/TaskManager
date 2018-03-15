package src.server;

import src.common.model.packet.*;
import src.server.controller.Controller;
import src.common.controller.TaskList;
import src.common.model.Task;

import java.io.*;
import java.net.Socket;
import java.util.*;

//Сначала серверу необходимо передать Action в виде строки(json).
//
//После, если необходимо, передать в виде строки необходимую информацию
//
//Ответ сервера:
//Сервер всегда возвращает State, говоря о том, как завершилась его работа
//
//Если клиент запросил GET_ALL_TASK и есть хотя бы 1 Task, то после State, сервер вышлет списков Task'ов
//Если же ни одного Task'а ещё нет, то State будет = NO_TASKS и после него больше ничего не последует
public class Server implements Runnable {

    private Socket socket;
    private TaskList taskList;
    private String fileName;
    private String login;
    private ActiveUsers activeUsers;

    public Server(Socket socket, ActiveUsers activeUsers) {
        this.socket = socket;
        this.activeUsers = activeUsers;
        taskList = null;
    }

    private void sendAnswer(State answer, ObjectOutputStream writer) throws IOException {
        writer.writeObject(answer);
        writer.flush();
    }

    @Override
    public void run() {

        try (ObjectInputStream reader = new ObjectInputStream(new DataInputStream(socket.getInputStream()));
             ObjectOutputStream writer = new ObjectOutputStream(new DataOutputStream(socket.getOutputStream()))) {
            while (!socket.isClosed()) {
                Action neededAction = (Action) reader.readObject();
                System.out.println("Server: Получили Action " + neededAction.toString());

                switch (neededAction) {
                    case LOGIN: {
                        //Читаем Login
                        String login = (String) reader.readObject();
                        System.out.println("Server: Получили login.");
                        //Читаем пароль
                        String password = (String) reader.readObject();
                        System.out.println("Server: Получили пароль.");

                        //Если в списке юзеров нет полученного логина или пароль не совпадает возвращается ERROR
                        HashMap users = Controller.readUsers();
                        if (users.containsKey(login)) {
                            //Проверка на использование данного логина в данный момент
                            if (activeUsers.contains(login)) {
                                sendAnswer(State.LOGIN_USED, writer);
                                System.out.println("Server: Логин(" + login + ") уже используется кем-то.");
                            }
                            if (users.get(login).equals(password)) {
                                fileName = login + ".json";
                                this.login = login;
                                taskList = Controller.readTaskList(fileName);
                                sendAnswer(State.OK, writer);

                                activeUsers.add(login);

                                System.out.println("Server: Пользователь " + login + " вошел");
                            } else {
                                sendAnswer(State.PASSWORD_ERROR, writer);
                                System.out.println("Server: Получили не верный пароль.");
                            }
                            break;
                        } else {
                            sendAnswer(State.LOGIN_ERROR, writer);
                            System.out.println("Server: Получили не верный логин.");
                            break;
                        }
                    }
                    case REGISTRATION: {
                        //Читаем Login
                        String login = (String) reader.readObject();
                        System.out.println("Server: Получили login.");
                        //Читаем пароль
                        String password = (String) reader.readObject();
                        System.out.println("Server: Получили пароль.");
                        HashMap users = Controller.readUsers();

                        if (!users.containsKey(login)) {
                            users.put(login, password);
                            Controller.writeUsers(users);
                            fileName = login + ".json";
                            this.login = login;
                            taskList = new TaskList();
                            Controller.writeTaskList(taskList, fileName);
                            sendAnswer(State.OK, writer);

                            activeUsers.add(login);

                            System.out.println("Server: Зарегестрировали нового пользователя " + login);
                        } else {
                            sendAnswer(State.LOGIN_ERROR, writer);
                            System.out.println("Server: Не удалось зарестрировать " + login
                                    + ", т.к. он уже есть в системе");
                        }
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
                        activeUsers.remove(login);
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
