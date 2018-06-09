package src.client;

import src.common.model.Constants;
import src.common.model.Task;
import src.common.model.packet.*;
import src.common.model.packet.Action;

import java.io.*;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Client implements Closeable {

    private Socket socket;
    private ObjectOutputStream serverWriter;
    private ObjectInputStream serverReader;
    private String login;

    public Client(String login, String password, boolean newUser) throws Exception {
        try {
            socket = new Socket("localhost", Constants.SERVER_PORT);
            serverWriter = new ObjectOutputStream(new DataOutputStream(socket.getOutputStream()));
            serverReader = new ObjectInputStream(new DataInputStream(socket.getInputStream()));
            //Ждём ответа от сервера 5 секунд
            socket.setSoTimeout(5000);
        } catch (Exception e) {
            throw new Exception("Не удалось соедениться с сервером");
        }

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

        try {
            return (State) serverReader.readObject();
        } catch (SocketTimeoutException ex) {
            close();
            throw new SocketTimeoutException("Не получили ответ от сервера!");
        }
    }

    private void login(String login, String password) throws Exception {
        if (login.equals("")) {
            throw new Exception("Не введен логин!");
        }
        if (password.equals("")) {
            throw new Exception("Не введен пароль!");
        }
        State answerFromServer;
        try {
            answerFromServer = sendRequest(Action.LOGIN, login, password);
        } catch (Exception e) {
            throw new Exception("Невозможно войти! Соединение с сервером прервано");
        }
            switch (answerFromServer) {
                case LOGIN_ERROR:
                    throw new Exception("Неверный логин!");
                case PASSWORD_ERROR:
                    throw new Exception("Неверный пароль!");
                case LOGIN_USED:
                    throw new Exception("Уже кто-то зашёл в систему под данным логином."
                            + "\nПовторите попытку позже.");
                case OK:
                    System.out.println("Client: Успешно вошли, сервер ответил OK");
                    break;
            }

    }

    private void register(String login, String password) throws Exception {
        if (login.equals("")) {
            throw new Exception("Не введен логин!");
        }
        if (password.equals("")) {
            throw new Exception("Не введен пароль!");
        }
        State answerFromServer;
        try {
            answerFromServer = sendRequest(Action.REGISTRATION, login, password);
        } catch (Exception e) {
            throw new Exception("Невозможно зарегестрироваться! Соединение с сервером прервано");
        }
            if (answerFromServer == State.LOGIN_ERROR) {
                throw new Exception("Логин уже занят. Придумайте новый.");
            }
            System.out.println("Client: Пользователь зарегистрирован, сервер ответил OK");
    }

    public void addTask(Task task) throws Exception {
        try {
            System.out.println("Client: Посылаем запрос на добавление");

            State answerFromServer = sendRequest(Action.ADD_TASK, task);
            if (answerFromServer != State.OK) {
                close();
                throw new Exception("Client: Таск не добавился!!!");
            }
            int id = (int) serverReader.readObject();
            task.setId(id);
            System.out.println("Client: Таск добавился");
        } catch (Exception e) {
            throw new Exception("Задача не добавлена! Соединение с сервером прервано");
        }
    }

    public void completeTask(Task task) throws Exception {
        try {
            System.out.println("Client: Посылаем запрос на завершение");

            State answerFromServer = sendRequest(Action.COMPLETE_TASK, task);
            if (answerFromServer != State.OK) {
                close();
                throw new Exception("Client: Не удалось завершить задачу!!!");
            }
            System.out.println("Client: Таск был отмечен как завершённый");
        } catch (Exception e) {
            throw new Exception("Задача не завершена! Соединение с сервером прервано");
        }
    }

    public void updateTask(Task newTask) throws Exception {
        try {
            System.out.println("Client: Посылаем запрос на обновление таска");
            State answerFromServer = sendRequest(Action.UPDATE_TASK, newTask);
            if (answerFromServer != State.OK) {
                close();
                throw new Exception("Client: Не удалось обновить таск!!!");
            }
            System.out.println("Client: Заменили таск на " + newTask);
        } catch (Exception e) {
            throw new Exception("Задача не обновлена! Соединение с сервером прервано");
        }
    }

    public void postponeTask(Task task, Calendar newDateTime) throws Exception {
        try {
            System.out.println("Client: Посылаем запрос на откладывание");
            State answerFromServer = sendRequest(Action.POSTPONE_TASK, task, newDateTime);
            if (answerFromServer != State.OK) {
                close();
                throw new Exception("Client: Таск не отложился!!!");
            }
            System.out.println("Client: Таск отложился");
        } catch (Exception e) {
            throw new Exception("Задача не отложена! Соединение с сервером прервано");
        }

    }

    public void deleteTask(Task task) throws Exception {
        try {
            System.out.println("Client: Посылаем запрос на удаление");
            State answerFromServer = sendRequest(Action.DELETE_TASK, task);
            if (answerFromServer != State.OK) {
                close();
                throw new Exception("Client: Таск не удалился!!!");
            }
            System.out.println("Client: Удалили таск " + task.toString());
        } catch (Exception e) {
            throw new Exception("Задача не удалена! Соединение с сервером прервано");
        }
    }

    public List<Task> getAllTasks() throws Exception {
        List<Task> tasks;
        try {
            System.out.println("Client: Посылаем запрос на все таски");
            State answerFromServer = sendRequest(Action.GET_ALL_TASKS);
            if (answerFromServer == State.OK) {
                tasks = (ArrayList<Task>) serverReader.readObject();
                System.out.println("Client: Таски приняты");
            } else if (answerFromServer == State.NO_TASKS) {
                tasks = new ArrayList<>();
            } else {
                close();
                throw new Exception("Client: Ошибка на сервере!");
            }

            return tasks;
        } catch (Exception e) {
            throw new Exception("Невозможно получить задачи! Соединение с сервером прервано");
        }

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
