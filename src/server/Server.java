package src.server;

import src.common.model.User;
import src.common.model.packet.*;
import src.common.controller.TaskList;
import src.common.model.Task;
import src.server.db.TasksTable;
import src.server.db.UsersTable;

import java.security.MessageDigest;

import java.io.*;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.util.*;

//Сначала серверу необходимо передать Action.
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
    private User user;
    private ActiveUsers activeUsers;

    public Server(Socket socket) {
        this.socket = socket;
        this.activeUsers = ActiveUsers.getInstance();
        taskList = new TaskList();
    }

    private void sendAnswer(State answer, ObjectOutputStream writer) throws IOException {
        writer.writeObject(answer);
        writer.flush();
    }

    //Шифрует пароль
    private String encrypt(String password, String login) throws NoSuchAlgorithmException {
        // Используем шифрование
        MessageDigest sha1 = MessageDigest.getInstance("SHA-1");
        // Добавляем к паролю соль (пусть будет логин) и зашифровываем
        byte[] encryptPass = sha1.digest((login + password).getBytes());
        // Переводим в 16-ричный вид
        StringBuilder sb = new StringBuilder();
        for (byte b : encryptPass) {
            sb.append(String.format("%02X", b));
        }
        return sb.toString();
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
                        //TO-DO: Заменить передачу логина и пароля на передачу User
                        //TO-DO: Шифрование сделать в клиенте?

                        //Читаем Login
                        String login = (String) reader.readObject();
                        System.out.println("Server: Получили login.");
                        //Читаем пароль
                        String password = (String) reader.readObject();
                        System.out.println("Server: Получили пароль.");

                        if(UsersTable.haveLogin(login)){
                            //Проверка на использование данного логина в данный момент
                            if (activeUsers.contains(login)) {
                                sendAnswer(State.LOGIN_USED, writer);
                                System.out.println("Server: Логин(" + login + ") уже используется кем-то.");
                                socket.close();
                                break;
                            }

                            //Шифруем пароль
                            String encryptPassStr = encrypt(password, login);
                            user = new User(login, encryptPassStr);

                            user = UsersTable.get(user);
                            if (user != null) {
                                taskList = new TaskList(TasksTable.getAll(user));

                                sendAnswer(State.OK, writer);

                                activeUsers.add(login);
                                System.out.println("Server: Пользователь " + login + " вошел");
                            } else {
                                sendAnswer(State.PASSWORD_ERROR, writer);
                                System.out.println("Server: Получили неверный пароль.");
                                socket.close();
                            }

                        } else {
                            sendAnswer(State.LOGIN_ERROR, writer);
                            System.out.println("Server: Получили неверный логин.");
                            socket.close();
                        }
                        break;
                    }
                    case REGISTRATION: {
                        //Читаем Login
                        String login = (String) reader.readObject();
                        System.out.println("Server: Получили login.");
                        //Читаем пароль
                        String password = (String) reader.readObject();
                        System.out.println("Server: Получили пароль.");

                        if(!UsersTable.haveLogin(login)){
                            taskList = new TaskList();

                            String encryptPassStr = encrypt(password, login);
                            user = new User(login, encryptPassStr);

                            int id = UsersTable.add(user);
                            user.setId(id);

                            sendAnswer(State.OK, writer);

                            activeUsers.add(login);

                            System.out.println("Server: Зарегестрировали нового пользователя " + login);
                        } else {
                            sendAnswer(State.LOGIN_ERROR, writer);
                            System.out.println("Server: Не удалось зарестрировать " + login
                                    + ", т.к. он уже есть в системе");
                            socket.close();
                        }
                        break;
                    }

                    case ADD_TASK: {
                        Task task = (Task) reader.readObject();
                        System.out.println("Server: Получили Task");

                        task.setId(TasksTable.add(task,user));

                        taskList.addTask(task);
                        System.out.println("Server: Добавил новый таск");
                        sendAnswer(State.OK, writer);
                        writer.writeObject(task.getId());
                        writer.flush();
                        break;
                    }

                    case UPDATE_TASK: {
                        Task task = (Task) reader.readObject();
                        System.out.println("Server: Получили новый Task.");

                        TasksTable.update(task);

                        taskList.updateTask(task);
                        System.out.println("Server: заменили старый Task на новый.");

                        sendAnswer(State.OK, writer);
                        break;
                    }
                    case DELETE_TASK: {
                        Task task = (Task) reader.readObject();
                        System.out.println("Server: Получили Task.");

                        TasksTable.delete(task);

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

                        TasksTable.complete(task);

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

                        TasksTable.postpone(task, newDateTime);

                        taskList.postpone(task, newDateTime);
                        System.out.println("Server: Отложили таск");

                        sendAnswer(State.OK, writer);
                        break;
                    }

                    case EXIT: {
                        //IOHelper.writeTaskList(taskList, fileName);
                        activeUsers.remove(user.getName());
//                        System.out.println("Server: Таски записаны в файл");
                        socket.close();
                        break;
                    }
                }

            }
            System.out.println("Server: Работа с клиентом " + user.getName() + " завершена");
        } catch (Exception e) {
            System.out.println("Server: Возникла ошибка: " + e.getMessage());
            activeUsers.remove(user.getName());
            try(ObjectOutputStream writer = new ObjectOutputStream(new DataOutputStream(socket.getOutputStream()))){
                sendAnswer(State.ERROR, writer);
            }
            catch (IOException ex){
                System.out.println("Server: Не удалось отправить сообщение об ошибке на клиент");
            }
        }
    }

}
