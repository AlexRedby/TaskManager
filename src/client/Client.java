package src.client;

import com.google.gson.Gson;
import src.common.model.Task;
import src.common.model.packet.*;
import src.common.model.packet.Action;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class Client implements Closeable {
    // TODO: Продумать как обрабатывать ошибки

    private Socket socket;
    private ObjectOutputStream serverWriter;
    private ObjectInputStream serverReader;
    private String login;

    public Client(String login, String password, boolean newUser) throws Exception {
        socket = new Socket("localhost", 777);
        serverWriter = new ObjectOutputStream(new DataOutputStream(socket.getOutputStream()));
        serverReader = new ObjectInputStream(new DataInputStream(socket.getInputStream()));
        this.login = login;
        if (newUser) {
            register(login, password);
        } else {
            login(login, password);
        }
    }

    public String getLogin() {
        return login;
    }

    //Отпрравляет action(действие) серверу, которое он хочет выполнить и необходимые данные, если нужно
    //Возвращает результат(state) работы сервера
    private State sendRequest(Action action, Object... objects) throws IOException, ClassNotFoundException {
        serverWriter.writeObject(action);
        serverWriter.flush();

        for (Object obj : objects) {
            serverWriter.writeObject(obj);
            serverWriter.flush();
        }

        return (State) serverReader.readObject();
    }

    public void login(String login, String password) throws Exception {

        State answerFromServer = sendRequest(Action.LOGIN, login, password);
        if (answerFromServer != State.OK) {
            if (answerFromServer == State.LOGIN_ERROR) {
                throw new Exception("Не верный логин!");
            } else {
                throw new Exception("Не верный пароль!");
            }
        }
        System.out.println("Client: Успешно вошли, сервер ответил OK");
    }

    public void register(String login, String password) throws Exception {
        State answerFromServer = sendRequest(Action.REGISTRATION, login, password);
        if (answerFromServer != State.OK) {
            throw new Exception("Client: Ошибка!");
        }
        System.out.println("Client: Пользователь зарегистрирован, сервер ответил OK");
    }

    public void addTask(Task task) throws Exception {
        System.out.println("Client: Посылаем запрос на добавление");
        State answerFromServer = sendRequest(Action.ADD_TASK, task);
        if (answerFromServer != State.OK) {
            close();
            throw new Exception("Client: Таск не добавился!!!");
        }
        System.out.println("Client: Таск добавился");
    }

    public void completeTask(Task task) throws Exception {
        System.out.println("Client: Посылаем запрос на завершение");

        State answerFromServer = sendRequest(Action.COMPLETE_TASK, task);
        if (answerFromServer != State.OK) {
            close();
            throw new Exception("Client: Не удалось завершить задачу!!!");
        }
        System.out.println("Client: Таск был отмечен как завершённый");
    }

    public void updateTask(Task oldTask, Task newTask) throws Exception {
        System.out.println("Client: Посылаем запрос на обновление таска");
        State answerFromServer = sendRequest(Action.UPDATE_TASK, oldTask, newTask);
        if (answerFromServer != State.OK) {
            close();
            throw new Exception("Client: Не удалось обновить таск!!!");
        }
        System.out.println("Client: Заменили таск " + oldTask + " на " + newTask);
    }

    public void postponeTask(Task task, Calendar newDateTime) throws Exception {
        System.out.println("Client: Посылаем запрос на откладывание");
        State answerFromServer = sendRequest(Action.POSTPONE_TASK, task, newDateTime);
        if (answerFromServer != State.OK) {
            close();
            throw new Exception("Client: Таск не отложился!!!");
        }
        System.out.println("Client: Таск отложился");
    }

    public void deleteTask(Task task) throws Exception {
        System.out.println("Client: Посылаем запрос на удаление");
        State answerFromServer = sendRequest(Action.DELETE_TASK, task);
        if (answerFromServer != State.OK) {
            close();
            throw new Exception("Client: Таск не удалился!!!");
        }
        System.out.println("Client: Удалили таск " + task.toString());
    }

    public List<Task> getAllTasks() throws Exception {
        List<Task> tasks = null;

        System.out.println("Client: Посылаем запрос на все таски");
        State answerFromServer = sendRequest(Action.GET_ALL_TASKS);
        if (answerFromServer == State.OK) {
            tasks = (ArrayList<Task>) serverReader.readObject();
            System.out.println("Client: Таски приняты");
        } else {
            tasks = new ArrayList<>();
        }

        return tasks;
    }

    @Override
    public void close() throws IOException {
        System.out.println("Client: Сматываем удочки");
        serverWriter.writeObject(Action.EXIT);
        serverWriter.flush();

        serverReader.close();
        serverWriter.close();
        socket.close();
    }
}
